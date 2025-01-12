package com.car.account.web.controller.contact;

import com.car.account.client.request.contact.ContactReq;
import com.car.account.client.request.contact.QueryContactListReq;
import com.car.account.client.request.goods.*;
import com.car.account.client.response.contact.ContactRes;
import com.car.account.client.response.goods.CalGoodsRes;
import com.car.account.client.response.goods.GoodsRes;
import com.car.account.web.mapper.store.StoreUserMapper;
import com.car.account.web.model.store.StoreUser;
import com.car.account.web.service.contact.ContactService;
import com.car.account.web.service.goods.GoodsService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xielinjiang
 * @date 2020/12/22
 */
@Slf4j
@Api(value = "ContactController", tags = "联系留言")
@RequestMapping(value = "/contact", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ContactController {

    @Autowired
    ContactService contactService;

    @PostMapping(value = "/insert")
    @ApiOperation(value = "新增留言", nickname = "insert")
    public ResultRes<String> insertMsg(@RequestBody @Valid ContactReq param){
        return contactService.insertMsg(param);
    }

    @PostMapping(value = "/queryList")
    @ApiOperation(value = "查询列表", nickname = "queryList")
    public PageRes<List<ContactRes>> queryList(@RequestBody @Valid QueryContactListReq param){
        return contactService.queryList(param);
    }
}
