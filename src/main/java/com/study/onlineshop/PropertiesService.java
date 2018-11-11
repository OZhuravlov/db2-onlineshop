package com.study.onlineshop;

import java.io.IOException;
import java.util.Properties;

public class PropertiesService {

    Properties properties;

    public PropertiesService(String propertiesFile){
        properties = new Properties();
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            properties.load(classLoader.getResourceAsStream(propertiesFile));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
