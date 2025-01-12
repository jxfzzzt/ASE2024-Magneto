package com.car.account.web.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件工具类
 */
public class FileUtils {

    private final static String FILE_UNIT_B = "B";

    private final static String FILE_UNIT_K = "K";

    private final static String FILE_UNIT_M = "M";

    private final static String FILE_UNIT_G = "G";


    /**
     * 判断文件大小
     *
     * @param file 文件
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(File file, int size, String unit) {
        long len = file.length();
        double fileSize = getFileSize(len, unit);
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = getFileSize(len, unit);
        if (fileSize > size) {
            return false;
        }
        return true;
    }

    public static double getFileSize(Long len, String unit) {
        double fileSize = 0;
        if (FILE_UNIT_B.equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if (FILE_UNIT_K.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if (FILE_UNIT_M.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if (FILE_UNIT_G.equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        return fileSize;
    }

    /**
     * @return void
     * @author LX
     * @description 删除目录下的所有文件
     * @date 2020/3/21
     **/
    public static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    public static void unzip(File dir, String tempDir) {
        if (dir == null) {
            return;
        }
        //先解压
        if (dir.getName().indexOf(".zip") >= 0) {
            unZip(dir.getPath(), tempDir + File.separator + getFileName(dir));
        }
    }

    public static String getFileName(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static void unZip(String path, String savePath) {
        int buffer = 2048;
        int count = -1;
        System.out.println("解压文件路径:" + savePath);
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipFile zipFile = null;
        try {
            //创建保存目录
            new File(savePath).mkdir();
            //解决中文乱码问题
            zipFile = new ZipFile(path, Charset.forName("gbk"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                byte buf[] = new byte[buffer];
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                boolean ismkDir = false;
                //检查此文件是否带有文件夹
                if (filename.lastIndexOf("/") != -1) {
                    ismkDir = true;
                }
                filename = savePath + File.separator + filename;
                //如果是文件夹先创建
                if (entry.isDirectory()) {
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                //如果是目录先创建
                if (!file.exists()) {
                    if (ismkDir) {
                        //目录先创建
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs();
                    }
                }
                //创建文件
                file.createNewFile();
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, buffer);
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();
                is.close();
            }
            zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除解压的文件夹，保存压缩包文件用以后续追查
     * @param directory
     */
    public static void deleteZipUnFile(File directory) {
        if (!directory.isDirectory()) {
            directory.delete();
        } else {
            File[] files = directory.listFiles();
            // 空文件夹
            if (files.length <= 0) {
                directory.delete();
                return;
            }
            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteZipUnFile(file);
                } else {
                    file.delete();
                }
            }
            // 删除文件夹本身
            directory.delete();
        }
    }


}
