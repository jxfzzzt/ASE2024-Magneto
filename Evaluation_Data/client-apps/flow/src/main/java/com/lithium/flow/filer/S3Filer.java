/*
 * Copyright 2015 Lithium Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lithium.flow.filer;

import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.flow.access.Access;
import com.lithium.flow.access.Prompt.Response;
import com.lithium.flow.access.Prompt.Type;
import com.lithium.flow.config.Config;
import com.lithium.flow.config.Configs;
import com.lithium.flow.io.DataIo;
import com.lithium.flow.streams.CounterInputStream;
import com.lithium.flow.util.Lazy;
import com.lithium.flow.util.LimiterInputStream;
import com.lithium.flow.util.Needle;
import com.lithium.flow.util.Threader;
import com.lithium.flow.util.UncheckedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpStatus;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.util.IOUtils;
import com.google.common.util.concurrent.RateLimiter;

/**
 * @author Matt Ayres
 */
public class S3Filer implements Filer {
	private final AmazonS3 internalS3;
	private final URI uri;
	private final String bucket;
	private final long partSize;
	private final long maxDrainBytes;
	private final boolean bypassCreateDirs;
	private final StorageClass storageClass;
	private final RateLimiter limiter;
	private final RateLimiter bitLimiter;
	private final Lazy<Threader> threader;

	public S3Filer(@Nonnull Config config, @Nonnull Access access) {
		this(config, buildS3(config, access));
	}

	public S3Filer(@Nonnull AmazonS3 s3) {
		this(Configs.empty(), s3);
	}

	public S3Filer(@Nonnull Config config, @Nonnull AmazonS3 s3) {
		checkNotNull(config);
		this.internalS3 = checkNotNull(s3);

		String url = config.getString("url");
		uri = getBaseURI(url);
		bucket = getBucket(url);
		partSize = config.getInt("s3.partSize", 5 * 1024 * 1024);
		maxDrainBytes = config.getInt("s3.maxDrainBytes", 128 * 1024);
		bypassCreateDirs = config.getBoolean("s3.bypassCreateDirs", false);
		storageClass = StorageClass.fromValue(config.getString("s3.storageClass", "STANDARD"));
		limiter = RateLimiter.create(config.getDouble("s3.rateLimit", 3400));
		bitLimiter = RateLimiter.create(config.getDouble("s3.bitLimit", Double.MAX_VALUE));

		int threads = config.getInt("s3.threads", 8);
		int maxQueued = config.getInt("s3.maxQueued", threads);
		threader = new Lazy<>(() -> new Threader(threads).setMaxQueued(maxQueued));
	}


