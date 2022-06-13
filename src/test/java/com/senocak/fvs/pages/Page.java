package com.senocak.fvs.pages;

import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

@Slf4j
public abstract class Page extends LoadableComponent<Page> {
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();
    protected static WebDriver driver;

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
            throw new NotFoundException("Element not found");
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

            log.info("************Typing on ************** Element: {}, Text: {}", el, t);
            clearInput(el);
            if (t instanceof String) {
                el.sendKeys(((String) t));
            } else if (t instanceof Keys) {
                el.sendKeys(((Keys) t));
            }
            sleepms(Constants.USER_WAIT_IN_MS);
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
            WebElement el;
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
     * Clear the input of the given element
     * @param el element to clear the input of
     */
    protected void clearInput(@NotNull WebElement el) {
        el.clear();
        el.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
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
                sleepms(Constants.USER_WAIT_IN_MS);
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
     * Get the visible web elements for the given locator by text contains
     * @param text the text to find the elements
     * @return the web elements
     */
    protected WebElement getElementByContainsText(String text){
        return getElement(By.xpath("//*[contains(text(),'"+text+"')]"));
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
     * Verify if the given element is present
     * @param t the web element to verify
     * @return true if the element is present
     */
    public <T> boolean isDisplayed(T t) {
        try {
            if (t instanceof WebElement) {
                log.debug("Element is WebElement. Checking if it is displayed");
                return ((WebElement) t).isDisplayed();
            } else if (t instanceof By) {
                log.debug("Element is By. Checking if it is displayed");
                return getElement((By) t).isDisplayed();
            }
            throw new NoSuchElementException("Unknown type of element");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Get the base url
     * @return the base url
     */
    public String getUrlFromConfig(){
        return configFileReader.getUrl();
    }
}
