package cc.alpha.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * 扩展Spring PropertyPlaceholderConfigurer实现根据环境加载配置文件
 * @author yizhiqiang
 *
 */
public class ModeSupportPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {
	private static Logger logger = LoggerFactory.getLogger(ModeSupportPlaceHolderConfigurer.class);
	private static final String PRODUCTION_MODE = "production.mode";
	private Resource[] locations;
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private boolean ignoreResourceNotFound = false;
	public static final String PROPERTY_FILE_EXTENSION = ".properties";
	private String fileEncoding;
	private static final String[] allowModes = new String[] { "daily", "publish" };

	public static final boolean isOnline() {
		return "publish".equalsIgnoreCase(System.getProperty(PRODUCTION_MODE));
	}

	/**
	 * Load properties into the given instance.
	 * @param props
	 *            the Properties instance to load into
	 * @throws java.io.IOException
	 *             in case of I/O errors
	 * @see #setLocations
	 */
	@Override
	protected void loadProperties(Properties props) throws IOException {
		if (this.locations != null) {
			String mode = System.getProperty(PRODUCTION_MODE);
			if (StringUtils.isEmpty(mode)) {
				mode = "daily";
			}
			mode = mode.toLowerCase();
			if (logger.isDebugEnabled()) {
				logger.debug("production model is " + mode);
			}
			for (Resource location : this.locations) {
				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				InputStream is = null;
				try {
					String filename = null;
					try {
						filename = location.getFilename();
					} catch (IllegalStateException ex) {
						// resource is not file-based. See SPR-7552.
					}
					is = location.getInputStream();
					if (filename != null && filename.endsWith(PROPERTY_FILE_EXTENSION) && !isExcludeProperty(mode, filename)) {
						if (this.fileEncoding != null) {
							this.propertiesPersister.load(props, new InputStreamReader(is, this.fileEncoding));
						} else {
							this.propertiesPersister.load(props, is);
						}
					}
				} catch (IOException ex) {
					if (this.ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
						}
					} else {
						throw ex;
					}
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
	}

	private String[] getExcludeMode(String mode) {
		if (StringUtils.isEmpty(mode)) {
			return allowModes;
		}
		List<String> excludeModes = new ArrayList<String>();
		mode = mode.toLowerCase();
		for (String m : allowModes) {
			if (!m.equals(mode)) {
				excludeModes.add(m);
			}
		}
		return excludeModes.toArray(new String[] {});
	}

	private boolean isExcludeProperty(String mode, String filename) {
		String[] excludeModes = getExcludeMode(mode);
		for (String m : excludeModes) {
			if (filename.endsWith(m + PROPERTY_FILE_EXTENSION)) {
				return true;
			}
		}
		return false;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
		this.propertiesPersister = propertiesPersister;
	}

	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public void setFileEncoding(String fileEncoding) {
		this.fileEncoding = fileEncoding;
	}
}