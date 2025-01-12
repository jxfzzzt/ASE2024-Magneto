package com.car.account.web.service.store.impl;

import com.car.account.client.request.store.AddStoreTechnicianRelateReq;
import com.car.account.client.request.store.CheckStoreTechnicianRelateReq;
import com.car.account.client.request.store.QueryAreaStoreListReq;
import com.car.account.client.request.store.QueryStoreTechnicianRelateListReq;
import com.car.account.client.response.store.QueryAreaStoreListRes;
import com.car.account.client.response.store.StoreRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateDetailRes;
import com.car.account.client.response.store.StoreTechnicianRelateListRes;
import com.car.account.web.mapper.store.StoreMapper;
import com.car.account.web.mapper.store.StoreTechnicianRelateMapper;
import com.car.account.web.mapper.store.StoreUserMapper;
import com.car.account.web.mapper.technician.TechnicianMapper;
import com.car.account.web.model.store.Store;
import com.car.account.web.model.store.StoreTechnicianRelate;
import com.car.account.web.model.store.StoreUser;
import com.car.account.web.model.technician.Technician;
import com.car.account.web.service.store.StoreTechnicianRelateService;
import com.car.common.enums.CheckStatusEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Slf4j
@Service
public class StoreTechnicianRelateServiceImpl implements StoreTechnicianRelateService {


    @Autowired
    StoreMapper storeMapper;
    @Autowired
    StoreUserMapper storeUserMapper;
    @Autowired
    StoreTechnicianRelateMapper storeTechnicianRelateMapper;
    @Autowired
    TechnicianMapper technicianMapper;

    /**
     * 技师申请关联店铺
     * @param addStoreTechnicianRelateReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<Boolean> addTechnicianToStore(AddStoreTechnicianRelateReq addStoreTechnicianRelateReq) {
        String storeUuid = addStoreTechnicianRelateReq.getStoreUuid();
        checkStoreAlreadyExist(storeUuid);

        checkTechnicianAlreadyExist(addStoreTechnicianRelateReq.getTechnicianUuid());

        //查询技师是否已经绑定店铺
        StoreTechnicianRelate storeTechnicianRelate = new StoreTechnicianRelate();
        storeTechnicianRelate.setTechnicianUuid(addStoreTechnicianRelateReq.getTechnicianUuid());
        storeTechnicianRelate.setSts(StsEnum.ACTIVE.getValue());
        storeTechnicianRelate = storeTechnicianRelateMapper.selectOne(storeTechnicianRelate);

        if (StringUtils.isEmpty(storeTechnicianRelate)) {
            //新增技师申请关联店铺
            addStoreTechnicianRelate(storeUuid,addStoreTechnicianRelateReq.getTechnicianUuid());

        } else if (!storeUuid.equals(storeTechnicianRelate.getStoreUuid())) {
            //如果技师已经关联店铺并且与新申请绑定店铺不一致时,删除之前绑定的店铺,重新申请绑定
            storeTechnicianRelate.setSts(StsEnum.INVALID.getValue());
            storeTechnicianRelate.setCheckSts(CheckStatusEnum.CHECK_REJECTED.getValue());
            storeTechnicianRelate.setLastUpdatedBy(TokenHelper.getUserName());
            storeTechnicianRelate.setLastUpdatedTime(new Date());
            storeTechnicianRelateMapper.updateByPrimaryKeySelective(storeTechnicianRelate);
            //新增技师申请关联店铺
            addStoreTechnicianRelate(storeUuid,addStoreTechnicianRelateReq.getTechnicianUuid());
        }

        return ResultRes.success(true);
    }

    /**
     * 店铺审核技师关联申请
     * @param checkStoreTechnicianRelateReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<Boolean> checkStoreTechnicianRelate(CheckStoreTechnicianRelateReq checkStoreTechnicianRelateReq) {
        StoreTechnicianRelate storeTechnicianRelate = checkStoreTechnicianRelateAlreadyExist(checkStoreTechnicianRelateReq.getUuid());
        String desc = CheckStatusEnum.enumOfDesc(checkStoreTechnicianRelateReq.getCheckSts());
        if (StringUtils.isEmpty(desc)) {
            log.error("技师关联店铺  无效的审核状态入参：checkSts{}",checkStoreTechnicianRelateReq.getCheckSts());
            throw new BusinessException(ResEnum.INVALID_CHECK_STS);
        }

        storeTechnicianRelate.setCheckSts(checkStoreTechnicianRelateReq.getCheckSts());
        storeTechnicianRelate.setLastUpdatedBy(TokenHelper.getUserName());
        storeTechnicianRelate.setLastUpdatedTime(new Date());
        storeTechnicianRelateMapper.updateByPrimaryKey(storeTechnicianRelate);

        return ResultRes.success(true);
    }

    /**
     * 店铺审核技师关联申请
     * @param uuid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<Boolean> storeUnbindTechnician(String uuid) {
        StoreTechnicianRelate storeTechnicianRelate = checkStoreTechnicianRelateAlreadyExist(uuid);
        storeTechnicianRelate.setSts(StsEnum.INVALID.getValue());
        storeTechnicianRelate.setLastUpdatedBy(TokenHelper.getUserName());
        storeTechnicianRelate.setLastUpdatedTime(new Date());
        storeTechnicianRelateMapper.updateByPrimaryKey(storeTechnicianRelate);
        return ResultRes.success(true);
    }

    /**
     * 查询技师关联店铺详情
     * @return
     */
    @Override
    public ResultRes<StoreTechnicianRelateDetailRes> queryStoreTechnicianRelateDetail() {
        String technicianUuid = TokenHelper.getUserUuid();
        StoreTechnicianRelateDetailRes res = new StoreTechnicianRelateDetailRes();

        StoreRelateDetailRes storeRelateDetailRes = storeTechnicianRelateMapper.queryStoreTechnicianRelateDetailRes(technicianUuid);
        res.setTechnicianUuid(technicianUuid);
        res.setStoreRelateDetailRes(storeRelateDetailRes);
        return ResultRes.success(res);
    }

