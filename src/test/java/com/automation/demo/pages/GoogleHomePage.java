package com.automation.demo.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

@Slf4j
public class GoogleHomePage extends Page{
    private static GoogleHomePage instance = null;
    private static final String GOOGLE_URL = "https://www.google.com/";
    private final By searchTextBox = By.name("q");
    private final By searchButton = By.xpath("//span[@class='QCzoEc z1asCe MZy1Rb']//ancestor::div[5]//div[@class='FPdoLc lJ9FBc']//input[@value='Google Search']");
    private WebElement searchTextBoxElement;

    public static GoogleHomePage getInstance() {
        if (instance == null) instance = new GoogleHomePage();
        return instance;
    }

    public void navigateToGoogleHomePage() {
        log.info("Navigating to Google Home page");
        get(GOOGLE_URL);
    }

    public void enterSearchText(String searchText) {
        log.info("Entering search text");
        searchTextBoxElement = getElement(searchTextBox);
        sendKeys(searchTextBoxElement, searchText);
    }

    public void clickOnGoogleSearch(){
        log.info("Click ENTER");
        sendKeys(searchTextBoxElement, Keys.ENTER);
    }
    @Override protected void load() {}
    @Override protected void isLoaded() throws Error {}
}
