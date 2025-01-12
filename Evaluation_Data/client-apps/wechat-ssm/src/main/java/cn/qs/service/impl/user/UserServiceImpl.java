package cn.qs.service.impl.user;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.user.User;
import cn.qs.bean.user.UserExample;
import cn.qs.bean.user.UserExample.Criteria;
import cn.qs.mapper.user.UserMapper;
import cn.qs.service.user.UserService;
import cn.qs.utils.securty.MD5Utils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> findAllUser() {
		UserExample userExample = new UserExample();
		List<User> list = userMapper.selectByExample(userExample);
		return list;
	}

	@Override
	public void add(User user) {
		userMapper.insert(user);
	}

	@Override
	public List<User> listByCondition(Map condition) {
		UserExample userExample = new UserExample();
		if (StringUtils.isNotBlank(MapUtils.getString(condition, "username"))) {
			Criteria createCriteria = userExample.createCriteria();
			createCriteria.andUsernameLike("%" + MapUtils.getString(condition, "username") + "%");
		}
		List<User> list = userMapper.selectByExample(userExample);
		return list;
	}

	@Override
	public void delete(Integer id) {
		userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public User findById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(User user) {
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(MD5Utils.md5(user.getPassword()));
		} else {
			user.setPassword(null);
		}

		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public User getUserByUserNameAndPassword(String username, String password) {
		UserExample userExample = new UserExample();
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andUsernameEqualTo(username);
		createCriteria.andPasswordEqualTo(MD5Utils.md5(password));

		List<User> selectByExample = userMapper.selectByExample(userExample);
		if (CollectionUtils.isNotEmpty(selectByExample)) {
			return selectByExample.get(0);
		}

		return null;
	}

	@Override
	public User findUserByUsername(String username) {
		UserExample userExample = new UserExample();
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andUsernameEqualTo(username);

		List<User> users = userMapper.selectByExample(userExample);
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}

		return users.get(0);
	}

	@Override
	public Page<User> pageByCondition(Map condition) {
		return null;
	}

}