	@Override
	@Nonnull
	public URI getUri() {
		return uri;
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		String prefix = path.isEmpty() || path.equals("/") ? "" : keyForPath(path) + "/";
		ListObjectsV2Request request = new ListObjectsV2Request()
				.withBucketName(bucket).withPrefix(prefix).withDelimiter("/");

		List<Record> records = new ArrayList<>();
		Set<String> names = new HashSet<>();

		ListObjectsV2Result listing;
		do {
			listing = get(s3 -> s3.listObjectsV2(request));

			for (String dir : listing.getCommonPrefixes()) {
				if (dir.startsWith(prefix)) {
					String name = dir.substring(prefix.length()).replace("/", "");
					if (names.add(name)) {
						records.add(new Record(uri, RecordPath.from(path, name), 0, 0, true));
					}
				}
			}

			for (S3ObjectSummary summary : listing.getObjectSummaries()) {
				if (!summary.getKey().endsWith("/")) {
					String name = RecordPath.getName(summary.getKey());
					long time = summary.getLastModified().getTime();
					long size = summary.getSize();
					records.add(new Record(uri, RecordPath.from(path, name), time, size, false));
				}
			}

			request.setContinuationToken(listing.getNextContinuationToken());
		} while (listing.isTruncated());

		return records;
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) throws IOException {
		try {
			ObjectMetadata metadata = s3().getObjectMetadata(bucket, keyForPath(path));
			long time = metadata.getLastModified().getTime();
			long size = metadata.getContentLength();
			boolean directory = path.endsWith("/");
			return new Record(uri, RecordPath.from(path), time, size, directory);
		} catch (AmazonServiceException e) {
			if (e.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return Record.noFile(uri, path);
			}
			throw new IOException(e);
		} catch (SdkClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		S3Object object = get(s3 -> s3.getObject(bucket, keyForPath(path)));
		S3ObjectInputStream s3In = object.getObjectContent();
		long length = object.getObjectMetadata().getContentLength();
		AtomicLong counter = new AtomicLong();

		return new CounterInputStream(s3In, counter) {
			@Override
			public void close() throws IOException {
				long drainBytes = length - counter.get();
				if (drainBytes > maxDrainBytes) {
					s3In.abort();
				} else {
					// drain to allow S3 connection reuse
					IOUtils.drainInputStream(in);
				}
				super.close();
			}
		};
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) {
		return new OutputStream() {
			private final String key = keyForPath(path);
			private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			private Needle<PartETag> needle;
			private String uploadId;
			private boolean closed;

			@Override
			public void write(int b) throws IOException {
				baos.write(b);
				flip(partSize);
			}

			@Override
			public void write(@Nonnull byte[] b) throws IOException {
				write(b, 0, b.length);
			}

			@Override
			public void write(@Nonnull byte[] b, int off, int len) throws IOException {
				baos.write(b, off, len);
				flip(partSize);
			}

			@Override
			public void close() throws IOException {
				if (closed) {
					return;
				}

				if (needle == null) {
					InputStream in = nextInputStream();
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(baos.size());
					PutObjectRequest request = new PutObjectRequest(bucket, key, in, metadata)
							.withStorageClass(storageClass);
					use(s3 -> s3.putObject(request));
				} else {
					flip(1);

					try {
						List<PartETag> tags = needle.toList();

						use(s3 -> s3.completeMultipartUpload(
								new CompleteMultipartUploadRequest(bucket, key, uploadId, tags)));
					} catch (UncheckedException e) {
						use(s3 -> s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucket, key, uploadId)));
						throw e.unwrap(IOException.class);
					}
				}

				closed = true;
			}

			private void flip(long minSize) throws IOException {
				if (baos.size() < minSize) {
					return;
				}

				if (needle == null) {
					needle = threader.get().needle();
					InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, key)
							.withStorageClass(storageClass);
					uploadId = get(s3 -> s3.initiateMultipartUpload(request).getUploadId());
				}

				InputStream in = nextInputStream();
				int partNum = needle.size() + 1;

				UploadPartRequest uploadRequest = new UploadPartRequest()
						.withUploadId(uploadId)
						.withBucketName(bucket)
						.withKey(key)
						.withPartNumber(partNum)
						.withPartSize(baos.size())
						.withInputStream(in);

				needle.submit(uploadId + "@" + partNum, () -> get(s3 -> s3.uploadPart(uploadRequest).getPartETag()));

				baos.reset();
			}

