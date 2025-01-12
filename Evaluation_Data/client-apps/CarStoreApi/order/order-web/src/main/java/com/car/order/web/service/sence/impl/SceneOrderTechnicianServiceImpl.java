package com.car.order.web.service.sence.impl;

import com.alibaba.fastjson.JSON;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.common.utils.token.LoginToken;
import com.car.order.client.request.scene.AddSceneTechnicianInfoReq;
import com.car.order.web.mapper.scene.SceneOrderTechnicianMapper;
import com.car.order.web.model.scene.SceneOrderTechnician;
import com.car.order.web.service.sence.SceneOrderService;
import com.car.order.web.service.sence.SceneOrderTechnicianService;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * @author cjw
 */
@Slf4j
@Service
public class SceneOrderTechnicianServiceImpl implements SceneOrderTechnicianService {

    @Autowired
    private SceneOrderTechnicianMapper sceneOrderTechnicianMapper;

    @Autowired
    private SceneOrderService sceneOrderService;

    /**
     * 新增现场订单技师内容
     * @param req
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addSceneTechnicianInfo(AddSceneTechnicianInfoReq req) {
        //获取当前登录用户uuid
        String userUuid = TokenHelper.getUserUuid();
        //获取当前登录用户名称
        String userName = TokenHelper.getUserName();
        //获取当前登录用户手机号
        String phone = null;
        LoginToken loginToken = TokenHelper.getLoginToken();
        if (null != loginToken) {
            phone = loginToken.getUserMobile();
        }
        String sceneOrderTechnicianUuid = null;
        //现场下单技师信息uuid 为空时新增，不为空是修改
        if (StringUtils.isEmpty(req.getSceneOrderTechnicianUuid())) {
            //新增现场订单技师内容
            sceneOrderTechnicianUuid = insertOrderTechnician(req, userUuid, userName, phone);
        } else {
            //修改现场订单技师内容
            sceneOrderTechnicianUuid = updateOrderTechnician(req, userUuid, userName, phone);
        }

        //dtc图片新增
        sceneOrderService.insertSceneOrderDtcImg(req.getDtcImgList(), userName, sceneOrderTechnicianUuid,1);
        return ResultRes.success(sceneOrderTechnicianUuid);
    }

    /**
     * 新增现场订单技师内容
     * @param req
     * @return
     */
    private String insertOrderTechnician (AddSceneTechnicianInfoReq req, String userUuid, String userName, String phone) {
        SceneOrderTechnician sceneOrderTechnician = new SceneOrderTechnician();
        sceneOrderTechnician.setUuid(UuidUtils.getUuid());
        sceneOrderTechnician.setOrderUuid(req.getSceneOrderUuid());
        sceneOrderTechnician.setTechnicianUuid(userUuid);
        sceneOrderTechnician.setTechnicianName(userName);
        sceneOrderTechnician.setTechnicianMobile(phone);
        //sceneOrderTechnician.setFaultDesc(req.getFaultDesc());
        sceneOrderTechnician.setAlreadyInspect(req.getAlreadyInspect());
        sceneOrderTechnician.setDtcCode(req.getDtcCode());
        sceneOrderTechnician.setRepairSummary(req.getRepairSummary());
        sceneOrderTechnician.setFaultSolve(req.getFaultSolve());
        sceneOrderTechnician.setSts(StsEnum.ACTIVE.getValue());
        sceneOrderTechnician.setCreatedBy(userName);
        sceneOrderTechnician.setCreatedTime(new Date());
        int insertNum = sceneOrderTechnicianMapper.insert(sceneOrderTechnician);
        if (insertNum <= 0) {
            log.error("新增现场订单技师内容失败，请求参数为：{}", JSON.toJSONString(sceneOrderTechnician));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return sceneOrderTechnician.getUuid();
    }

    /**
     * 新增现场订单技师内容
     * @param req
     * @return
     */
    private String updateOrderTechnician (AddSceneTechnicianInfoReq req, String userUuid, String userName, String phone) {
        SceneOrderTechnician sceneOrderTechnician = new SceneOrderTechnician();
        sceneOrderTechnician.setUuid(req.getSceneOrderTechnicianUuid());
        //sceneOrderTechnician.setFaultDesc(req.getFaultDesc());
        sceneOrderTechnician.setAlreadyInspect(req.getAlreadyInspect());
        sceneOrderTechnician.setDtcCode(req.getDtcCode());
        sceneOrderTechnician.setRepairSummary(req.getRepairSummary());
        sceneOrderTechnician.setFaultSolve(req.getFaultSolve());
        sceneOrderTechnician.setLastUpdatedBy(userName);
        sceneOrderTechnician.setLastUpdatedTime(new Date());
        int updateNum = sceneOrderTechnicianMapper.updateByPrimaryKeySelective(sceneOrderTechnician);
        if (updateNum <= 0) {
            log.error("修改现场订单技师内容失败，请求参数为：{}", JSON.toJSONString(sceneOrderTechnician));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        return sceneOrderTechnician.getUuid();
    }
}

