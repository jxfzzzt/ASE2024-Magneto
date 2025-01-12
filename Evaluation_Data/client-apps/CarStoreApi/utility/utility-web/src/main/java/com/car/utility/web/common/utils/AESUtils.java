package com.car.utility.web.common.utils;

import com.car.utility.web.common.constants.WechatPayConstants;
import com.car.utility.web.common.utils.weixin.WXPayUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.Base64Utils;
import org.springside.modules.utils.security.CryptoUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 *
 */
public class AESUtils {

	private AESUtils() {

	}

	/**
	 * 密钥算法
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * 加解密算法/工作模式/填充方式
	 */
	private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

	/**
	 * 解密
	 *
	 * @param data
	 * @param signKey
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static String decryptData(String data, String signKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

		String keyMD5 = WXPayUtil.MD5(signKey);
		SecretKeySpec secretKey = new SecretKeySpec(keyMD5.toLowerCase().getBytes(WechatPayConstants.UTF8), ALGORITHM);
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(Base64Utils.decode(data.getBytes(WechatPayConstants.UTF8))));
	}

	public static String aesDecrypt(String data, String key) {
		return CryptoUtil.aesDecrypt(Base64.decode(data.getBytes()), key.getBytes());
	}

	public static String aesEncrypt(String data, String key) {
		return new String(Base64.encode(CryptoUtil.aesEncrypt(data.getBytes(), key.getBytes())));
	}


	public static void main(String[] args) {
		//System.out.println("解密："+AESUtils.aesDecrypt("GD8+5ZFFhlE01MGW7SUdKQi+aF+88X0KlTWAAu1asUCeVeMBVLmpKqWiz/kt9/isVEmmmRfPOFcrSltBdFI1Obmb4uTqDn8OR/QoQh5Ui1H1OYe1YqmcPY3I5LXXytQdNwgWb1flv6Ds4OzI4hZGzGtlKUdQ4KtJDk3NTyA4ZrpB5SU/EK20d/+TmO0EZGZVI9pu+sDDXFdzi6EJE/EHuZ0SU4fZnrsM38QWHrFVLbHr83FtKML6c4Wott6wOR8z9qsS8XLwRau5kZZmZw2mJu3m7MGClJzCrBe2lq3VykLxaSZBOvifxmmYVfmHf+b127ZSAj6kQ3Ix1RyS67U4/wMWJwIOjbCkKMs3yGUs9sL6Uqh+t/VaTvPF8WN/qgZn2H5C9JOX7VBaG5y7RM/wCLvQFxuTfh1CrA3xbXFdvmG5yBAtCqv42rG1ONRqRUQmi6eGAfnWbOkLEmQLYbohs/R1P0vMy0QHPlgdDYzFSWOhMlaAcu5OsWxT93fu5aflx5noP66dolIABzUoqNqWzXxIvxdS0g02v7ovnWh8wRQxQryEN8czoC9B0Tchs/YRN1aob+IENVf1xOLZ0dD5nRHSSS6xMjT/4C1Iw6OPD9CXJaFs0dlsqx5TRlJUt1DxsSvWG+va77cngm9vdaDODYTLPzJd/iEzH6UWIyilUrdJ2G4uIDcvWuMjjZsGdBRLfDMDvEoakp/O2Neap1q3XikKftXdte9NK+JSwSpZaTdp6pcM7lU8XwKJqUvNrNOK5Mk4mqZ3spcdeqky866q+ToClR0Rah/g+ZAglPZpblyoTmJhX9XzyY52uyEPVddwvS44eBS+EdGut87Ogk+8YhXxCUOZN5nxJ6k39a4EenugTkRNUOFHBnJhViU1HU0AuvVOatbu/1w1Y+/M+XmrZRaEhY34RMcuDaujODnwJ8UDwZ5rFRgLzRJI9uClptkAtmyR/vUgFIUPdM5QIAZ0VfV5kZ30jnPocgbRHSw0ORB+2ZkmRBo0K1Qsxry/loFapItomPWU81RCBklu2OccvJWW4DTpdbASRHZ7dg6LORGEtcLwD3dXfhQt+eX9kA/UoOoj4v8nLZPgZCEA3QaZTJgBAP1mrWm7kzbBhZNBi9vLEiULLWk5LOqK5PdMnE4DtWF2c+AEFzwMpkUU8n436bXDXj7eMSRFMNwWBvXbGwEpjxZIAzXfYG16eWpsWTI798zB9pwuj6NKjoqwfak9Vr2/TGPWiR8zQ1eD33kYKpp/ipeGiPsLaKZboxAbrhaHLENVcxrog8wMYkRFYrgZI5Abs5apEv6bTXyAopmD9OGSi4PDkbg+axJ0BtSeTM4ySN34W5tiCCBTlTJpe64zD8Yr8TAFW8vRAIS6YZK6QJliy1T69sNGDEkcaaaTcKDYedc5O5YD9NcBhK695IkoXvCc5yMDj48cZAI09bui5l8nH+cf0aY5tLyHvzKsVRXkUBbTDhdvIhKAsGC0jZtcmjkA7hfIjj8B2XGKbd6Z2ELZ9jDtLx3gkntT/sDXH4PjgJlA7GSkKwPML5BDh5+WN2ZN2+lGxwwLumID3AjMXHX0VIyYVXBl3ko9Jz4xfXm9boXao00NQrUROcqV0hvnRfZwDaZHIkSYglkjtY9Y7n8wwnb1dqXAx5Hug1VmdrpQvQNuUzOrmwuomWiG5RpiHcVLym7T76ccRfpnxEKZnuIfZZVuILGlFpqsqaSrH9heCg7qW1h5z58VDYbmD1H78HW0WO2UizPl1vvT2MCHr9kMn/+IEK4+KIhgAU8c9EtPP1W34aTc7x2eDOZ0SBCRAj9HZwC3w/rCznu98AwsN94=","61QEg436u1qjigJF"));

		System.out.println("加密："+AESUtils.aesEncrypt("1606428273","61QEg436u1qjigJF"));
	}
}
