package pl.chief.cookbook.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    public static final PropertiesUtils INSTANCE = new PropertiesUtils();

    private PropertiesUtils(){
        Properties properties = new Properties();
        String propertyFile = "util.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(propertyFile);
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperties(properties);
    }

    public String getPropertyStringValue(String key){
        Object object = System.getProperties().get(key);
        return (String) object;
    }

    public Object getPropertyObject(String key){
        return System.getProperties().get(key);
    }
}