			@Nonnull
			private InputStream nextInputStream() {
				return new LimiterInputStream(new ByteArrayInputStream(baos.toByteArray()), bitLimiter);
			}
		};
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		String key = keyForPath(path);
		ObjectMetadata metadata = get(s3 -> s3.getObjectMetadata(bucket, key));
		metadata.setLastModified(new Date(time));
		use(s3 -> s3.copyObject(new CopyObjectRequest(bucket, key, bucket, key).withNewObjectMetadata(metadata)));
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		use(s3 -> s3.deleteObject(bucket, keyForPath(path)));
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		String oldKey = keyForPath(oldPath);
		String newKey = keyForPath(newPath);
		use(s3 -> s3.copyObject(new CopyObjectRequest(bucket, oldKey, bucket, newKey).withStorageClass(storageClass)));
		use(s3 -> s3.deleteObject(bucket, oldKey));
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		if (!bypassCreateDirs) {
			InputStream in = new ByteArrayInputStream(new byte[0]);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			use(s3 -> s3.putObject(bucket, keyForPath(path) + "/", in, metadata));
		}
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		deleteFile(path);
	}

	@Override
	public void close() throws IOException {
		threader.getOptional().ifPresent(Threader::close);
		use(AmazonS3::shutdown);
	}

	@Nonnull
	private String keyForPath(@Nonnull String path) {
		return path.startsWith("/") ? path.substring(1) : path;
	}

	@Nonnull
	private AmazonS3 s3() {
		limiter.acquire();
		return internalS3;
	}

	private void use(@Nonnull Consumer<AmazonS3> consumer) throws IOException {
		try {
			consumer.accept(s3());
		} catch (AmazonClientException e) {
			throw new IOException(e);
		}
	}

	@Nonnull
	private <T> T get(@Nonnull Function<AmazonS3, T> function) throws IOException {
		try {
			return function.apply(s3());
		} catch (AmazonClientException e) {
			throw new IOException(e);
		}
	}

	@Nonnull
	public static AmazonS3 buildS3(@Nonnull Config config, @Nonnull Access access) {
		checkNotNull(config);
		checkNotNull(access);

		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();

		ClientConfiguration cc = new ClientConfiguration();
		cc.setMaxErrorRetry(config.getInt("s3.maxErrorRetry", 3));
		cc.setConnectionTimeout((int) config.getTime("s3.connectionTimeout", "10s"));
		cc.setRequestTimeout((int) config.getTime("s3.requestTimeout", "0"));
		cc.setSocketTimeout((int) config.getTime("s3.socketTimeout", "50s"));
		cc.setClientExecutionTimeout((int) config.getTime("s3.clientExecutionTimeout", "0"));
		cc.setMaxConnections(config.getInt("s3.maxConnections", 50));
		builder.withClientConfiguration(cc);

		String region = config.getString("aws.region", null);
		String endpoint = config.getString("aws.endpoint", null);
		String bucket = getBucket(config.getString("url"));

		if (endpoint != null) {
			builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region));
		} else if (region != null) {
			builder.withRegion(region);
		}

		builder.withChunkedEncodingDisabled(getBooleanOrNull(config, "s3.chunkedEncodingDisabled"));
		builder.withPathStyleAccessEnabled(getBooleanOrNull(config, "s3.pathStyleAccessEnabled"));

		switch (config.getString("aws.credentials", "basic")) {
			case "basic":
				String key = config.getString("aws.key");
				AmazonS3Exception exception = null;
				int tries = config.getInt("s3.promptTries", 3);

				for (int i = 0; i < tries; i++) {
					Response response = access.prompt(key + ".secret", key + ".secret: ", Type.MASKED);
					try {
						String secret = response.value();
						builder.withCredentials(buildCredentials(config, new BasicAWSCredentials(key, secret)));

						AmazonS3 s3 = builder.build();
						if (!s3.doesBucketExistV2(bucket)) {
							throw new RuntimeException("bucket does not exist: " + bucket);
						}
						response.accept();
						return s3;
					} catch (AmazonS3Exception e) {
						exception = e;
						response.reject();
					}
				}

				if (exception != null) {
					throw exception;
				}
				break;

			case "instance":
				builder.withCredentials(InstanceProfileCredentialsProvider.getInstance());
				break;

			case "profile":
				String profile = config.getString("aws.profile");
				builder.withCredentials(new ProfileCredentialsProvider(profile));
				break;

			case "none":
				break;
		}

		return builder.build();
	}

	@Nonnull
	private static AWSCredentialsProvider buildCredentials(@Nonnull Config config, @Nonnull AWSCredentials basic) {
		AWSCredentialsProvider credentials = new AWSStaticCredentialsProvider(basic);

		String roleArn = config.getString("aws.roleArn", null);
		String roleSessionName = config.getString("aws.roleSessionName", null);
		String region = config.getString("aws.region", null);

		if (roleArn == null || roleSessionName == null) {
			return credentials;
		}

		AWSSecurityTokenService sts = AWSSecurityTokenServiceClientBuilder.standard()
				.withCredentials(credentials)
				.withRegion(region)
				.build();

		return new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, roleSessionName)
				.withStsClient(sts)
				.build();
	}

	@Nonnull
	public static String getBucket(@Nonnull String url) {
		return getBaseURI(url).getHost();
	}

	@Nonnull
	private static URI getBaseURI(@Nonnull String url) {
		int index = url.indexOf("://");
		if (index > -1) {
			index = url.indexOf("/", index + 3);
			if (index > -1) {
				url = url.substring(0, index);
			}
		}

		return URI.create(url);
	}

	@Nullable
	private static Boolean getBooleanOrNull(@Nonnull Config config, @Nonnull String key) {
		return config.containsKey(key) ? config.getBoolean(key) : null;
	}
}
