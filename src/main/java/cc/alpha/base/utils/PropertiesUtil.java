package cc.alpha.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	
	protected final static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Properties prop = null;

	private static void init() {
		if (prop != null && !prop.isEmpty()) {
			return;
		}
		prop = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream("config.properties");
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			prop.load(bf);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

	}

	public static String getValue(String key) {
		try {
			init();
			return prop.getProperty(key, "");
		} catch (Exception e) {
			logger.error("获取属性" + key + "异常");
			return "";
		}

	}

	public static String getValue(String key, String deaultVal) {
		try {
			init();
			return prop.getProperty(key, deaultVal);
		} catch (Exception e) {
			logger.error( "获取属性" + key + "异常");
			return deaultVal;
		}

	}

	/**
	 * 重置prop，从文件重新读取配置
	 */
	public static void toReloadProp() {
		prop.clear();
	}
	
	public static void main(String args[]) {
		System.out.println(getValue("server.name", "21323"));
	}
}
