package com.automation.demo.pages;

import lombok.extern.slf4j.Slf4j;
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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import static com.automation.demo.utility.Constants.DOCUMENT_READY_TIMEOUT;
import static com.automation.demo.utility.Constants.EXPECTED_CONDITION_POLLING_INTERVAL;
import static com.automation.demo.utility.Constants.EXPECTED_CONDITION_TIMEOUT;
import static com.automation.demo.utility.Constants.USER_WAIT_IN_MS;
import static com.automation.demo.utility.Constants.WEBDRIVER_TIME_OUT_IN_SECONDS;

@Slf4j
public abstract class Page extends LoadableComponent {
    protected static final Properties config = new Properties();
    protected static WebDriver driver;

    static {
        try {
            config.load(Page.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            log.error("Error loading config.properties file", e);
        }
    }

    /**
     * Get the value of a property
     * @param prop the property to get
     * @return the value of the property
     */
    public String getPropertyValue(String prop) {
        log.info("Getting value for " + prop);
        Optional<String> opt = Optional.ofNullable(config.getProperty(prop));
        if (opt.isPresent()) {
            log.info("id for " + prop + " is " + opt.get());
            return opt.get();
        }
        log.error("No value found for {}", prop);
        return null;
    }

    /**
     * Launch the browser
     */
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

    /**
     * Redirect to the given url
     * @param url the url to redirect to
     */
    public void get(String url) {
        driver.get(url);
    }

    /**
     * End the current session
     */
    public void endSession() {
        driver.quit();
    }

    /**
     * Send keys to the given element
     * @param p the element to send keys to
     * @param t the keys to send
     * @param <P> the type of element
     * @param <T> the type of keys
     */
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
                log.info("p is " + p);
                el = (WebElement) ((JavascriptExecutor) driver).executeScript("return " + p);
                if (el == null)
                    return null;
            }
            new WebDriverWait(driver, WEBDRIVER_TIME_OUT_IN_SECONDS)
                    .until(ExpectedConditions.elementToBeClickable(el));
            log.info("************Tapping on ************** " + el.toString());
            el.click();
            return el;
        });
        sleepms(USER_WAIT_IN_MS);
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
        } while (i < DOCUMENT_READY_TIMEOUT);
        Assert.fail("Document was not ready in "+DOCUMENT_READY_TIMEOUT+" seconds. Check if application is debuggable");
    }

    /**
     * Get the web element for the given locator
     * @param by the locator to find the element
     * @return the web element
     */
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

    /**
     * Get the visible web element for the given locator
     * @param by the locator to find the element
     * @return the web element
     */
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

    /**
     * Get the elements for the given locator
     * @param by the locator to find the elements
     * @return the web elements
     */
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
}
