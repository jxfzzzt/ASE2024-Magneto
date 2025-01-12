package cn.qs.utils.user;

import org.apache.commons.lang3.math.NumberUtils;

import cn.qs.bean.user.User;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.system.MySystemUtils;

public class UserUtils {

	private UserUtils() {
	}

	public static void addDefaultWechatInfo(User user) {
		if (user == null) {
			return;
		}

		// 设置第一次登陆的优惠金额
		if (user.getCoupon() == null) {
			user.setCoupon(NumberUtils.toFloat(MySystemUtils.getProperty("coupon", "0")));
		}
		user.setWechataddress("中国北京");
		user.setWechatnickname(user.getFullname());
		user.setWechatphoto(DefaultValue.DEFAULT_USER_WECHAT_PHOTO);
	}

}
