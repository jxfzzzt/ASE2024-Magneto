package cn.qs.service.user;

import java.util.List;

import cn.qs.bean.user.User;
import cn.qs.service.BaseService;

public interface UserService extends BaseService<User, Integer> {

	/**
	 * 根据接口查询所用的用户
	 */
	List<User> findAllUser();

	User findUserByUsername(String username);

	User getUserByUserNameAndPassword(String username, String password);

}
