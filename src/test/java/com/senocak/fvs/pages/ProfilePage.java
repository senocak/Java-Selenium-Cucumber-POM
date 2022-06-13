package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class ProfilePage extends Page {
    private static final ProfilePage instance = new ProfilePage();
    private final By INPUT_NAME = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[1]/label/div/div[1]/div[2]/input");
    private final By INPUT_SURNAME = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[2]/label/div/div[1]/div[2]/input");
    private final By INPUT_EMAIL = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[4]/label/div/div[1]/div[2]/input");
    private final By DIV_DEFAULT_COMPANY = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[5]/label/div");
    private final By DIV_DEFAULT_LOCATION = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[6]/label/div");
    private final By BUTTON_SAVE = By.xpath("/html/body/div[1]/div/div/div/div/div/div[2]/main/div/div/div/div[2]/form/div/div/div[7]/button");

    /**
     * Private constructor to prevent instantiation
     */
    private ProfilePage(){}

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static ProfilePage getInstance(WebDriver wd) {
        driver = wd;
        return instance;
    }

    /**
     * Redirect to Login Page
     */
    public void homePage(){
        get(getUrlFromConfig() + "/profile");
    }

    /**
     * Input name
     * @param text name
     * @return ProfilePage instance
     */
    public ProfilePage enter_name(String text) {
        log.info("Entering name");
        sendKeys(getElement(INPUT_NAME), text);
        return this;
    }

    /**
     * Input surname
     * @param text surname
     * @return ProfilePage instance
     */
    public ProfilePage enter_surname(String text) {
        log.info("Entering name");
        sendKeys(getElement(INPUT_SURNAME), text);
        return this;
    }

    /**
     * Input email
     * @param text email
     * @return ProfilePage instance
     */
    public ProfilePage enter_email(String text) {
        log.info("Entering name");
        sendKeys(getElement(INPUT_EMAIL), text);
        return this;
    }

    /**
     * Input email
     * @param text email
     * @return ProfilePage instance
     */
    public ProfilePage select_company(String text) {
        log.info("Entering name");
        click(getElement(DIV_DEFAULT_COMPANY));
        click(getElementByContainsText(text));
        return this;
    }

    /**
     * Input email
     * @param text email
     * @return ProfilePage instance
     */
    public ProfilePage select_location(String text) {
        log.info("Entering name");
        click(getElement(DIV_DEFAULT_LOCATION));
        click(getElementByContainsText(text));
        return this;
    }

    /**
     * Click on save button
     */
    public ProfilePage click_button_save() {
        log.info("Clicking on save button");
        click(getElement(BUTTON_SAVE));
        return this;
    }

    @Override protected void load() {
        log.debug("Loading ForgotPasswordPage");
    }
    @Override protected void isLoaded() {
        log.debug("ForgotPasswordPage is loaded");
    }
}
