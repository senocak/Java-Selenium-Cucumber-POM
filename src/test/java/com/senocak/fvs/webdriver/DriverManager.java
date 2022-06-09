package com.senocak.fvs.webdriver;

import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.utility.Enums.DriverType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DriverManager {
    private static WebDriver driver;
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();

    /**
     * Private constructor to prevent instantiation.
     */
    private DriverManager() {}

    /**
     * Returns a web driver instance.
     * @return WebDriver instance.
     */
    public static WebDriver getDriver() {
        if (Objects.isNull(driver)) {
            log.info("Thread has no WedDriver, creating new one");
            switch (configFileReader.getEnvironment()) {
                case LOCAL:
                    return createLocalDriver();
                case REMOTE:
                default:
                    createRemoteDriver();
                    break;
            }
        }
        log.info("Thread has a WedDriver, returning it.");
        return driver;
    }

    /**
     * Create local driver instance
     */
    private static WebDriver createLocalDriver() {
        WebDriver driver = null;
        String BROWSER = System.getProperty("BROWSER");
        DriverType driverType;
        try {
            driverType = DriverType.valueOf(BROWSER.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Browser name not matched. Exception: {}", ExceptionUtils.getMessage(e));
            try {
                driverType = configFileReader.getBrowser();
            }catch (RuntimeException ex) {
                log.error("Error loading config.properties file: {}", ExceptionUtils.getMessage(e));
                log.info("Default browser will be used: {}", DriverType.CHROME.name());
                driverType = DriverType.CHROME;
            }
        }
        String headlessProperty = System.getProperty("HEADLESS");
        List<String> args = new ArrayList<>();
        args.add("--disable-gpu");
        args.add("--disable-extensions");
        args.add("--no-sandbox");
        if (headlessProperty != null && headlessProperty.equals("true")) {
            args.add("--headless");
        }
        String[] arguments = new String[args.size()];
        arguments = args.toArray(arguments);

        log.info("Browser: {}, arguments: {}", driverType.name(), Arrays.toString(arguments));
        switch (driverType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(arguments);
                driver = new ChromeDriver(chromeOptions);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(arguments);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                options.addArguments(arguments);
                driver = new EdgeDriver(options);
                break;
            case SAFARI:
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
        }
        long time = configFileReader.getTime();

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        Duration duration = Duration.ofSeconds(time);
        driver.manage().timeouts().implicitlyWait(duration);
        driver.manage().timeouts().pageLoadTimeout(duration);
        driver.manage().timeouts().setScriptTimeout(duration);
        DriverManager.driver = driver;
        return DriverManager.driver;
    }

    /**
     * Create remote driver instance
     */
    private static void createRemoteDriver() {
        log.error("Remote driver is not implemented yet");
        throw new RuntimeException("Remote web driver is not yet implemented");
    }

    /**
     * Returns a string containing current browser name, its version and OS name.
     * This method is used in the the *WebDriverListeners to change the test name.
     * @return String containing browser info.
     */
    public static String getRemoteWebDriverInfo() {
        log.debug("Getting remote web driver info");
        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
        return "BrowserInfo: " + cap.getBrowserName() + " " + cap.getBrowserVersion() + " " + cap.getPlatformName().toString();
    }

    /**
     * End the current session
     */
    public static void closeDriver() {
        try {
            log.info("Closing driver");
            if (Objects.nonNull(driver)) {
                driver.quit();
                driver.close();
            }
        } catch (Exception e) {
            log.error("Error closing driver. Exception: {}" + ExceptionUtils.getMessage(e));
        } finally {
            log.info("Driver closed");
            driver = null;
        }
    }
}