    /**
     * 查询店铺技师关联列表
     * @return
     */
    @Override
    public PageRes<List<StoreTechnicianRelateListRes>> queryStoreTechnicianRelateList(QueryStoreTechnicianRelateListReq param) {
        log.debug("查询店铺技师关联列表");
        String userUuid = TokenHelper.getUserUuid();
        StoreUser storeUser = storeUserMapper.selectByPrimaryKey(userUuid);
        if(StringUtils.isEmpty(storeUser)){

            log.error("登录人员未定位到店铺人员信息");
            throw new BusinessException(ResEnum.STORE_CONTACT_NOT_EXIST);
        }

        String storeUuid = storeUser.getStoreUuid();
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<StoreTechnicianRelateListRes> storeTechnicianRelateListRes = storeTechnicianRelateMapper.queryStoreTechnicianRelateList(storeUuid);
        PageInfo<StoreTechnicianRelateListRes> pageInfo = new PageInfo<>(storeTechnicianRelateListRes);
        return PageRes.success(storeTechnicianRelateListRes, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 根据区域查询店铺列表
     * * @param queryAreaStoreListReq
     * @return
     */
    @Override
    public PageRes<List<QueryAreaStoreListRes>> queryStoreListByArea(QueryAreaStoreListReq param) {
        log.debug("查询店铺技师关联列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<QueryAreaStoreListRes> queryAreaStoreListRes = storeTechnicianRelateMapper.queryStoreListByArea(param.getAreaUuid());
        PageInfo<QueryAreaStoreListRes> pageInfo = new PageInfo<>(queryAreaStoreListRes);
        return PageRes.success(queryAreaStoreListRes, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }


    /**
     * 验证店铺与技师关联关系是否存在
     * @param uuid
     */
    private StoreTechnicianRelate checkStoreTechnicianRelateAlreadyExist(String uuid) {
        StoreTechnicianRelate query = new StoreTechnicianRelate();
        query.setUuid(uuid);
        query = storeTechnicianRelateMapper.selectByPrimaryKey(query);
        if (StringUtils.isEmpty(query) || StsEnum.INVALID.getValue().equals(query.getSts())) {
            log.error("技师关联店铺  店铺与技师关联关系未匹配到对应数据：uuid{}",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        return query;
    }

    /**
     * 验证店铺是否存在
     * @param uuid
     */
    private Store checkStoreAlreadyExist(String uuid) {
        Store queryStore = new Store();
        queryStore.setUuid(uuid);
        queryStore = storeMapper.selectByPrimaryKey(queryStore);
        if (StringUtils.isEmpty(queryStore ) || StsEnum.INVALID.getValue().equals(queryStore.getSts())) {
            log.error("技师关联店铺  店铺未匹配到对应数据：uuid{}",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        return queryStore;
    }

    /**
     * 验证技师是否存在
     * @param technicianUuid
     */
    private Technician checkTechnicianAlreadyExist(String technicianUuid) {
        Technician queryTechnician = new Technician();
        queryTechnician.setUuid(technicianUuid);
        queryTechnician = technicianMapper.selectByPrimaryKey(queryTechnician);
        if (StringUtils.isEmpty(queryTechnician) || StsEnum.INVALID.getValue().equals(queryTechnician.getSts())) {
            log.error("技师关联店铺  技师未匹配到对应数据：uuid{}",technicianUuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        return queryTechnician;
    }

    /**
     * 技师申请关联店铺
     * @param storeUuid
     * @param technicianUuid
     */
    private void addStoreTechnicianRelate(String storeUuid, String technicianUuid) {
        StoreTechnicianRelate storeTechnicianRelate = new StoreTechnicianRelate();
        storeTechnicianRelate.setCreatedBy(TokenHelper.getUserName());
        storeTechnicianRelate.setCreatedTime(new Date());
        storeTechnicianRelate.setSts(StsEnum.ACTIVE.getValue());
        storeTechnicianRelate.setUuid(UuidUtils.getUuid());
        storeTechnicianRelate.setStoreUuid(storeUuid);
        storeTechnicianRelate.setCheckSts(CheckStatusEnum.CHECK_PENDING.getValue());
        storeTechnicianRelate.setTechnicianUuid(technicianUuid);
        storeTechnicianRelateMapper.insert(storeTechnicianRelate);
    }

}
