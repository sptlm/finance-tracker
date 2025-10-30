package kfu.itis.maslennikov.financetracker.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;


public class AppConfigUtil {

    private final static String configFile = "config.json";

    public static AppConfig loadConfig() {

        ObjectMapper om = new ObjectMapper();

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile)) {
            return om.readValue(in, AppConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
