package com.car.utility.web.service;

import com.car.common.res.ResultRes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author xlj
 */
public interface FileService {

    /**
     * 上传图片
     * @param file
     * @param fileType
     * @return
     */
    ResultRes<String> uploadImageFile(MultipartFile file, String fileType);

    /**
     * 删除图片base64
     * @param imgBase64
     * @param fileType
     * @return
     */
    ResultRes<String> uploadPicBase64Img(String imgBase64, String fileType);

}
