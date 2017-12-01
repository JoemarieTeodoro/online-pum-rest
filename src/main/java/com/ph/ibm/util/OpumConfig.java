package com.ph.ibm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class OpumConfig {

	Logger logger = Logger.getLogger(OpumConfig.class);

	private static Properties props;

	private OpumConfig() {
	}

	static {
		if (props == null) {
			props = new Properties();
			ClassLoader classLoader = OpumConfig.class.getClassLoader();
			InputStream in = classLoader.getResourceAsStream("opum_config.properties");
			try {
				props.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Properties getConfigProperties() {
		return props;
	}

}
