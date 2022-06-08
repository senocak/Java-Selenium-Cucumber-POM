package com.senocak.fvs.assertions;

import com.senocak.fvs.pages.Page;
import com.senocak.fvs.utility.Constants;
import io.cucumber.core.exception.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.regex.Pattern;

@Slf4j
public class Assert extends Page {

    /**
     * This method is used to assert the text to be present in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param textToFind - Text to be present in the element.
     */
    public static void userSeesTextInSource(String errorMessage, String textToFind) {
        waitUntil(d -> d.getPageSource().contains(textToFind),errorMessage);
    }

    /**
     * This method is used to wait until the element is present in the DOM.
     * @param ec - ExpectedCondition
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     */
    private static void waitUntil(ExpectedCondition<Boolean> ec, String errorMessage) {
        try {
            (new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT)))
                    .ignoring(ClassCastException.class)
                    .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                    .ignoring(WebDriverException.class)
                    .withMessage(errorMessage)
                    .until(ec);
        } catch (TimeoutException e) {
            org.junit.Assert.fail(errorMessage);
        } catch (Exception e) {
            log.error("Error in waitUntil method: {}", ExceptionUtils.printStackTrace(e));
            throw e;
        }
    }

    /**
     * This method is used to wait until the element is present in the DOM.
     * @param ec - ExpectedCondition
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     */
    private static void waitWebElementUntil(ExpectedCondition<WebElement> ec, String errorMessage) {
        try {
            (new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT)))
                    .ignoring(ClassCastException.class)
                    .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                    .ignoring(WebDriverException.class)
                    .withMessage(errorMessage)
                    .until(ec);
        } catch (TimeoutException e) {
            org.junit.Assert.fail(errorMessage);
        } catch (Exception e) {
            log.error("Error in waitUntil method: {}", ExceptionUtils.printStackTrace(e));
            throw e;
        }
    }

    /**
     * This method is used to assert the text to be present in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     * @param textExpected - Text to be present in the element.
     */
    public static void assertTextToBePresentInElementLocated(String errorMessage, By by, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElementLocated(by, textExpected), errorMessage);
    }

    /**
     * This method is used to assert the text to be present in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param webElement - WebElement
     * @param textExpected  - Text to be present in the element.
     */
    public static void assertTextToBePresentInElement(String errorMessage, WebElement webElement, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElement(webElement, textExpected), errorMessage);
    }

    /**
     * This method is used to assert the text to be present in the element value.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     * @param textExpected - Text to be present in the element value.
     */
    public static void assertTextToBePresentInElementValue(String errorMessage, By by, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElementValue(by, textExpected), errorMessage);
    }

    /**
     * This method is used to assert the attribute to be present in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     * @param attribute - Attribute to be present in the element.
     * @param value - Value of the attribute.
     */
    public static void assertAttributeContains(String errorMessage, By by, String attribute, String value) {
        waitUntil(ExpectedConditions.attributeContains(by, attribute, value),errorMessage);
    }

    /**
     * This method is used to assert the attributes to be present in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     * @param attribute - Attribute to be present in the element.
     * @param value - Value of the attribute.
     * @param timeout   - Timeout in seconds.
     */
    public static void assertAttributeContains(String errorMessage, By by, String attribute, String value, long timeout) {
        (new WebDriverWait(driver, Duration.ofSeconds(timeout)))
                .ignoring(ClassCastException.class)
                .withMessage(errorMessage)
                .until(ExpectedConditions.attributeContains(by, attribute, value));
    }

    /**
     * This method is used to assert the text to be matched in the element.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     * @param pattern - Pattern to be matched in the element.
     */
    public static void assertTextMatches(String errorMessage, By by, Pattern pattern) {
        waitUntil(ExpectedConditions.textMatches(by, pattern),errorMessage);
    }

    /**
     * This method is used to assert the js return value.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param javascript - Javascript to be executed.
     */
    public static void assertJSReturnsTrue(String errorMessage, String javascript) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(Constants.EXPECTED_CONDITION_POLLING_INTERVAL))
                .withMessage(errorMessage)
                .ignoring(NoSuchElementException.class);
        wait.until(driver -> {
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            return (boolean) js.executeScript(javascript);
        });
    }

    /**
     * This method is used to assert the js return value and text matches.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param javascript - Javascript to be executed.
     * @param textExpected - Text to be matched in the element.
     */
    public static void assertJSReturnsValueAndTextMatches(String errorMessage, String javascript, String textExpected) {
        String result = (String) (new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPECTED_CONDITION_TIMEOUT)))
                .ignoring(ClassCastException.class)
                .withMessage(errorMessage)
                .until(ExpectedConditions.jsReturnsValue(javascript));
        org.junit.Assert.assertTrue(result.equalsIgnoreCase(textExpected));
    }

    /**
     * This method is used to assert the element to be visible.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     */
    public static void assertElementVisible(String errorMessage, By by) {
        waitWebElementUntil(ExpectedConditions.visibilityOfElementLocated(by),errorMessage);
    }

    /**
     * This method is used to assert the element to be visible.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param el - WebElement
     */
    public static void assertElementVisible(String errorMessage, WebElement el) {
        waitWebElementUntil(ExpectedConditions.visibilityOf(el),errorMessage);
    }

    /**
     * This method is used to assert the element to be visible.
     * @param errorMessage - Error message to be displayed if the element is not present in the DOM.
     * @param by - By locator
     */
    public static void assertElementInvisible(String errorMessage, By by) {
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(by),errorMessage);
    }

    @Override protected void load() {}
    @Override protected void isLoaded() throws Error {}
}

