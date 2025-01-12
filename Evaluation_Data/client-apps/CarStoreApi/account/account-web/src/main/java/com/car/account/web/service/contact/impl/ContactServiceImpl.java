package com.car.account.web.service.contact.impl;

import com.car.account.client.request.contact.ContactReq;
import com.car.account.client.request.contact.QueryContactListReq;
import com.car.account.client.response.contact.ContactRes;
import com.car.account.client.response.store.QueryStoreListRes;
import com.car.account.client.response.store.sub.ImgStoreRes;
import com.car.account.web.common.utils.UuidUtils;
import com.car.account.web.mapper.contact.ContactMapper;
import com.car.account.web.model.contact.ContactMsg;
import com.car.account.web.model.store.StoreImages;
import com.car.account.web.service.contact.ContactService;
import com.car.common.enums.StsEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xielinjiang
 * @date 2021/1/28 0:00
 */
@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    @Resource
    ContactMapper contactMapper;


    /**
     * 新增留言
     * @param param
     * @return
     */
    @Override
    public ResultRes<String> insertMsg(ContactReq param) {
        ContactMsg contactMsg = new ContactMsg();
        BeanUtils.copyProperties(param,contactMsg);
        contactMsg.setUuid(UuidUtils.getUUID());
        contactMsg.setCreatedTime(new Date());
        contactMsg.setSts(StsEnum.ACTIVE.getValue());
        contactMapper.insert(contactMsg);
        return ResultRes.success(contactMsg.getUuid());
    }

    /**
     * 查询数据列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<ContactRes>> queryList(QueryContactListReq param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<ContactRes> contactResList = contactMapper.queryList(param);
        PageInfo<ContactRes> pageInfo = new PageInfo<>(contactResList);
        return PageRes.success(contactResList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }
}
