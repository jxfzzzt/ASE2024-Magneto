package com.car.account.web.service.withdrawal.impl;

import com.car.account.client.enums.withdrawal.WithdrawalTypeEnum;
import com.car.account.client.enums.withdrawal.WithdrawalUserRoleEnum;
import com.car.account.client.request.withdrawal.AddWithdrawalReq;
import com.car.account.client.request.withdrawal.QueryWithdrawalListReq;
import com.car.account.client.request.withdrawal.UpdateWithdrawalReq;
import com.car.account.client.request.withdrawal.WithdrawalDetailReq;
import com.car.account.client.response.withdrawal.QueryWithdrawalListRes;
import com.car.account.client.response.withdrawal.WithdrawalDetailRes;
import com.car.account.client.response.withdrawal.WithdrawalRes;
import com.car.account.web.common.constants.WithdrawalConstants;
import com.car.account.web.mapper.store.StoreAccountMapper;
import com.car.account.web.mapper.store.StoreMapper;
import com.car.account.web.mapper.technician.TechnicianAccountMapper;
import com.car.account.web.mapper.technician.TechnicianMapper;
import com.car.account.web.mapper.withdrawal.WithdrawalDetailMapper;
import com.car.account.web.mapper.withdrawal.WithdrawalMapper;
import com.car.account.web.model.store.Store;
import com.car.account.web.model.store.StoreAccount;
import com.car.account.web.model.withdrawal.Withdrawal;
import com.car.account.web.model.withdrawal.WithdrawalDetail;
import com.car.account.web.service.withdrawal.WithdrawalService;
import com.car.common.enums.CheckStatusEnum;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.*;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Slf4j
@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    WithdrawalMapper withdrawalMapper;
    @Autowired
    WithdrawalDetailMapper withdrawalDetailMapper;

    @Autowired
    StoreAccountMapper storeAccountMapper;
    @Autowired
    StoreMapper storeMapper;

    @Autowired
    TechnicianMapper technicianMapper;
    @Autowired
    TechnicianAccountMapper technicianAccountMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> addWithdrawal(AddWithdrawalReq addWithdrawalReq) {
        log.debug("新增提现记录");
         String roleDesc = WithdrawalUserRoleEnum.enumOfDesc(addWithdrawalReq.getUserRole());
         if (StringUtils.isEmpty(roleDesc)) {
             log.error("用户角色类型输入错误 userRoleType {}",addWithdrawalReq.getUserRole());
             throw new BusinessException(ResEnum.USER_ROLE_TYPE_ERROR);
         }

        String withdrawalTypeDesc = WithdrawalTypeEnum.enumOfDesc(addWithdrawalReq.getWithdrawalType());
        if (StringUtils.isEmpty(withdrawalTypeDesc)) {
            log.error("提现类型输入错误 withdrawalType {}",addWithdrawalReq.getWithdrawalType());
            throw new BusinessException(ResEnum.WITHDRAWAL_TYPE_ERROR);
        }

        if (WithdrawalUserRoleEnum.STORE_WITHDRAWAL.getValue().equals(addWithdrawalReq.getUserRole())) {
            log.debug("店铺提现");
            //TODO 待实现
            storeWithdrawal(addWithdrawalReq);
        } else if (WithdrawalUserRoleEnum.TECHNICIAN_WITHDRAWAL.getValue().equals(addWithdrawalReq.getUserRole())) {
            log.debug("技师提现");
            //TODO 待实现
            technicianWithdrawal(addWithdrawalReq);
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUuid(UuidUtils.getUuid());
        withdrawal.setCreatedBy(TokenHelper.getUserName());
        withdrawal.setCreatedTime(new Date());
        withdrawal.setSts(StsEnum.ACTIVE.getValue());
        withdrawal.setSts(CheckStatusEnum.CHECK_PENDING.getValue());
        BeanUtils.copyProperties(addWithdrawalReq,withdrawal);


        List<WithdrawalDetail> withdrawalDetailList = new ArrayList<>();
//        for (WithdrawalDetailReq withdrawalDetailReq : withdrawalDetailReqList) {
//            WithdrawalDetail withdrawalDetail = new WithdrawalDetail();
//            withdrawalDetail.setUuid(UuidUtils.getUuid());
//            withdrawalDetail.setWithdrawalUuid(withdrawal.getUuid());
//            withdrawalDetail.setCreatedBy(TokenHelper.getUserName());
//            withdrawalDetail.setCreatedTime(new Date());
//            withdrawalDetail.setSts(StsEnum.ACTIVE.getValue());
//            BeanUtils.copyProperties(withdrawalDetailReq,withdrawalDetail);
//            withdrawalDetailList.add(withdrawalDetail);
//        }

        withdrawalMapper.insert(withdrawal);
        withdrawalDetailMapper.batchInsertWithdrawalDetail(withdrawalDetailList);
        return ResultRes.success(withdrawal.getUuid());
    }

    /**
     * 删除提现记录
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<String> deleteWithdrawal(String uuid) {
        log.debug("删除提现记录");
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUuid(uuid);
        withdrawal = withdrawalMapper.selectByPrimaryKey(withdrawal);
        if (withdrawal == null || StsEnum.INVALID.getValue().equals(withdrawal.getSts())) {
            log.error("删除 未匹配到对应数据 uuid {}",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        withdrawal.setSts(StsEnum.INVALID.getValue());
        withdrawalMapper.updateByPrimaryKeySelective(withdrawal);
        withdrawalDetailMapper.deleteWithdrawalDetail(uuid);
        return ResultRes.success(uuid);
    }

    /**
     * 提现审核
     * @param updateWithdrawalReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @TxcTransaction
    public ResultRes<String> checkWithdrawal(UpdateWithdrawalReq updateWithdrawalReq) {
        log.debug("提现审核");
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUuid(updateWithdrawalReq.getUuid());
        withdrawal = withdrawalMapper.selectByPrimaryKey(withdrawal);
        if (withdrawal == null || StsEnum.INVALID.getValue().equals(withdrawal.getSts())) {
            log.error("修改 未匹配到对应数据 uuid {}",updateWithdrawalReq.getUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        BeanUtils.copyProperties(updateWithdrawalReq,withdrawal);
        withdrawalMapper.updateByPrimaryKeySelective(withdrawal);

        //TODO
        //发送审核结果
        return ResultRes.success(updateWithdrawalReq.getUuid());
    }

    /**
     * 查询提现列表
     * @param param
     * @return
     */
    @Override
    public PageRes<List<QueryWithdrawalListRes>> queryWithdrawalList(QueryWithdrawalListReq param) {
        log.debug("查询提现列表");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<QueryWithdrawalListRes> withdrawalList = withdrawalMapper.queryWithdrawalList(param);
        PageInfo<QueryWithdrawalListRes> pageInfo = new PageInfo<>(withdrawalList);

        return PageRes.success(withdrawalList, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
    }

    /**
     * 查询提现详情
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<WithdrawalRes> queryWithdrawalDetailByUuid(String uuid) {
        log.debug("查询  验证提现记录是否存在 userUuid {}",uuid);
        Withdrawal queryWithdrawal = new Withdrawal();
        queryWithdrawal.setUuid(uuid);
        Withdrawal withdrawal = withdrawalMapper.selectByPrimaryKey(queryWithdrawal);
        if (withdrawal == null || StsEnum.INVALID.getValue().equals(withdrawal.getSts())) {
            log.error("查询  提现记录未匹配到对应数据：userUuid{}",uuid);
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        WithdrawalRes withdrawalRes = withdrawalMapper.queryWithdrawalDetailByUuid(uuid);

        if (!StringUtils.isEmpty(withdrawalRes.getUuid())) {
            List<WithdrawalDetailRes> withdrawalDetailResList = withdrawalDetailMapper.queryWithdrawalDetailByWithdrawalUuid(withdrawalRes.getUuid());
            withdrawalRes.setWithdrawalDetailResList(withdrawalDetailResList);
        }
        return ResultRes.success(withdrawalRes);
    }

    /**
     * 提现信息导出
     * @param exportReq
     * @param response
     */
    @Override
    public void exportWithdrawalList(QueryWithdrawalListReq exportReq, HttpServletResponse response) {
        log.debug("提现信息导出");
        try {
            List<QueryWithdrawalListRes> withdrawalList = withdrawalMapper.queryWithdrawalList(exportReq);
            //读取模板文件
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(WithdrawalConstants.WITHDRAWAL_EXPORT_TEMPLATE);
            //设置空行默认属性
            List<QueryWithdrawalListRes> excelList = ExcelUtils.setFieldValue(withdrawalList);
            Workbook wb = new XSSFWorkbook(resourceAsStream);
            Sheet sheet = wb.getSheetAt(0);
            //从第三行开始写入
            int firstRowIndex = sheet.getFirstRowNum()+2;
            QueryWithdrawalListRes exportDto;
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
                exportDto = excelList.get(rowIndex - 2);
                ExcelUtils.setCell(row,cellStyle1,0,rowIndex-1);
                ExcelUtils.setCell(row,cellStyle,1,exportDto.getUserName());
                ExcelUtils.setCell(row,cellStyle,2,exportDto.getMobile());
                if (!StringUtils.isEmpty(exportDto.getCreatedTime())) {
                    ExcelUtils.setCell(row,cellStyle,3, DateUtil.dateToStr(exportDto.getCreatedTime(),DateUtil.YYYY_MM_DD_HH_MM_SS));
                }

                ExcelUtils.setCell(row,cellStyle,4,exportDto.getWithdrawalAmount().toString());
                ExcelUtils.setCell(row,cellStyle,5,WithdrawalUserRoleEnum.enumOfDesc(exportDto.getUserRole()));
                if (!StringUtils.isEmpty(exportDto.getLastUpdatedTime())) {
                    ExcelUtils.setCell(row,cellStyle,6,DateUtil.dateToStr(exportDto.getLastUpdatedTime(),DateUtil.YYYY_MM_DD_HH_MM_SS));
                }
                ExcelUtils.setCell(row,cellStyle,7, CheckStatusEnum.enumOfDesc(exportDto.getCheckSts()));
            }
            ExcelUtils.responseWrite(wb,response, WithdrawalConstants.WITHDRAWAL_EXPORT_TEMPLATE);
        } catch (Exception ex){
            log.error("提现信息导出异常，异常原因：{}", ExceptionUtils.stackTraceToString(ex));
        }
    }

    /**
     * 店铺提现
     * @param addWithdrawalReq
     */
    private void storeWithdrawal(AddWithdrawalReq addWithdrawalReq) {
        Store store = new Store();
        store.setUuid(addWithdrawalReq.getUserUuid());
        store.setSts(StsEnum.ACTIVE.getValue());
        store = storeMapper.selectOne(store);

        if (StringUtils.isEmpty(store)) {
            log.error("店铺不存在 uuid: {}",addWithdrawalReq.getUserUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        StoreAccount storeAccount = new StoreAccount();
        storeAccount.setStoreUuid(store.getUuid());
        storeAccount.setSts(StsEnum.ACTIVE.getValue());
        storeAccount = storeAccountMapper.selectOne(storeAccount);
        if (StringUtils.isEmpty(storeAccount)) {
            log.error("店铺对应账户不存在 店铺uuid: {}",addWithdrawalReq.getUserUuid());
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }

        if (WithdrawalTypeEnum.PARTIAL_WITHDRAWAL.getValue().equals(addWithdrawalReq.getWithdrawalType())) {
            //分类提现
            WithdrawalDetailReq withdrawalDetailReq = addWithdrawalReq.getWithdrawalDetailReq();
            if (StringUtils.isEmpty(withdrawalDetailReq)) {
                log.error("提现详情为null");
                throw new BusinessException(ResEnum.EMPTY_WITHDRAWAL_DETAIL);
            }
            //TODO 根据提现详情验证有效后提现

        } else if (WithdrawalTypeEnum.ALL_WITHDRAWAL.getValue().equals(addWithdrawalReq.getWithdrawalType())) {
            //全部提现
            //TODO 获取账户可提现余额全部提现

        }
    }

    /**
     * 技师提现
     * @param addWithdrawalReq
     */
    private void technicianWithdrawal(AddWithdrawalReq addWithdrawalReq) {

    }
}
