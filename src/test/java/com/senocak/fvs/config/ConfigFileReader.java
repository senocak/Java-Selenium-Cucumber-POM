package com.senocak.fvs.config;

import com.senocak.fvs.utility.Enums.DriverType;
import com.senocak.fvs.utility.Enums.EnvironmentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ConfigFileReader {
    private static final ConfigFileReader instance = new ConfigFileReader();
    private static Config config;

    /**
     * Constructor to initialize the properties file
     */
    private ConfigFileReader() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
            config = (new Yaml(new Constructor(Config.class))).load(inputStream);
            System.out.println(config);
        } catch (Exception e) {
            log.error("Error loading config.yaml file: {}", ExceptionUtils.getMessage(e));
            throw new RuntimeException("configuration.properties not found");
        }
    }

    /**
     * Singleton instance of ConfigFileReader
     * @return instance of ConfigFileReader
     */
    public static ConfigFileReader getInstance() {
        return instance;
    }

    /**
     * Get the instance of Config
     * @return Config instance
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Get the value of the property with the given key
     * @return value of the property
     */
    public DriverType getBrowser() {
        String browserName = config.getBrowser();
        switch (browserName) {
            case "chrome":
                return DriverType.CHROME;
            case "firefox":
                return DriverType.FIREFOX;
            case "edge":
                return DriverType.EDGE;
            case "safari":
                return DriverType.SAFARI;
            default:
                log.error("Browser name key value in configuration file is not matched: {}", browserName);
                throw new RuntimeException("Browser name key value in configuration file is not matched: " + browserName);
        }
    }

    /**
     * Get the environment type (e.g. local, remote)
     * @return EnvironmentType
     */
    public EnvironmentType getEnvironment() {
        String environmentName = config.getEnvironment();

        switch (environmentName) {
            case "local":
                return EnvironmentType.LOCAL;
            case "remote":
                return EnvironmentType.REMOTE;
            default:
                log.error("Environment type key value in configuration file is not matched: {}", environmentName);
                throw new RuntimeException("Environment type key value in configuration file is not matched: " + environmentName);
        }
    }

    /**
     * Get the value of a property
     * @return the value of the property
     */
    public String getUrl() {
        return config.getUrl();
    }

    /**
     * Get the value of a property
     * @return the value of the property
     */
    public long getTime() {
        return Long.parseLong(config.getTimeout());
    }

    /**
     * Get the value of a property
     * @return the value of the property
     */
    public List<Users> getUsers() {
        return config.getUsers();
    }
}
