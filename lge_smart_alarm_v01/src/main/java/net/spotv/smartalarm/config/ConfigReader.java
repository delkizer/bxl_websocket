package net.spotv.smartalarm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    
    public ConfigReader() {
        this.properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }    

}
