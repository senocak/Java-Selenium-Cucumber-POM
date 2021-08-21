package com.automation.demo.Assertions;

import com.automation.demo.pages.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.regex.Pattern;
import static com.automation.demo.utility.Constants.*;

public class Assert extends Page {

    public static void userSeesTextInSource(String errorMessage, String textToFind) {
        waitUntil(d -> d.getPageSource().contains(textToFind),errorMessage);
    }

    private static void waitUntil(ExpectedCondition<Boolean> ec, String errorMessage) {
        try {
            (new WebDriverWait(driver, EXPECTED_CONDITION_TIMEOUT))
                    .ignoring(ClassCastException.class)
                    .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                    .ignoring(WebDriverException.class)
                    .withMessage(errorMessage)
                    .until(ec);
        } catch (TimeoutException e) {
            org.junit.Assert.fail(errorMessage);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void waitWebElementUntil(ExpectedCondition<WebElement> ec, String errorMessage) {
        try {
            (new WebDriverWait(driver, EXPECTED_CONDITION_TIMEOUT))
                    .ignoring(ClassCastException.class)
                    .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                    .ignoring(WebDriverException.class)
                    .withMessage(errorMessage)
                    .until(ec);
        } catch (TimeoutException e) {
            org.junit.Assert.fail(errorMessage);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void assertTextToBePresentInElementLocated(String errorMessage, By by, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElementLocated(by, textExpected), errorMessage);
    }


    public static void assertTextToBePresentInElement(String errorMessage, WebElement webElement, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElement(webElement, textExpected), errorMessage);
    }

    public static void assertTextToBePresentInElementValue(String errorMessage, By by, String textExpected) {
        waitUntil(ExpectedConditions.textToBePresentInElementValue(by, textExpected), errorMessage);
    }

    public static void assertAttributeContains(String errorMessage, By by, String attribute, String value) {
        waitUntil(ExpectedConditions.attributeContains(by, attribute, value),errorMessage);
    }

    public static void assertAttributeContains(String errorMessage, By by, String attribute, String value, long timeout) {
        (new WebDriverWait(driver, timeout))
                .ignoring(ClassCastException.class)
                .withMessage(errorMessage)
                .until(ExpectedConditions.attributeContains(by, attribute, value));
    }

    public static void assertTextMatches(String errorMessage, By by, Pattern pattern) {
        waitUntil(ExpectedConditions.textMatches(by, pattern),errorMessage);
    }

    public static void assertJSReturnsTrue(String errorMessage, String javascript) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(EXPECTED_CONDITION_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(EXPECTED_CONDITION_POLLING_INTERVAL))
                .withMessage(errorMessage)
                .ignoring(NoSuchElementException.class);
        wait.until(driver -> {
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            return (boolean) js.executeScript(javascript);
        });
    }

    public static void assertJSReturnsValueAndTextMatches(String errorMessage, String javascript, String textExpected) {
        String result = (String) (new WebDriverWait(driver, EXPECTED_CONDITION_TIMEOUT))
                .ignoring(ClassCastException.class)
                .withMessage(errorMessage)
                .until(ExpectedConditions.jsReturnsValue(javascript));
        org.junit.Assert.assertTrue(result.equalsIgnoreCase(textExpected));
    }

    public static void assertElementVisible(String errorMessage, By by) {
        waitWebElementUntil(ExpectedConditions.visibilityOfElementLocated(by),errorMessage);
    }

    public static void assertElementVisible(String errorMessage, WebElement el) {
        waitWebElementUntil(ExpectedConditions.visibilityOf(el),errorMessage);
    }

    public static void assertElementInvisible(String errorMessage, By by) {
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(by),errorMessage);
    }

    @Override protected void load() {}
    @Override protected void isLoaded() throws Error {}
}

