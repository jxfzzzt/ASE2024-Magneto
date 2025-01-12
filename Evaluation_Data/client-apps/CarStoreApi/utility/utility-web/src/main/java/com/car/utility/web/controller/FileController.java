package com.car.utility.web.controller;

import com.car.common.res.ResultRes;
import com.car.utility.client.request.file.UploadImgReq;
import com.car.utility.web.common.constants.ConfigConstants;
import com.car.utility.web.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务器
 * @author xlj
 */
@RestController
@RequestMapping("/file")
@Slf4j
@Api(value = "FileController", tags = "文件上传管理")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    ConfigConstants configConstants;

    /**
     * 文件上传
     * @param file
     * @param type
     * @return
     */
    @PostMapping(value = "uploadFile")
    public ResultRes<String> uploadFile(@RequestParam("file") MultipartFile file,
                                       @ApiParam(name = "type", value = " type:person人员 other 默认", required = true) @RequestParam("type") String type) {
        return fileService.uploadImageFile(file,type);
    }


    /**
     * 字符串图片上传
     * @return
     */
    @PostMapping(value = "uploadBase64Image")
    public ResultRes<String> uploadBase64Image(@RequestBody UploadImgReq req) {
        return fileService.uploadPicBase64Img(req.getBase64Img(), req.getType());
    }
}

