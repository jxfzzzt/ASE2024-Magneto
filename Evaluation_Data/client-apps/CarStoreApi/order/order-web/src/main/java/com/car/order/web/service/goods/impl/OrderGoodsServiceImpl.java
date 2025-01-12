package com.car.order.web.service.goods.impl;

import com.alibaba.fastjson.JSON;
import com.car.account.client.enums.goods.ImgTypeEnum;
import com.car.common.enums.OrderPrefixEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.enums.UserTypeEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.car.order.client.enums.consult.OrderTypeEnum;
import com.car.order.client.enums.driving.ReceiveMethodEnum;
import com.car.order.client.enums.goods.EvaluateStsEnum;
import com.car.order.client.enums.goods.OrderStsEnum;
import com.car.order.client.enums.goods.PayMethodEnum;
import com.car.order.client.enums.goods.ServiceStsEnum;
import com.car.order.client.request.order.goods.*;
import com.car.order.client.request.order.order.AddOrderInfoReq;
import com.car.order.client.response.order.goods.OrderGoodsListRes;
import com.car.order.client.response.order.goods.OrderGoodsRes;
import com.car.order.client.response.order.goods.PreOrderRes;
import com.car.order.client.response.order.goods.sub.ReceiveAddrRes;
import com.car.order.web.common.constants.Constants;
import com.car.order.web.mapper.addr.ReceiveAddrMapper;
import com.car.order.web.mapper.goods.*;
import com.car.order.web.model.addr.ReceiveAddr;
import com.car.order.web.model.goods.*;
import com.car.order.web.service.goods.OrderGoodsService;
import com.car.order.web.service.order.OrderInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Slf4j
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    OrderGoodsMapper orderGoodsMapper;
    @Autowired
    OrderGoodsDetailMapper orderGoodsDetailMapper;
    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    GoodsImagesMapper goodsImagesMapper;
    @Autowired
    GoodsDetailMapper goodsDetailMapper;
    @Autowired
    ReceiveAddrMapper receiveAddrMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    private IdWorker idWorker = new IdWorker(1, 1);
    /**
     * 查询商品订单列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<OrderGoodsListRes>> queryOrderGoodsList(QueryOrderGoodsListReq param) {
        log.debug("查询商品订单列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<OrderGoodsListRes> orderGoodsList = orderGoodsMapper.queryOrderGoodsList(param);
        PageInfo<OrderGoodsListRes> pageInfo = new PageInfo<>(orderGoodsList);
        return PageRes.success(orderGoodsList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询商品订单详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<OrderGoodsRes> queryOrderGoodsDetail(String uuid) {
        log.debug("查询商品订单详情");
        OrderGoodsRes orderGoodsRes = orderGoodsMapper.queryOrderGoods(uuid);
        return ResultRes.success(orderGoodsRes);
    }

    /**
     * 商品订单信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportOrderGoodsList(QueryOrderGoodsListReq exportReq, HttpServletResponse response) {
        log.debug("商品订单信息导出");
        try {
            List<OrderGoodsListRes> orderGoodsList = orderGoodsMapper.queryOrderGoodsList(exportReq);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(Constants.ORDER_GOODS_INFO_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<OrderGoodsListRes> excelList = ExcelUtils.setFieldValue(orderGoodsList);
            Workbook wb = new XSSFWorkbook(resourceAsStream);
            Sheet sheet = wb.getSheetAt(0);
            //从第三行开始写入
            int firstRowIndex = sheet.getFirstRowNum()+2;
            for (int rowIndex = firstRowIndex; rowIndex < excelList.size()+2; rowIndex++) {
                //行样式
                Row rowStyle = (rowIndex % 2) == 0?sheet.getRow(2): sheet.getRow(3);
                //单列样式
                CellStyle cellStyle = ExcelUtils.getExcelFormat(rowStyle.getCell(1));
                CellStyle cellStyle1 = ExcelUtils.getExcelFormat(rowStyle.getCell(0));
                Row row = sheet.getRow(rowIndex);
                if(row == null){
                    row = sheet.createRow(rowIndex);
                }
                row.setHeight(rowStyle.getHeight());
                OrderGoodsListRes exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getOrderNum());
                ExcelUtils.setCell(row,cellStyle,2,exportDto.getGoodsName());
                ExcelUtils.setCell(row,cellStyle,3,StringUtils.isEmpty(exportDto.getGoodsNum()) ? 0 : exportDto.getGoodsNum());
                BigDecimal materialsExpenses = (null != exportDto.getMaterialsExpenses()) ? exportDto.getMaterialsExpenses() : BigDecimal.ZERO;
                BigDecimal manHourCost = (null != exportDto.getManHourCost()) ? exportDto.getManHourCost() : BigDecimal.ZERO;
                BigDecimal actualAmount = (null != exportDto.getActualAmount()) ? exportDto.getActualAmount() : BigDecimal.ZERO;
                ExcelUtils.setCell(row,cellStyle,4,"¥ "+materialsExpenses);
                ExcelUtils.setCell(row,cellStyle,5,"¥ "+manHourCost);
                ExcelUtils.setCell(row,cellStyle,6,exportDto.getCreatedTime());
                ExcelUtils.setCell(row,cellStyle,7, exportDto.getServiceArea());
                ExcelUtils.setCell(row,cellStyle,8, exportDto.getServiceNum());
                ExcelUtils.setCell(row,cellStyle,9, "¥ "+actualAmount);
                ExcelUtils.setCell(row,cellStyle,10, exportDto.getContacts());
                ExcelUtils.setCell(row,cellStyle,11, exportDto.getMobile());
                ExcelUtils.setCell(row,cellStyle,12, PayMethodEnum.enumOfDesc(exportDto.getPayType()));
                ExcelUtils.setCell(row,cellStyle,13, OrderStsEnum.enumOfDesc(exportDto.getOrderSts()));
            }
            ExcelUtils.responseWrite(wb,response, Constants.ORDER_GOODS_INFO_EXPORT_TEMPLATE);
        } catch (Exception ex){
            log.error("商品订单信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }

    private OrderGoods initOrder(CreateOrderReq params){

        String userUuid = TokenHelper.getUserUuid();
        String userName = TokenHelper.getUserName();
        Integer userType = TokenHelper.getUserType();
        if(!UserTypeEnum.vehicle.getType().equals(userType)){
            log.error("非车主用户类型,无法下单");
            throw new BusinessException(ResEnum.VEHICLE_OWNER_NOT_EXIST);
        }

        String goodsId = params.getGoodsId();
        //根据商品查询详细信息
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if(null == goods){
            log.error("未定位到商品信息>>>goodId:{}",goodsId);
            throw new BusinessException(ResEnum.GOODS_NOT_EXIST);
        }

        int surplusNum = goods.getSurplusNum();
        Integer num = params.getNum();

        if(surplusNum < num){
            log.error("库存不足>>>surplusNum:{},buyNum:{}",surplusNum,num);
            throw new BusinessException(ResEnum.GOODS_NOT_ENOUGH);
        }

        List<GoodsDetail> detailList = goodsDetailMapper.queryListByGoodsId(goodsId);
        if(null == detailList || detailList.isEmpty()){

            log.error("未定位到商品明细列表信息>>>goodId:{}",goodsId);
            throw new BusinessException(ResEnum.GOODS_DETAIL_NOT_EXIST);
        }

        BigDecimal goodsAmt = BigDecimal.ZERO;
        for(GoodsDetail s : detailList){
            BigDecimal actAmt = s.getActAmt();
            Integer detailNum = s.getNum();
            if(null == detailNum){
                goodsAmt = DigitUtils.add(goodsAmt,actAmt);
            }else{
                BigDecimal multiply = DigitUtils.multiply(actAmt, BigDecimal.valueOf(detailNum));
                goodsAmt = DigitUtils.add(goodsAmt,multiply);
            }
        }

        //店铺信息
        String storeUuid = goods.getStoreUuid();
        //人工费
        BigDecimal manHourCost = (null != goods.getManHourCost()) ? goods.getManHourCost() : BigDecimal.ZERO;
        //平台返利
        BigDecimal materialsExpenses = (null != goods.getMaterialsExpenses()) ? goods.getMaterialsExpenses() : BigDecimal.ZERO;
        //商品费用
        BigDecimal goodsUnitPrice = DigitUtils.multiply(goodsAmt, new BigDecimal(params.getNum()));
        //平台服务费用
        //BigDecimal platformServiceMoney = null != goods.getPlatformServiceMoney() ? goods.getPlatformServiceMoney() : BigDecimal.ZERO;
        //商品总费用 (商品的单价+工时费+平台服务费-材料费)
        BigDecimal serviceAfterAmt = DigitUtils.add(goodsUnitPrice, manHourCost);
       // BigDecimal amount = DigitUtils.add(serviceAfterAmt, platformServiceMoney);
        BigDecimal receivableAmount = DigitUtils.subtract(serviceAfterAmt, materialsExpenses);
        //计算费用(实收金额)
        BigDecimal totalAmt = receivableAmount;
        /*if (ReceiveMethodEnum.EXPRESS.getCode().equals(params.getReceiveMethod())) {
            //如果为快递，总价-工时费 = 总价（实收金额)
            totalAmt = DigitUtils.subtract(receivableAmount, manHourCost);
        }*/

        Integer receiveMethod = params.getReceiveMethod();
        String remark = params.getRemark();
        //生成订单
        OrderGoods order = new OrderGoods();
        String receiveAddrUuid = params.getReceiveAddrUuid();
        if(StringUtil.isNotBlank(receiveAddrUuid)){
            ReceiveAddr receiveAddr = receiveAddrMapper.selectByPrimaryKey(receiveAddrUuid);

            if(null == receiveAddr){
                log.error("未定位到收货地址>>>receiveAddrUuid:{}",receiveAddrUuid);
                throw new BusinessException(ResEnum.RECEIVE_ADDR_NOT_EXIST);
            }

            String province = receiveAddr.getProvinceName();
            String city = receiveAddr.getCityName();
            String area = receiveAddr.getAreaName();
            String phone = receiveAddr.getPhone();
            String contactor = receiveAddr.getContactor();
            String addr = receiveAddr.getAddr();

            StringJoiner sj = new StringJoiner(".");
            if(StringUtil.isNotBlank(province)){
                sj.add(province);
            }
            if(StringUtil.isNotBlank(city)){
                sj.add(city);
            }

            if(StringUtil.isNotBlank(area)){
                sj.add(area);
            }
            sj.add(addr);

            order.setContacts(contactor);
            order.setMobile(phone);
            order.setServiceArea(area);
            order.setDeliveryAddress(sj.toString());
        }

        order.setUuid(UuidUtils.getUuid());
        order.setSts(StsEnum.ACTIVE.getValue());
        order.setCreatedBy(userName);
        order.setCreatedTime(new Date());

        order.setUserUuid(userUuid);
        order.setOrderNum(OrderUtils.GenOrderNo(OrderPrefixEnum.WX));
        order.setStoreUuid(storeUuid);

        order.setOrderSts(OrderStsEnum.UNPAID.getValue());
        order.setActualAmount(totalAmt);
        order.setReceivableAmount(receivableAmount);
        order.setAmtExpress(materialsExpenses);
        order.setAmtService(manHourCost);
        order.setDeliveryMode(receiveMethod);
        order.setOrderRemark(remark);
        order.setGoodsUuid(goods.getUuid());
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsNum(params.getNum());
        order.setPlatformServiceMoney(goods.getPlatformServiceMoney());
        //查询商品对应图片
        List<GoodsImages> goodsImagesList = goodsImagesMapper.queryListByGoodsId(goodsId);
        String imgUrl = null;
        if (!CollectionUtils.isEmpty(goodsImagesList)) {
            for (GoodsImages goodsImages : goodsImagesList) {
                if (ImgTypeEnum.MAIN_GRAPH.getValue().equals(goodsImages.getImgType())) {
                    imgUrl = goodsImages.getImgPath();
                    if (!StringUtils.isEmpty(imgUrl)) {
                        break;
                    }
                }
            }
        }
        order.setGoodsImgUrl(imgUrl);
        order.setServiceSts(ServiceStsEnum.NOT_SERVICE.getValue());
        order.setEvaluateSts(EvaluateStsEnum.NO_COMMENT.getValue());
        return order;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderReq params) {

        String goodsId = params.getGoodsId();
        OrderGoods order = initOrder(params);
        int relateNum = orderGoodsMapper.insert(order);
        if(0 == relateNum){
            log.error("新增订单失败>>>order:{}",JSON.toJSONString(order));
            throw new BusinessException(ResEnum.INSERT_DB_ERROR);
        }
        //新增order_info信息
        AddOrderInfoReq addOrderInfoReq = new AddOrderInfoReq();
        addOrderInfoReq.setOrderType(OrderTypeEnum.GOOD.getValue());
        addOrderInfoReq.setOrderUuid(order.getUuid());
        orderInfoService.addOrder(addOrderInfoReq);

        String orderNum = order.getOrderNum();
        //生成明细订单
        List<GoodsDetail> detailList = goodsDetailMapper.queryListByGoodsId(goodsId);
        detailList.stream().forEach(goodsDetail ->{

            String goodsDetailUuid = goodsDetail.getUuid();
            String goodsUuid = goodsDetail.getGoodsUuid();
            BigDecimal amt = goodsDetail.getAmt();
            String name = goodsDetail.getName();
            Integer detailNum = goodsDetail.getNum();

            OrderGoodsDetail detail = new OrderGoodsDetail();
            detail.setUuid(UuidUtils.getUuid());
            detail.setSts(StsEnum.ACTIVE.getValue());
            detail.setCreatedBy(TokenHelper.getUserName());
            detail.setCreatedTime(new Date());

            detail.setGoodsUuid(goodsUuid);
            detail.setGoodsDetailUuid(goodsDetailUuid);
            detail.setOrderUuid(orderNum);
            detail.setGoodsName(name);
            detail.setGoodsNum(detailNum);
            detail.setMaterialsExpenses(amt);


            //记录订单明细
            int insert = orderGoodsDetailMapper.insert(detail);
            if(0 == insert){

                log.error("新增订单明细失败>>>detail:{}",JSON.toJSONString(detail));
                throw new BusinessException(ResEnum.INSERT_DB_ERROR);
            }

        });
        //返回订单
        return order.getUuid();
    }

    /**
     * 修改订单服务信息
     * @param req
     * @return
     */
    @Override
    public ResultRes<String> updateGoodsOrder(UpdateServerOrderReq req) {
        String userName = TokenHelper.getUserName();
        //修改订单服务信息
        int updateNum = orderGoodsMapper.updateGoodsOrder(req, userName);
        if (updateNum <= 0) {
            log.error("修改订单服务信息失败，请求参数为：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(req.getOrderUuid());
    }

    /**
     * 修改订单服务信息
     * @param req
     * @return
     */
    @Override
    public ResultRes<String> updateGoodsDeliveryOrder(UpdateDeliveryOrder req) {
        String userName = TokenHelper.getUserName();
        //修改订单服务信息
        int updateNum = orderGoodsMapper.updateGoodsDeliveryOrder(req, userName);
        if (updateNum <= 0) {
            log.error("修改订单服务信息失败，请求参数为：{}", JSON.toJSONString(req));
            throw new BusinessException(ResEnum.UPDATE_DB_ERROR);
        }
        return ResultRes.success(req.getOrderUuid());
    }


    @Override
    public ResultRes<PreOrderRes> preOrder(PreOrderReq params) {
        CreateOrderReq v = new CreateOrderReq();
        v.setGoodsId(params.getGoodsId());
        v.setReceiveMethod(0);
        v.setReceiveMethod(1);
        OrderGoods orderGoods = initOrder(v);

        PreOrderRes res = new PreOrderRes();
        res.setAmt(orderGoods.getActualAmount());
        res.setGoodsUuid(params.getGoodsId());


        String userUuid = TokenHelper.getUserUuid();
        ReceiveAddr ad = new ReceiveAddr();
        ad.setSts(StsEnum.ACTIVE.getValue());
        ad.setUserId(userUuid);
        List<ReceiveAddr> list = receiveAddrMapper.select(ad);
        if(!CollectionUtils.isEmpty(list)){

            ReceiveAddr receiveAddr = list.get(0);
            ReceiveAddrRes r = new ReceiveAddrRes();
            BeanUtils.copyProperties(receiveAddr,r);
            res.setReceiveAddrRes(r);
        }

        GoodsImages images = new GoodsImages();
        images.setSts(StsEnum.ACTIVE.getValue());
        images.setGoodsUuid(params.getGoodsId());
        List<GoodsImages> imgList = goodsImagesMapper.select(images);

        if(!CollectionUtils.isEmpty(imgList)){
            List<GoodsImages> collect = imgList.stream().sorted(Comparator.comparing(GoodsImages::getImgType)).collect(Collectors.toList());

            List<String> imgs = new ArrayList<>();
            collect.stream().forEach(s->{

                String imgPath = s.getImgPath();
                imgs.add(imgPath);
            });
            res.setGoodsImgList(imgs);
        }
        return ResultRes.success(res);
    }
}
