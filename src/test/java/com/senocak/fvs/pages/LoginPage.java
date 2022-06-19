package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class LoginPage extends Page {
    private static final String URL = "/login";
    private static final LoginPage instance = new LoginPage();
    private final By IMG_LOGIN = By.id("login-logo");
    private final By INPUT_EMAIL = By.id("login-input-email");
    private final By INPUT_PASSWORD = By.id("login-input-password");
    private final By BUTTON_LOGIN = By.id("login-button");

    /**
     * Private constructor to prevent instantiation
     */
    private LoginPage(){}

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static LoginPage getInstance(WebDriver wd) {
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
     * Input username
     * @param text username
     */
    public void enterPassword(String text) {
        log.info("Entering password");
        sendKeys(getElement(INPUT_PASSWORD), text);
    }

    /**
     * Click on login button
     */
    public void clickLogin() {
        log.info("Clicking on login button");
        click(getElement(BUTTON_LOGIN));
    }

    /**
     * Redirect to Login page
     */
    @Override protected void load() {
        get(getUrlFromConfig() + URL);
        log.debug("Loading Login page");
    }
    /**
     * Verify page is loaded
     */
    @Override protected void isLoaded() {
        assertIsLoaded(URL);
        isDisplayed(getElement(IMG_LOGIN));
    }
}
