package com.car.utility.web.service.impl;

import com.car.common.enums.ResEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.utility.client.enums.FileChannelEnum;
import com.car.utility.client.enums.FileTypeEnum;
import com.car.utility.web.common.constants.ConfigConstants;
import com.car.utility.web.service.AliOssRemoteService;
import com.car.utility.web.service.FileService;
import com.obs.services.ObsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author xlj
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private static final List<String> FILE_SUFFIX = Arrays.asList("pdf","jpg","png","jpeg","xls","xlsx","avi","rmvb","rm","asf","divx","mpg","mpeg","mpe","wmv","mp4","mkv","vob","ppt","pptx","mov");

    @Autowired
    private AliOssRemoteService aliOssService;

    @Autowired
    private ConfigConstants configConstants;

    /**
     * 验证文件类型
     * @param fileName
     */
    private void validFileName(String fileName){
        List<String> collect = FILE_SUFFIX.stream().filter(s -> (fileName.toLowerCase().endsWith(s))).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)){
            log.error("非法文件>>>fileName:{}",fileName);
            throw new BusinessException(ResEnum.FILE_TYPE_ERROR);
        }
    }

    /**
     * 上传图片
     * @param file
     * @param fileType
     * @return
     */
    @Override
    public ResultRes<String> uploadImageFile(MultipartFile file, String fileType) {
        //需要将图片限制为10M
        if (file.getSize() > configConstants.getUploadFileSizeLimit()) {
            throw new BusinessException(ResEnum.IMAGE_UPLOAD_LIMIT);
        }
        //验证文件是否在指定区间
        validFileName(file.getOriginalFilename());
        //验证文件类型是否正确
        if(StringUtils.isEmpty(FileTypeEnum.enumOfDesc(fileType)) || file == null){
            throw new BusinessException(ResEnum.FILE_PARAM_ERROR);
        }
        //预约人像照片进行人脸检测逻辑
        if(FileTypeEnum.PERSON.getValue().equals(fileType)){
            //TODO 预约人像照片进行人脸检测逻辑
        }
        //调用第三方组件进行文件上传，支持本地存储、阿里云oss存储、华为云obs存储
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String[] prefixStr = prefix.split("/");
        prefix = prefixStr[prefixStr.length - 1];
        String fileName = UuidUtils.getUuid() + Constants.SUFFIX_SPLITOR + prefix;
        String filePath = fileType  + "/" + DateUtil.dateToStr(new Date(),DateUtil.YYYY_MM_DD) +"/" + fileName;
        String imageUrl = null;
        try {
            imageUrl = uploadFileToService(filePath,file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultRes.success(imageUrl);
    }

    /**
     * 上传文件到存储服务端
     * @param filePath
     * @param is
     * @return
     */
    public String uploadFileToService(String filePath,InputStream is){
        String fileUrl = null;
        try{
            if(FileChannelEnum.obs.getValue().equals(configConstants.getFileChannel())){
                //华为云
                ObsClient obsClient = new ObsClient(configConstants.getObs().getAccessKey(), configConstants.getObs().getSecretKey(), configConstants.getObs().getEndPoint());
                obsClient.putObject(configConstants.getObs().getBucketName(), filePath, is);
                fileUrl = configConstants.getFileUrlPrefix() + filePath;
            }else if(FileChannelEnum.oss.getValue().equals(configConstants.getFileChannel())){
                //阿里云
                boolean uploadFlag = true;
                //将资源上传到阿里云
                uploadFlag = aliOssService.put(filePath, is, configConstants.getOss().getEndPoint(),
                        configConstants.getOss().getAccessKey(), configConstants.getOss().getSecretKey(),configConstants.getOss().getBucketName());
                if(!uploadFlag){
                    throw new BusinessException(ResEnum.API_ERROR);
                }
                fileUrl = configConstants.getFileUrlPrefix() + filePath;
            }else {
                // 上传图片  本地存储流程
                String path = configConstants.getLocal().getFilePathPrefix() + filePath;
                try {
                    FileUploadUtil.upload(path, is);
                } catch (Exception e) {
                    log.error("本地上传文件错误，错误原因：{}",ExceptionUtils.stackTraceToString(e));
                    throw new BusinessException(ResEnum.IMAGE_PRESERVE_ERROR);
                }
                fileUrl = configConstants.getFileUrlPrefix() + filePath;
            }
        }catch (Exception e){
            log.error(ExceptionUtils.stackTraceToString(e));
            ResultRes.error(ResEnum.API_ERROR.getValue(), ExceptionUtils.stackTraceToString(e));
        }
        return fileUrl;
    }

    /**
     * 图片Base64上传
     * @param imgBase64
     * @param fileType
     * @return
     */
    @Override
    public ResultRes<String> uploadPicBase64Img(String imgBase64, String fileType) {
        //验证文件类型是否正确
        if(StringUtils.isEmpty(FileTypeEnum.enumOfDesc(fileType)) || StringUtils.isEmpty(imgBase64)){
            throw new BusinessException(ResEnum.FILE_PARAM_ERROR);
        }
        //预约人像照片进行人脸检测逻辑
        if(FileTypeEnum.PERSON.getValue().equals(fileType)){
            //TODO 预约人像照片进行人脸检测逻辑
        }
        //调用阿里云OSS文件上传
        String fileName = UuidUtils.getUuid() + Constants.SUFFIX_SPLITOR + "jpg";
        String filePath = fileType+ "/" + DateUtil.dateToStr(new Date(),DateUtil.YYYY_MM_DD) +"/" + fileName;
        String imageUrl = null;
        try{
            InputStream is = base64ToInputStream(imgBase64);
            imageUrl = uploadFileToService(filePath,is);
        }catch (Exception e){
            log.error(ExceptionUtils.stackTraceToString(e));
            ResultRes.error(ResEnum.API_ERROR.getValue(), ExceptionUtils.stackTraceToString(e));
        }
        return ResultRes.success(imageUrl);
    }

    public  InputStream base64ToInputStream(String base64string){
        ByteArrayInputStream stream = null;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes1 = decoder.decode(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            log.error("图片base64解密失败>>>ex:{}",e);
        }
        return stream;
    }
}
