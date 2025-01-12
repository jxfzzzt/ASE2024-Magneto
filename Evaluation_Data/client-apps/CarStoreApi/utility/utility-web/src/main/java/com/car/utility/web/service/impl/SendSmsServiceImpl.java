package com.car.utility.web.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.car.common.res.ResultRes;
import com.car.utility.web.common.constants.SmsConstants;
import com.car.utility.web.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @author xlj
 */
@Service
@Slf4j
public class SendSmsServiceImpl implements SendSmsService {

    @Autowired
    SmsConstants smsConstants;

    /**
     * 阿里云短信发送统一接口
     * @param phone 手机号码
     * @param smsCode   短信模板编码
     * @param content   填充内容
     * @return
     */
    @Override
    public ResultRes<String> sendSms(String phone, String smsCode, String content) {
        DefaultProfile profile = DefaultProfile.getProfile(smsConstants.getRegionId(),smsConstants.getAccessKeyID(), smsConstants.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsConstants.getDomain());
        request.setVersion(smsConstants.getVersion());
        request.setAction(smsConstants.getAction());
        request.putQueryParameter("RegionId", smsConstants.getRegionId());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", smsConstants.getSignName());
        request.putQueryParameter("TemplateCode", smsCode);
        if(!StringUtils.isEmpty(content)){
            request.putQueryParameter("TemplateParam", content);
        }
        String smsRes = null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            smsRes = response.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultRes.success(smsRes);
    }

}
