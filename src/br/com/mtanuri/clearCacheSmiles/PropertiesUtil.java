package br.com.mtanuri.clearCacheSmiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	Properties prop = new Properties();

	public PropertiesUtil() throws IOException {
		InputStream in = PropertiesUtil.class
				.getResourceAsStream("/br/com/mtanuri/clearCacheSmiles/config/config.properties");
		prop.load(in);
		in.close();
	}

	public String getPropertie(String key) {
		return prop.get(key).toString();
	}

}
