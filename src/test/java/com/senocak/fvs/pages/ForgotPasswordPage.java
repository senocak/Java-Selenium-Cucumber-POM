package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class ForgotPasswordPage extends Page {
    private static final ForgotPasswordPage instance = new ForgotPasswordPage();
    private final By INPUT_EMAIL = By.xpath("/html/body/div[1]/div/div/div/div/form/div[1]/label/div/div[1]/div[2]/input");
    private final By BUTTON_RESET = By.xpath("/html/body/div[1]/div/div/div/div/form/div[2]/button");

    /**
     * Private constructor to prevent instantiation
     */
    private ForgotPasswordPage(){}

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static ForgotPasswordPage getInstance() {
        return instance;
    }

    /**
     * Redirect to Login Page
     */
    public void homePage(){
        get(getUrlFromConfig() + "/forgot-password");
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

    @Override protected void load() {
        log.debug("Loading ForgotPasswordPage");
    }
    @Override protected void isLoaded() {
        log.debug("ForgotPasswordPage is loaded");
    }
}
