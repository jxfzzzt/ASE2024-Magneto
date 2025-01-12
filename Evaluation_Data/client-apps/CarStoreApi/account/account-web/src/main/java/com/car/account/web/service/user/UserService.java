package com.car.account.web.service.user;

import com.car.account.client.request.user.UpdateUserImgReq;
import com.car.common.res.ResultRes;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/10
 */
public interface UserService {
    /**
     * 修改用户头像
     * @param req
     * @return
     */
    ResultRes<String> updateUserPhotoImg(UpdateUserImgReq req);


    /**
     * 查询用户头像信息
     * @return
     */
    ResultRes<String> queryUserPhotoImgUrl();
}
