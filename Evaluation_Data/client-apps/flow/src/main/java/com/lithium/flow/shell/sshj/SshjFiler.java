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

package com.lithium.flow.shell.sshj;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

import com.lithium.flow.filer.Filer;
import com.lithium.flow.filer.Record;
import com.lithium.flow.filer.RecordPath;
import com.lithium.flow.io.DataIo;
import com.lithium.flow.util.Unchecked;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.schmizz.sshj.sftp.FileAttributes;
import net.schmizz.sshj.sftp.FileMode;
import net.schmizz.sshj.sftp.OpenMode;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;

/**
 * @author Matt Ayres
 */
public class SshjFiler implements Filer {
	private static final Set<OpenMode> READ_MODES = EnumSet.of(OpenMode.READ);
	private static final Set<OpenMode> WRITE_MODES = EnumSet.of(OpenMode.WRITE, OpenMode.CREAT, OpenMode.TRUNC);
	private static final Set<OpenMode> APPEND_MODES = EnumSet.of(OpenMode.WRITE, OpenMode.CREAT, OpenMode.APPEND);
	private static final Set<OpenMode> OPEN_WRITE_MODES = EnumSet.of(OpenMode.READ, OpenMode.WRITE, OpenMode.CREAT);

	private final URI uri;
	private final SFTPClient sftp;
	private final int buffer;

	public SshjFiler(@Nonnull Sshj ssh, @Nonnull URI uri) throws IOException {
		this.uri = checkNotNull(uri);
		sftp = checkNotNull(ssh).newSFTPClient();
		buffer = ssh.getFilerBuffer();
	}

	@Override
	@Nonnull
	public URI getUri() {
		return uri;
	}

	@Override
	@Nonnull
	public List<Record> listRecords(@Nonnull String path) throws IOException {
		return getRecord(path).exists()
				? sftp.ls(path).stream().map(this::getRecord).collect(toList())
				: Collections.emptyList();
	}

	@Nonnull
	private Record getRecord(@Nonnull RemoteResourceInfo info) {
		if (info.getAttributes().getType() == FileMode.Type.SYMLINK) {
			return Unchecked.get(() -> getRecord(info.getPath()));
		}

		long time = info.getAttributes().getMtime() * 1000;
		long size = info.getAttributes().getSize();
		return new Record(uri, RecordPath.from(info.getParent(), info.getName()), time, size, info.isDirectory());
	}

	@Override
	@Nonnull
	public Record getRecord(@Nonnull String path) {
		try {
			FileAttributes attributes = sftp.stat(path);
			return new Record(uri, RecordPath.from(path), attributes.getMtime() * 1000,
					attributes.getSize(), attributes.getMode().getType() == FileMode.Type.DIRECTORY);
		} catch (IOException e) {
			return Record.noFile(uri, path);
		}
	}

	@Override
	@Nonnull
	public InputStream readFile(@Nonnull String path) throws IOException {
		RemoteFile remoteFile = sftp.open(path, READ_MODES);
		return remoteFile.new ReadAheadRemoteFileInputStream(buffer);
	}

	@Override
	@Nonnull
	public OutputStream writeFile(@Nonnull String path) throws IOException {
		RemoteFile remoteFile = sftp.open(path, WRITE_MODES);
		return remoteFile.new RemoteFileOutputStream(0, buffer);
	}

	@Override
	@Nonnull
	public OutputStream appendFile(@Nonnull String path) throws IOException {
		RemoteFile remoteFile = sftp.open(path, APPEND_MODES);
		return remoteFile.new RemoteFileOutputStream(remoteFile.length(), buffer);
	}

	@Override
	@Nonnull
	public DataIo openFile(@Nonnull String path, boolean write) throws IOException {
		return new SshjDataIo(sftp.open(path, write ? OPEN_WRITE_MODES : READ_MODES));
	}

	@Override
	public void setFileTime(@Nonnull String path, long time) throws IOException {
		sftp.setattr(path, new FileAttributes.Builder().withAtimeMtime(time / 1000, time / 1000).build());
	}

	@Override
	public void deleteFile(@Nonnull String path) throws IOException {
		sftp.rm(path);
	}

	@Override
	public void renameFile(@Nonnull String oldPath, @Nonnull String newPath) throws IOException {
		sftp.rename(oldPath, newPath);
	}

	@Override
	public void createDirs(@Nonnull String path) throws IOException {
		synchronized (sftp) {
			sftp.mkdirs(path);
		}
	}

	@Override
	public void deleteDir(@Nonnull String path) throws IOException {
		sftp.rmdir(path);
	}

	@Override
	public void close() throws IOException {
		sftp.close();
	}
}
