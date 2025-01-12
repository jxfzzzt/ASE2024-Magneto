package com.car.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author xlj
 */
public class FileUploadUtil {


	public static void upload(String path, InputStream is) {
		//截取文件名
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        //截取文件路径
        String remoteFilePath = path.substring(0, path.lastIndexOf("/"));
        createdir(path);
        try {
			File toRealFile = new File(remoteFilePath,fileName);
	        OutputStream os = new FileOutputStream(toRealFile);
			byte[] buffer = new byte[1024];
	        int length = 0;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        is.close();
	        os.close();
        } catch (Exception e) {
			throw new RuntimeException("附件保存失败");
		}
	}

	/**
	 * 创建文件夹并返回文件名称
	 * @param remoteFilePath
	 * @return
	 */
	private static String createdir(String remoteFilePath) {
		//统一传入的文件格式
        if (!remoteFilePath.startsWith("/")) {
            remoteFilePath = "/" + remoteFilePath;
        }
        //截取文件名
        String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
        //截取文件路径
        String path = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/"));
        //进入文件路径,如果不存在执行创建
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
        	file.mkdirs();
        }
        return fileName;
	}

	/**
	 * 获取随机文件名
	 *
	 * @return 文件名
	 */
	public static String getRandomFileName() {
		return UUID.randomUUID().toString();
	}

	/**
     * 获取文件扩展名
     *
     * @return string
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
