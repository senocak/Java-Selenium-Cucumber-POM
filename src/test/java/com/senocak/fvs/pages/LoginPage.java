package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

@Slf4j
public class LoginPage extends Page {
    private static final LoginPage instance = new LoginPage();
    private final By INPUT_USERNAME = By.xpath("/html/body/div[1]/div/div/div/div/form/label[1]/div/div[1]/div[2]/input");
    private final By INPUT_PASSWORD = By.xpath("/html/body/div[1]/div/div/div/div/form/label[2]/div/div[1]/div[2]/input");
    private final By BUTTON_LOGIN = By.xpath("/html/body/div[1]/div/div/div/div/form/div[3]/button");

    /**
     * Private constructor to prevent instantiation
     */
    private LoginPage(){}

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static LoginPage getInstance() {
        return instance;
    }

    /**
     * Redirect to Login Page
     */
    public void homePage(){
        get(getUrlFromConfig() + "/login");
    }

    /**
     * Input username
     * @param text username
     */
    public void enterEmail(String text) {
        log.info("Entering username");
        sendKeys(getElement(INPUT_USERNAME), text);
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
     * Verify if alert is displayed
     * @param arg0 alert text
     * @return true if alert is displayed
     */
    public boolean verifyPopupMessage(String arg0) {
        return isDisplayed(By.xpath("//div[contains(string(), '"+arg0+"')]"));
    }

    @Override protected void load() {
        log.debug("Loading LoginPage");
    }
    @Override protected void isLoaded() {
        log.debug("LoginPage is loaded");
    }
}
