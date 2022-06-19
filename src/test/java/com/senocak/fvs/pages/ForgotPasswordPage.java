package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class ForgotPasswordPage extends Page {
    private static final String URL = "/forgot-password";
    private static final ForgotPasswordPage instance = new ForgotPasswordPage();
    private final By INPUT_EMAIL = By.id("forgot-password-email");
    private final By BUTTON_RESET = By.id("forgot-password-btn");

    /**
     * Private constructor to prevent instantiation
     */
    private ForgotPasswordPage(){}

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static ForgotPasswordPage getInstance(WebDriver wd) {
        driver = wd;
        return instance;
    }

    /**
     * Input username
     * @param text username
     */
    public void enterEmail(String text) {
        log.info("Entering username");
        sendKeys(getElement(INPUT_EMAIL), text);
    }

    /**
     * Click on login button
     */
    public void click_send_reset() {
        log.info("Clicking on login button");
        click(getElement(BUTTON_RESET));
    }

    /**
     * Redirect to Forgot Password page
     */
    @Override protected void load() {
        get(getUrlFromConfig() + URL);
        log.debug("Loading Forgot Password page");
    }
    /**
     * Verify page is loaded
     */
    @Override protected void isLoaded() {
        assertIsLoaded(URL);
    }
}
