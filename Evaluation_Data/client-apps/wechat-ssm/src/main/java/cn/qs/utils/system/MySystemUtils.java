package cn.qs.utils.system;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.qs.bean.user.User;
import cn.qs.utils.UUIDUtils;
import cn.qs.utils.file.PropertiesFileUtils;

public class MySystemUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySystemUtils.class);

	static {
		checkSettingPropertyFiles();
	}

	private MySystemUtils() {
	}

	/**
	 * 检查settings.properties文件是否存在，不存在就创建
	 */
	public static void checkSettingPropertyFiles() {
		File userDir = SystemUtils.getUserDir();
		File propertiesFile = new File(userDir, "settings.properties");
		if (!propertiesFile.exists()) {
			try {
				propertiesFile.createNewFile();
				LOGGER.info("create settings.properties success, path: {}", propertiesFile.getAbsolutePath());

				setProperty("productName", "管理网");
			} catch (IOException e) {
				LOGGER.error("create settings.properties failed", e);
			}
		} else {
			LOGGER.info("settings.properties exsisted, path: {}", propertiesFile.getAbsolutePath());
		}
	}

	public static final String settings_file_path = SystemUtils.getUserDir().getAbsolutePath() + "/settings.properties";

	public static String getProductName() {
		return getProperty("productName", "管理网");
	}

	public static String getProperty(String key) {
		return getProperty(key, "");
	}

	public static String getProperty(String key, String defaultValue) {
		return StringUtils.defaultIfBlank(PropertiesFileUtils.getPropertyValueByFilePath(settings_file_path, key),
				defaultValue);
	}

	public static void setProperty(String key, Object value) {
		PropertiesFileUtils.saveOrUpdatePropertyByFilePath(settings_file_path, key, String.valueOf(value));
	}

	public static User getLoginUser() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}

	/**
	 * 是否是微信登录(微信账号)
	 * 
	 * @return
	 */
	public static boolean isWXLogin() {
		Object property = getLoginUser().getProperty("from");
		return "wechat".equals(property);
	}

	public static User getLoginUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}

	public static String getLoginUsername(HttpServletRequest request) {
		return getLoginUser(request).getUsername();
	}

	public static String getLoginUsername() {
		return getLoginUser().getUsername();
	}

	public static File getTmpFile() {
		// 获取到当前系统的临时工作目录
		String tempDirectoryPath = FileUtils.getTempDirectoryPath();
		String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		String tmpFileDir = tempDirectoryPath + date;
		FileUtils.deleteQuietly(new File(tmpFileDir));

		// 创建目录(以日期格式命名)
		File file2 = new File(tmpFileDir);
		file2.mkdir();

		// 创建临时文件,UUID产生名称
		String fileName = UUIDUtils.getUUID2();
		String tmpFileName = (tmpFileDir + "/" + fileName).replace("\\", "/");
		File file = new File(tmpFileName);
		try {
			file.createNewFile();
		} catch (IOException ignore) {
			// ignore
		}

		return file;
	}
}
