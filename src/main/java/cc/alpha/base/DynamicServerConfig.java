package cc.alpha.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import cc.alpha.base.utils.PropertiesUtil;


public class DynamicServerConfig extends PropertyPlaceholderConfigurer {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected Properties[] localProperties;

	protected boolean localOverride = false;

	private Resource[] locations;

	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void setLocalOverride(boolean localOverride) {
		this.localOverride = localOverride;
	}

	/**
	 * 根据启动参数,动态读取配置文件
	 */
	protected void loadProperties(Properties props) throws IOException {
		if (locations != null) {
			logger.info("Start loading properties file.....");
			for (Resource location : this.locations) {
				InputStream is = null;
				try {
					String filename = location.getFilename();
					String env = resolveSystemProperty("spring.profiles.active");//从系统配置获取
					if(env==null || env.equals("")) {
						env = PropertiesUtil.getValue("spring.profiles.active");//从配置文件获取
					}
					if(env==null || env.equals("")) {
						env = "dev";//获取不到默认为开发环境
					}
					String envconfig = "server." + env + ".properties";
					if (filename.contains(envconfig)) {
						logger.info("Loading properties file from " + location);
						is = location.getInputStream();
						this.propertiesPersister.load(props, is);
						break;
					}
				} catch (IOException ex) {
					logger.info("读取配置文件失败.....");
					ex.printStackTrace();
					throw ex;
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
	}
	
	protected String resolveSystemProperty(String key, String def) {
		String val = resolveSystemProperty(key);
		return (val == null) ? def : val;
	}
}
