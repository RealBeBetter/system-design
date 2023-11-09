package com.example.larktool.config;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Real
 * @since 2023/11/01 14:30
 */
public class ConfigReader {
    public static Properties getConfig(String filename) {
        Properties prop = new Properties();
        ClassLoader classLoader = ConfigReader.class.getClassLoader();
        try (InputStreamReader streamReader = new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream(filename)), StandardCharsets.UTF_8)) {
            prop.load(streamReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return prop;
    }

}
