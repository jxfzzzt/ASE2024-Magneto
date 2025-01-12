package com.car.order.web.service.front.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.car.common.enums.ResEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.response.course.QueryCourseOrderInfoRes;
import com.car.order.client.response.dtc.QueryDtcOrderInfoRes;
import com.car.order.client.response.order.consult.ConsultOrderDetailRes;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.web.dto.OrderCaseDto;
import com.car.order.web.dto.PayOrderInfoDto;
import com.car.order.web.dto.scene.SceneOrderDto;
import com.car.order.web.mapper.consult.ConsultOrderMapper;
import com.car.order.web.mapper.course.CourseOrderMapper;
import com.car.order.web.mapper.dtc.DtcOrderMapper;
import com.car.order.web.mapper.goods.OrderGoodsMapper;
import com.car.order.web.mapper.instance.OrderCaseMapper;
import com.car.order.web.mapper.order.OrderInfoMapper;
import com.car.order.web.mapper.scene.SceneOrderMapper;
import com.car.order.web.mapper.sharetechnicianorder.ShareTechnicianOrderMapper;
import com.car.order.web.model.order.OrderInfo;
import com.car.order.web.model.sharetechnicianorder.ShareTechnicianOrder;
import com.car.order.web.service.front.order.OrderFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/31
 */
@Slf4j
@Service
public class OrderFrontServiceImpl implements OrderFrontService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private ConsultOrderMapper consultOrderMapper;

    @Autowired
    private OrderCaseMapper orderCaseMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private DtcOrderMapper dtcOrderMapper;

    @Autowired
    private CourseOrderMapper courseOrderMapper;

    @Autowired
    private SceneOrderMapper sceneOrderMapper;

    @Autowired
    private ShareTechnicianOrderMapper shareTechnicianOrderMapper;

    /**
     * 查询全部订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<JSONObject> queryOrderInfo(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        JSONObject jsonObject = new JSONObject();
        //查询订单相关信息
        OrderInfo orderInfo = orderInfoMapper.queryOrderInfo(uuid);
        //修改对应订单支付类型
        if (OrderTypeEnum.GOOD.getValue().equals(orderInfo.getOrderType())) {
            //查询维修保养订单详情
            OrderGoodsRes orderGoodsRes = orderGoodsMapper.queryOrderGoods(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(orderGoodsRes, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.GOOD.getValue());

        } else if (OrderTypeEnum.CONSULT.getValue().equals(orderInfo.getOrderType())) {
            //查询咨询订单详情
            ConsultOrderDetailRes consultOrderDetailRes =  consultOrderMapper.queryOrderConsultDetail(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(consultOrderDetailRes, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.CONSULT.getValue());

        } else if (OrderTypeEnum.EXAMPLE.getValue().equals(orderInfo.getOrderType())) {
            //查询案例订单详情
            OrderCaseDto orderCaseDto = orderCaseMapper.queryOrderCaseDetail(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(orderCaseDto, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.EXAMPLE.getValue());

        } else if (OrderTypeEnum.AUDITOR.getValue().equals(orderInfo.getOrderType())) {
            //查询旁听订单详情
            ConsultOrderDetailRes consultOrderDetailRes =  consultOrderMapper.queryOrderConsultDetail(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(consultOrderDetailRes, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.CONSULT.getValue());

        } else if (OrderTypeEnum.DTC.getValue().equals(orderInfo.getOrderType())) {
            //查询DTC订单详情
            QueryDtcOrderInfoRes queryDtcOrderInfoRes = dtcOrderMapper.getById(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(queryDtcOrderInfoRes, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.DTC.getValue());

        } else if (OrderTypeEnum.COURSE.getValue().equals(orderInfo.getOrderType())) {
            //查询课程订单详情
            QueryCourseOrderInfoRes queryCourseOrderInfoRes = courseOrderMapper.getById(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(queryCourseOrderInfoRes, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.COURSE.getValue());

        } else if (OrderTypeEnum.SCENE.getValue().equals(orderInfo.getOrderType())) {
            //查询现场订单详情
            SceneOrderDto sceneOrder = sceneOrderMapper.querySceneOrderInfo(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(sceneOrder, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.SCENE.getValue());

        }else if (OrderTypeEnum.SHARED_TECHNICIAN.getValue().equals(orderInfo.getOrderType())) {
            //查询共享技师订单详情
            ShareTechnicianOrder shareTechnicianOrder = shareTechnicianOrderMapper.queryOrderShareTechnicianOrderInfo(uuid);
            jsonObject = JSON.parseObject(JSON.toJSONString(shareTechnicianOrder, SerializerFeature.WriteMapNullValue));
            jsonObject.put("orderType", OrderTypeEnum.SHARED_TECHNICIAN.getValue());
        }
        return ResultRes.success(jsonObject);
    }

    /**
     * 查询我的提问数量
     * @param userUuid
     * @return
     */
    @Override
    public ResultRes<Integer> queryQuizCount(String userUuid) {
        if (StringUtils.isEmpty(userUuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //查询我的提问数量
        Integer quizCount = consultOrderMapper.queryQuizCount(userUuid);
        return ResultRes.success(quizCount);
    }

    /**
     * 查询我的案例数量
     * @param userUuid
     * @return
     */
    @Override
    public ResultRes<Integer> queryCaseCount(String userUuid) {
        if (StringUtils.isEmpty(userUuid)) {
            throw new BusinessException(ResEnum.LACK_PARAMETER);
        }
        //查询我的案例数量
        Integer caseCount = orderCaseMapper.queryCaseCount(userUuid);
        return ResultRes.success(caseCount);
    }
}
