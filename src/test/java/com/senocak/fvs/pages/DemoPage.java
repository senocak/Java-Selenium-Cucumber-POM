package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@Slf4j
public class DemoPage extends Page {
    private static final String URL = "/demo";
    private static final DemoPage instance = new DemoPage();
    private final By INPUT_NAME = By.id("demo-input-name");
    private final By INPUT_SURNAME = By.id("demo-input-surname");
    private final By INPUT_EMAIL = By.id("demo-input-email");
    private final By INPUT_COMPANY = By.id("demo-input-company");
    private final By INPUT_PHONE = By.id("demo-input-phone");
    private final By IFRAME_GOOGLE_CAPTCHA = By.xpath("//iframe[starts-with(@name, 'a-') and starts-with(@src, 'https://www.google.com/recaptcha')]");
    private final By CHECKBOX_GOOGLE_CAPTCHA = By.id("recaptcha-anchor");
    private final By BUTTON_RESET = By.id("forgot-password-btn");

    /**
     * Private constructor to prevent instantiation
     */
    private DemoPage(){}

    /**
     * Singleton instance of DemoPage
     * @return DemoPage instance
     */
    public static DemoPage getInstance(WebDriver wd) {
        driver = wd;
        return instance;
    }

    public void googleCaptcha() {
        //new WebDriverWait(driver, 10).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(IFRAME_GOOGLE_CAPTCHA));
        //new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(CHECKBOX_GOOGLE_CAPTCHA)).click();
    }

    /**
     * Redirect to Demo page
     */
    @Override protected void load() {
        get(getUrlFromConfig() + URL);
        log.debug("Loading Demo page");
    }
    /**
     * Verify page is loaded
     */
    @Override protected void isLoaded() {
        assertIsLoaded(URL);
    }
}
