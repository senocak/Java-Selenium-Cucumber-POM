package com.automation.demo.pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.*;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import static com.automation.demo.utility.Constants.*;

public abstract class Page extends LoadableComponent {
    protected static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    protected static final Properties config = new Properties();
    protected static WebDriver driver;

    static {
        try {
            config.load(Page.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            LOGGER.severe("IOException: " + e.getMessage());
        }
    }

    public String getPropertyValue(String prop) {
        LOGGER.info("Getting value for " + prop);
        Optional<String> opt = Optional.ofNullable(config.getProperty(prop));
        if (opt.isPresent()) {
            LOGGER.info("id for " + prop + " is " + opt.get());
            return opt.get();
        }
        LOGGER.severe(prop + " could not be found in config.properties!");
        return null;
    }

    public void launchBrowser() {
        if (driver != null) endSession();
        if (Objects.equals(getPropertyValue("webdriver"), "chrome")){
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/chromedriver.exe");
            driver = new ChromeDriver();
        }else if (Objects.equals(getPropertyValue("webdriver"), "edge")){
            System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") + "/src/test/resources/drivers/msedgedriver.exe");
            driver = new EdgeDriver();
        }
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    public void get(String url) {
        driver.get(url);
    }
    public void endSession() {
        driver.quit();
    }

    public <P, T> void sendKeys(P p, T t) {
        sleepms(USER_WAIT_IN_MS);
        if ((p == null))
            throw new NotFoundException(p + "is null");
        else if (!(p instanceof WebElement) && !(p instanceof By))
            throw new InvalidArgumentException(p + "parameter type not supported by this function");

        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
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

            LOGGER.fine("************Typing on ************** " + el + " " + t);
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

    public <T> void click(T p) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
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
                LOGGER.info("p is " + p);
                el = (WebElement) ((JavascriptExecutor) driver).executeScript("return " + p);
                if (el == null)
                    return null;
            }
            new WebDriverWait(driver, WEBDRIVER_TIME_OUT_IN_SECONDS)
                    .until(ExpectedConditions.elementToBeClickable(el));
            LOGGER.fine("************Tapping on ************** " + el.toString());
            el.click();
            return el;
        });
        sleepms(USER_WAIT_IN_MS);
    }

    protected boolean isChecked(By by) {
        return getVisibleElement(by).isSelected();
    }

    public void rightClickOn(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        WebElement link = wait.until(driver -> {
            WebElement el;
            el = driver.findElement(by);
            new WebDriverWait(driver, WEBDRIVER_TIME_OUT_IN_SECONDS)
                    .until(ExpectedConditions.elementToBeClickable(el));
            return el;
        });
        Actions action = new Actions(driver);
        action.contextClick(link).perform();
        sleepms(USER_WAIT_IN_MS);
    }

    private void waitUntilDocumentReady() {
        LOGGER.info("checking if document is in ready state");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int i = 0;
        do {
            try {
                if (js.executeScript("return document.readyState").equals("complete")) {
                    LOGGER.info("document state is ready");
                    return;
                }
                LOGGER.info("****************waiting for page to be in ready state***********: " + i);
                sleepms(1000);
                i++;
            } catch (WebDriverException e) {
                LOGGER.info("WebDriverException: " + e.getMessage());
            }
        } while (i < DOCUMENT_READY_TIMEOUT);
        Assert.fail("Document was not ready in "+DOCUMENT_READY_TIMEOUT+" seconds. Check if application is debuggable");
    }

    protected WebElement getElement(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> driver.findElement(by));
    }

    protected WebElement getVisibleElement(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> {
            new WebDriverWait(driver, WEBDRIVER_TIME_OUT_IN_SECONDS)
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return driver.findElement(by);
        });
    }

    protected List<WebElement> getElements(By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(WebDriverException.class)
                .ignoring(TimeoutException.class)
                .withMessage(by + " element could not be found!");
        return wait.until(driver -> driver.findElements(by));
    }

    protected WebElement getLastElement(By by) {
        List<WebElement> els;
        String currentPageTitle;
        try {
            currentPageTitle = driver.getTitle();
        } catch (Exception e) {
            currentPageTitle = "???";
        }
        LOGGER.info("Locating elements " + by.toString() + " on page titled " + currentPageTitle);
        els = getElements(by);
        LOGGER.info("List size is " + els.size());
        return els.get(els.size() - 1);
    }

    public void scroll(By by) {
        TouchActions action = new TouchActions(driver);
        action.scroll(10, 100);
        action.perform();
    }

    public void sleepms(long time) {
        try {
            LOGGER.info("Sleeping for " + time + " ms");
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.info("InterruptedException: " + e.getMessage());
        }
    }

    public byte[] getScreenshot() {
        LOGGER.info("Capturing screenshot");
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public void getLogs() {
        Set<String> logs = driver.manage().logs().getAvailableLogTypes();
        LOGGER.info("Logs are; " + logs);
        System.out.println(logs);
    }
}
