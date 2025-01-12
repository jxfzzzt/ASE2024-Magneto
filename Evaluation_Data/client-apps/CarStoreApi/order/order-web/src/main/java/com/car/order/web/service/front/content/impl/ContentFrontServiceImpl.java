package com.car.order.web.service.front.content.impl;

import com.car.common.enums.UserTypeEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.order.client.enums.consult.ConsultImgTypenum;
import com.car.order.client.enums.front.QueryStateEnum;
import com.car.order.client.enums.goods.EvaluateStsEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.ServiceStsEnum;
import com.car.order.client.request.order.consult.QueryOrderConsultFrontListReq;
import com.car.order.client.response.order.consult.ConsultOrderFrontRes;
import com.car.order.client.response.order.consult.OrderConsultFrontListRes;
import com.car.order.web.dto.OrderConsultDto;
import com.car.order.web.mapper.content.ContentMapper;
import com.car.order.web.mapper.consult.OrderConsultFrontMapper;
import com.car.order.web.service.front.content.ContentFrontService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Slf4j
@Service
public class ContentFrontServiceImpl implements ContentFrontService {

    @Autowired
    OrderConsultFrontMapper orderConsultFrontMapper;

    @Autowired
    private ContentMapper contentMapper;

    /**
     * 查询付费咨询订单列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderConsultFrontListRes>> queryOrderConsultList(QueryOrderConsultFrontListReq param) {
        log.debug("查询付费咨询订单列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        OrderConsultDto orderConsultDto = new OrderConsultDto();
        if (QueryStateEnum.UNPAID.getValue().equals(param.getState())) {
            //查询待付款订单
            orderConsultDto.setOrderSts(OrderStsEnum.UNPAID.getValue());
        } else if (QueryStateEnum.STAY_SERVICE.getValue().equals(param.getState())) {
            //查询待服务订单（已支付订单与未服务订单）
            orderConsultDto.setOrderSts(OrderStsEnum.HAVE_PAID.getValue());
            orderConsultDto.setServiceSts(ServiceStsEnum.NOT_SERVICE.getValue());
        } else if (QueryStateEnum.STAY_EVALUATE.getValue().equals(param.getState())) {
            //查询待评价订单
            orderConsultDto.setOrderSts(OrderStsEnum.HAVE_PAID.getValue());
            orderConsultDto.setEvaluateSts(EvaluateStsEnum.NO_COMMENT.getValue());
            orderConsultDto.setServiceSts(ServiceStsEnum.ALREADY_SERVICE.getValue());
        } else if (QueryStateEnum.REFUND.getValue().equals(param.getState())) {
            //查询退款订单
            List<Integer> list = new ArrayList<>();
            list.add(OrderStsEnum.A_REFUND_OF.getValue());
            list.add(OrderStsEnum.REFUND_SUCCESS.getValue());
            list.add(OrderStsEnum.REFUND_FAILURE.getValue());
            orderConsultDto.setOrderStsList(list);
        }
        if(UserTypeEnum.technician.getType().equals(TokenHelper.getUserType())){
            orderConsultDto.setTechnicianUuid(TokenHelper.getUserUuid());
        }else{
            orderConsultDto.setCarOwnerUuid(TokenHelper.getUserUuid());
        }
        List<OrderConsultFrontListRes> contentList = orderConsultFrontMapper.queryContentList(orderConsultDto);
        PageInfo<OrderConsultFrontListRes> pageInfo = new PageInfo<>(contentList);

        return PageRes.success(contentList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询付费咨询订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<ConsultOrderFrontRes> queryOrderConsultDetail(String uuid) {
        log.debug("付费咨询订单详情 uuid {}",uuid);
        ConsultOrderFrontRes consultOrderFrontRes = orderConsultFrontMapper.queryContentDetail(uuid);
        if (!StringUtils.isEmpty(consultOrderFrontRes)) {
            //查询咨询图片
            List<String> consultImgUrlList = contentMapper.queryImgUrl(consultOrderFrontRes.getConsultUuid(), ConsultImgTypenum.CONSULT_IMG.getValue());
            //查询回答图片
            List<String> answerUrlList = contentMapper.queryImgUrl(consultOrderFrontRes.getConsultUuid(), ConsultImgTypenum.ANSWER_IMG.getValue());
            consultOrderFrontRes.setConsultImgUrlList(consultImgUrlList);
            consultOrderFrontRes.setAnswerUrlList(answerUrlList);
        }
        return ResultRes.success(consultOrderFrontRes);
    }
}
