package com.shop.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

	static {
		String fileName = "shop.properties";
		logger.info("开始加载{}文件内容..", fileName);
		props = new Properties();
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8");
			props.load(in);
			logger.info("加载{}文件内容完成..", fileName);
		} catch (FileNotFoundException e) {
			logger.error("{}文件未找到", fileName);
		} catch (IOException e) {
			logger.error("配置文件读取异常", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("{}文件流关闭异常", fileName);
			}
		}
	}

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){

        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }



}
