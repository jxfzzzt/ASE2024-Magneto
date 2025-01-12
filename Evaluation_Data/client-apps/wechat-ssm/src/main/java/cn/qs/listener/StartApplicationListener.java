package cn.qs.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qs.bean.user.User;
import cn.qs.service.user.UserService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.ThreadUtils;
import cn.qs.utils.system.MySystemUtils;
import cn.qs.utils.system.SpringBootUtils;
import cn.qs.utils.user.UserUtils;

@WebListener
public class StartApplicationListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 检查配置文件
		MySystemUtils.checkSettingPropertyFiles();

		ThreadUtils.execute(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("容器启动，开启线程同步数据创建默认用户");
				try {
					Thread.sleep(1 * 60 * 1000L);
				} catch (InterruptedException e) {
					// ignore
				}

				doCreateAdmin();
			}

			private void doCreateAdmin() {
				String adminUserName = "admin";

				UserService userService = SpringBootUtils.getBean(UserService.class);
				User findUserByUsername = userService.findUserByUsername(adminUserName);
				if (findUserByUsername == null) {
					User user = new User();
					String adminPassword = DigestUtils.md5Hex("123456");
					user.setUsername(adminUserName);
					user.setPassword(adminPassword);
					user.setFullname("系统管理员");
					user.setSex("男");
					user.setRoles(DefaultValue.ROLE_SYSYEM);

					UserUtils.addDefaultWechatInfo(user);

					user.setProperty("from", "system");

					userService.add(user);
					LOGGER.info("系统管理员创建成功");
				} else {
					LOGGER.info("系统管理员已经存在");
				}

			}
		});
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ignore
	}

}
