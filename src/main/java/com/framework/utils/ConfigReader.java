package com.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config.properties file");
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}