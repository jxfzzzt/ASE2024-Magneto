package com.car.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ImageUtil {

    /**
     * 图片后缀
     */
    public static final String IMAGE_SUFFIX = ".jpg";

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    /**
     *
     *  上传BASE64
     *
     * @param base64
     * @return
     */
    public byte[] uploadImage(String base64) {
        byte[] result = base64ToMultipart(base64);
        return result;
    }

    /**
     * 图片流转换为base64
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String multipartFileToBaseStr(MultipartFile file) throws IOException {
        return Base64.encodeBase64String(file.getBytes());
    }


    /**
     * 将文件转成base64 字符串
     * @param file
     * @return  *
     * @throws Exception
     */
    public static String encodeBase64File(File file) {
        String base64 = "";
        //byte[] btImg = getImageFromNetByUrl(path);
        byte[] btImg = fileToByte(file);
        base64 = java.util.Base64.getEncoder().encodeToString(btImg);
        return base64;
    }

    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     *
     *  BASE64转base64ToMultipart
     *
     * @param base64
     * @return
     */
    public byte[] base64ToMultipart(String base64) {

            String[] baseStrs = base64.split(",");

            byte[] b = java.util.Base64.getEncoder().encode(baseStrs[0].getBytes());
            return b;

    }


    public static byte[] fileToByte(File file)
    {
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }



    /**
     *
     * byte转base64
     *
     * @param b
     * @return
     */
    public static String byte2Base64StringFun(byte[] b){
        return Base64.encodeBase64String(b);
    }

    /**
     * 生成图片
     * @param imgData
     *  图片数据（base64）
     * @param imagePath
     *  图片路径
     * @param imageName
     *  图片名称
     * @return
     */
    public static boolean base64ImageToFile(String imgData,String imagePath, String imageName){
        if (StringUtils.isEmpty(imgData)){
            // 图像数据为空
            return false;
        }
        if(StringUtils.isEmpty(imagePath)){
            // 保存路径为空
            return false;
        }
        if(StringUtils.isEmpty(imageName)){
            // 图片名称为空
            return false;
        }
        imgData = StringUtil.replaceBlank(imgData);

        File file = new File(imagePath);

        if(!file.getParentFile().exists()){
            file.getParentFile().mkdir();
        }

        if(!file.exists()){
            file.mkdir();
        }
        String imgFilePath = imagePath + imageName;
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            // Base64解码
            byte[] b = java.util.Base64.getDecoder().decode(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    // 调整异常数据
                    b[i] += 256;
                }
            }
            try {
                out.write(b);
                return true;
            } catch (IOException e) {
                log.error(ExceptionUtils.stackTraceToString(e));
            }
        } catch (FileNotFoundException e) {
            log.error(ExceptionUtils.stackTraceToString(e));
        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error(ExceptionUtils.stackTraceToString(e));
            }
        }
        return false;
    }

}
