package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class ProfilePage extends Page {
    private static final String URL = "/profile";
    private static final ProfilePage instance = new ProfilePage();
    private final By INPUT_NAME = By.id("profile-input-name");
    private final By INPUT_SURNAME = By.id("profile-input-surname");
    private final By INPUT_PASSWORD = By.id("profile-input-password");
    private final By INPUT_EMAIL = By.id("profile-input-email");
    private final By DIV_DEFAULT_COMPANY = By.id("profile-input-company");
    private final By DIV_DEFAULT_LOCATION = By.id("profile-input-location");
    private final By BUTTON_SAVE = By.id("profile-save-btn");

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

    /**
     * Redirect to Profile page
     */
    @Override protected void load() {
        get(getUrlFromConfig() + URL);
        log.debug("Loading Profile page");
    }
    /**
     * Verify page is loaded
     */
    @Override protected void isLoaded() {
        assertIsLoaded(URL);
    }
}
