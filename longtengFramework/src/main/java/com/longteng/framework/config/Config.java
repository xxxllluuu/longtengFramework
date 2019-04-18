package com.longteng.framework.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {

    private static Properties properties;

    static {
        properties = new Properties();
        try {

            File file = new File("");

            properties.load(new InputStreamReader(
                    new FileInputStream(file.getAbsolutePath() + "/config/config.properties"), "UTF-8"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据属性文件中的key，获取属性文件中相应的value
     * 
     * @param key
     * @return
     */
    public static String get(String key) {

        return properties.getProperty(key, "");
    }

    public static void main(String[] args) {

        File temp = new File("");
        System.out.println(temp.getAbsolutePath());

        System.out.println(Config.class.getResource("/").getPath());
    }
}
