package com.car.account.web.common.utils;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZipUtil {

    private static final int BUFFER = 2048;

    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

    @Value("${file.staticAccessPathForUserHead}")
    private String staticAccessPathForUserHead;
    @Value("${file.uploadFolderForUserHead}")
    private String uploadFolderForUserHead;
    @Value("${file.staticAccessPathForZip}")
    private String staticAccessPathForZip;

    /**
     * 判断是否是中文
     */
    public static boolean existZH(String ustr) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(ustr);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 递归判断文件名称路径
     *
     * @param file
     * @return
     */
    public static List<String> getDirFiles(File file) {
        List<String> resultList = new ArrayList<String>();
        //判断是否是一个文件夹
        if (file.isDirectory()) {
            resultList.add(file.toString());
            for (File inner : file.listFiles()) {
                String fileName = inner.getName();
                //检查此文件是否带有此文件夹,跳过解析此文件路径
                if ("MACOSX".toUpperCase().contains(fileName)) {
                    continue;
                }
                //递归部分
                resultList.addAll(getDirFiles(inner));
            }
            //不是一个文件夹，添加路径
        } else {
            //停止部分
            resultList.add(file.toString());
        }
        return resultList;
    }


    private static void copyFileRar(String srcPathStr, String desPathStr) {
        //1.获取源文件的名称
        String newFileName = srcPathStr.substring(srcPathStr.lastIndexOf("/") + 1);
        desPathStr = desPathStr + File.separator + newFileName;
        try {
            //2.创建输入输出流对象
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);

            //创建搬运工具
            byte datas[] = new byte[1024 * 8];
            //创建长度
            int len = 0;
            //循环读取数据
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
            }
            //3.释放资源
            fis.close();
            fos.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 解压Zip文件与Rar文件
     *
     * @param sourceFiles 需要解压的文件，解压的目标文件夹
     * @param osName 上传文件操作系统
     */
    public static String unZip(String sourceFiles, String osName) {
        int count = -1;
        String savePath = "";
        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        //保存解压文件目录
        savePath = sourceFiles.substring(0, sourceFiles.lastIndexOf("."));
        //创建保存目录
        if (!new File(savePath).exists()) {
            new File(savePath).mkdir();
        }
        ZipFile zipFile = null;
        try {
            //解决中文乱码问题
            if (osName.toUpperCase().contains("WINDOWS")) {
                zipFile = new ZipFile(sourceFiles, "gbk");
            } else {
                zipFile = new ZipFile(sourceFiles, "UTF8");
            }
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                byte[] buf = new byte[BUFFER];
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String fileName = entry.getName();
                boolean ismkdir = false;
                //检查此文件是否带有文件夹
                if (!osName.toUpperCase().contains("WINDOWS")) {
                    if (fileName.toUpperCase().contains("_MACOSX")) {
                        continue;
                    }
                }
                if (fileName.lastIndexOf("/") != -1) {
                    fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
                    ismkdir = false;
                }
                fileName = savePath + "/" + fileName;
                //如果是文件夹先创建
                if (entry.isDirectory()) {
                    continue;
                }
                file = new File(fileName);
                //如果是目录先创建
                if (!file.exists()) {
                    if (ismkdir) {
                        //目录先创建
                        new File(fileName.substring(0, fileName.lastIndexOf("/"))).mkdirs();
                    }
                }
                //创建文件
                file.createNewFile();
                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, BUFFER);
                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
            }
        } catch (IOException ioe) {
            log.error("解析文件流错误，错误原因：{}", ioe.getMessage());
            //解析文件流错误，返回空路径
            return "";
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
                log.error("关闭文件流错误，错误原因：{}", e.getMessage());
                //解析关闭文件流错误，返回空路径
                return "";
            }
        }
        return savePath;
    }

    /**
     * 移动文件处理
     * @param url
     * @param new_name
     * @return
     * @throws Exception
     */
    public static boolean renameFile(String url, String new_name) throws Exception {
        String old_url = url;
        old_url = old_url.replace("/", "/");
        File old_file = new File(old_url);
        if (!old_file.exists()) {
            throw new IOException("文件重命名失败，文件（"+old_file+"）不存在");
        }
        System.out.println(old_file.exists());

        String old_name = old_file.getName();
        // 获得父路径
        String parent = old_file.getParent();
        // 重命名
        String new_url = parent + "/" + new_name;
        File new_file = new File(new_url);
        old_file.renameTo(new_file);

        System.out.println("原文件：" + old_file.getName());
        System.out.println("新文件：" + new_file.getName());
        new_name = new_file.getName();
        old_name = old_file.getName();
        if (new_name.equals(old_name)) {
            return false;
        } else {
            //copyFile(url,parent+"\\"+new_name);
            //deleteFile(url);
            return true;
        }

    }



    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            //文件存在时  
            if (oldfile.exists()) {
                //读入原文件  
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    //字节数 文件大小
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }






    /**
     * 加密压缩
     * @param sourceFile 需要压缩的文件
     * @param zip 压缩包
     * @throws ZipException 压缩异常
     */
    public static void compressFile(File sourceFile, File zip) throws ZipException {
        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(zip);
        ZipParameters zipParameters = new ZipParameters();
        //压缩方式
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        //压缩级别
        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);

        if(sourceFile.isDirectory()){
            File[] files = sourceFile.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isDirectory()){
                        zipFile.addFolder(file, zipParameters);

                    }else{
                        zipFile.addFile(file, zipParameters);
                    }
                }
            }
        }else{
            zipFile.addFile(sourceFile, zipParameters);
        }
    }

}

