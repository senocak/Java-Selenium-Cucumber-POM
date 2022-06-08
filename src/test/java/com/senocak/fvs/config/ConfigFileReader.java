package com.senocak.fvs.config;

import com.senocak.fvs.utility.Enums.DriverType;
import com.senocak.fvs.utility.Enums.EnvironmentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class ConfigFileReader {
    private static ConfigFileReader instance;
    private final Properties properties = new Properties();

    /**
     * Constructor to initialize the properties file
     */
    private ConfigFileReader() {
        try {
            properties.load(ConfigFileReader.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            log.error("Error loading config.properties file: {}", ExceptionUtils.getMessage(e));
            throw new RuntimeException("configuration.properties not found");
        }
    }

    /**
     * Singleton instance of ConfigFileReader
     * @return instance of ConfigFileReader
     */
    public static ConfigFileReader getInstance() {
        return instance == null ? new ConfigFileReader() : instance;
    }

    /**
     * Get the instance of Properties
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Get the value of the property with the given key
     * @return value of the property
     */
    public DriverType getBrowser() {
        String browserName = getPropertyValue("browser");

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
        String environmentName = getPropertyValue("environment");

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
        return getPropertyValue("url");
    }

    /**
     * Get the value of a property
     * @return the value of the property
     */
    public long getTime() {
        return Long.parseLong(getPropertyValue("timeout"));
    }

    /**
     * Get the value of a property
     * @param prop the property to get
     * @return the value of the property
     */
    private String getPropertyValue(String prop) {
        log.info("Getting value for " + prop);
        Optional<String> opt = Optional.ofNullable(properties.getProperty(prop));
        if (opt.isPresent()) {
            log.info("id for " + prop + " is " + opt.get());
            return opt.get();
        }
        log.error("No value found for {}", prop);
        throw new RuntimeException("url not specified in the config file.");
    }
}
