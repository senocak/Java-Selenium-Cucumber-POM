package com.senocak.fvs.pages;

import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.utility.Enums.DriverType;
import com.senocak.fvs.utility.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class Page extends LoadableComponent {
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();
    protected static WebDriver driver;

    /**
     * Launch the browser
     */
    public WebDriver launchBrowser() {
        if (driver != null)
            closeDriver();
        switch (configFileReader.getEnvironment()) {
            case LOCAL:
                driver = createLocalDriver();
                break;
            case REMOTE:
            default:
                createRemoteDriver();
                break;
        }
        return driver;
    }

    /**
     * Create local driver instance
     * @return web driver instance
     */
    private WebDriver createLocalDriver() {
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
        return driver;
    }

    /**
     * Create remote driver instance
     * @return web driver instance
     */
    private WebDriver createRemoteDriver() {
        throw new RuntimeException("Remote web driver is not yet implemented");
    }

    /**
     * End the current session
     */
    public void closeDriver() {
        try {
            driver.close();
            driver.quit();
        } catch (Exception e) {
            log.error("Error closing driver. Exception: {}" + ExceptionUtils.getMessage(e));
        }
    }

    /**
     * Redirect to the given url
     * @param url the url to redirect to
     */
    public void get(String url) {
        driver.get(url);
    }

    /**
     * Send keys to the given element
     * @param p the element to send keys to
     * @param t the keys to send
     * @param <P> the type of element
     * @param <T> the type of keys
     */
    public <P, T> void sendKeys(P p, T t) {
        sleepms(Constants.USER_WAIT_IN_MS);
        if ((p == null))
            throw new NotFoundException(p + "is null");
        else if (!(p instanceof WebElement) && !(p instanceof By))
            throw new InvalidArgumentException(p + "parameter type not supported by this function");

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class);
        wait.until(driver -> {
            WebElement el;
            if (p instanceof By) {
                el = driver.findElement((By) p);
            } else {
                el = ((WebElement) p);
            }

            log.info("************Typing on ************** " + el + " " + t);
            if (t instanceof String) {
                el.clear();
                el.sendKeys(((String) t));
            } else if (t instanceof Keys) {
                el.sendKeys(((Keys) t));
            }
            sleepms(500);
            return el;
        });
    }

    /**
     * Click on the given element
     * @param p the element to click on
     * @param <T> the type of element
     */
    public <T> void click(T p) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class)
                .ignoring(ElementClickInterceptedException.class);
        wait.until(driver -> {
            WebElement el = null;
            if (p instanceof By) {
                el = driver.findElement((By) p);
            } else if (p instanceof WebElement) {
                el = ((WebElement) p);
            } else if (p instanceof String) {
                log.info("p is " + p);
                el = (WebElement) ((JavascriptExecutor) driver).executeScript("return " + p);
                if (el == null){
                    log.error("Element not found named " + p);
                    throw new NotFoundException(p + "is null");
                }
            }else{
                log.error("Element not found named " + p);
                throw new InvalidArgumentException(p + "parameter type not supported by this function");
            }
            new WebDriverWait(driver, Duration.ofSeconds(Constants.WEBDRIVER_TIME_OUT_IN_SECONDS))
                    .until(ExpectedConditions.elementToBeClickable(el));
            log.info("************Tapping on ************** " + el);
            el.click();
            return el;
        });
        sleepms(Constants.USER_WAIT_IN_MS);
    }

    /**
     * Check if the given element is checked
     * @param by the element to check
     * @return true if the element is checked
     */
    protected boolean isChecked(By by) {
        return getVisibleElement(by).isSelected();
    }

    /**
     * Right click on the given element
     * @param by the element to right click on
     */
    public void rightClickOn(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        WebElement link = wait.until(driver -> {
            WebElement el;
            el = driver.findElement(by);
            new WebDriverWait(driver, Duration.ofSeconds(Constants.WEBDRIVER_TIME_OUT_IN_SECONDS))
                    .until(ExpectedConditions.elementToBeClickable(el));
            return el;
        });
        Actions action = new Actions(driver);
        action.contextClick(link).perform();
        sleepms(Constants.USER_WAIT_IN_MS);
    }

    /**
     * Wait for the given element to be visible
     */
    private void waitUntilDocumentReady() {
        log.info("checking if document is in ready state");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int i = 0;
        do {
            try {
                if (js.executeScript("return document.readyState").equals("complete")) {
                    log.info("document state is ready");
                    return;
                }
                log.info("****************waiting for page to be in ready state***********: " + i);
                sleepms(1000);
                i++;
            } catch (WebDriverException e) {
                log.info("WebDriverException: " + e.getMessage());
            }
        } while (i < Constants.DOCUMENT_READY_TIMEOUT);
        Assert.fail("Document was not ready in "+ Constants.DOCUMENT_READY_TIMEOUT+" seconds. Check if application is debuggable");
    }

    /**
     * Get the web element for the given locator
     * @param by the locator to find the element
     * @return the web element
     */
    protected WebElement getElement(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> driver.findElement(by));
    }

    /**
     * Get the visible web element for the given locator
     * @param by the locator to find the element
     * @return the web element
     */
    protected WebElement getVisibleElement(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> {
            new WebDriverWait(driver, Duration.ofSeconds(Constants.WEBDRIVER_TIME_OUT_IN_SECONDS))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return driver.findElement(by);
        });
    }

    /**
     * Get the elements for the given locator
     * @param by the locator to find the elements
     * @return the web elements
     */
    protected List<WebElement> getElements(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .ignoring(TimeoutException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> driver.findElements(by));
    }

    /**
     * Get the last visible web element for the given locator
     * @param by the locator to find the element
     * @return the web element
     */
    protected WebElement getLastElement(By by) {
        List<WebElement> els;
        String currentPageTitle;
        try {
            currentPageTitle = driver.getTitle();
        } catch (Exception e) {
            currentPageTitle = "???";
        }
        log.info("Locating elements " + by.toString() + " on page titled " + currentPageTitle);
        els = getElements(by);
        log.info("List size is " + els.size());
        return els.get(els.size() - 1);
    }

    /**
     * Scroll to the element
     * @param by the locator to find the element
     */
    public void scroll(By by) {
        TouchActions action = new TouchActions(driver);
        action.scroll(10, 100);
        action.perform();
    }

    /**
     * Sleep for the given time
     * @param time the time to sleep
     */
    public void sleepms(long time) {
        try {
            log.info("Sleeping for " + time + " ms");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.info("InterruptedException: " + e.getMessage());
        }
    }

    /**
     * Get the screen shot of the current page
     * @return the screen shot
     */
    public byte[] getScreenshot() {
        log.info("Capturing screenshot");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Get the logs for the current page
     */
    public void getLogs() {
        Set<String> logs = driver.manage().logs().getAvailableLogTypes();
        log.info("Logs are; " + logs);
        System.out.println(logs);
    }


    public String getUrlFromConfig(){
        return configFileReader.getUrl();
    }
}
