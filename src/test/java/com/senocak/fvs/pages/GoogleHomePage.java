package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import java.util.List;

@Slf4j
public class GoogleHomePage extends Page {
    private static final GoogleHomePage instance = new GoogleHomePage();
    private static final String GOOGLE_URL = "https://www.google.com/";
    private final By searchTextBox = By.name("q");
    private WebElement searchTextBoxElement;
    private final By searchResults = By.id("result-stats");

    /**
     * Private constructor to prevent instantiation
     */
    private GoogleHomePage(){}

    /**
     * Singleton instance of GoogleHomePage
     * @return instance of GoogleHomePage
     */
    public static GoogleHomePage getInstance() {
        return instance;
    }

    /**
     * Navigate to Google Home Page
     */
    public void navigateToGoogleHomePage() {
        log.info("Navigating to Google Home page");
        get(GOOGLE_URL);
    }

    /**
     * Search for a given text
     * @param searchText text to search
     */
    public void enterSearchText(String searchText) {
        log.info("Entering search text");
        searchTextBoxElement = getElement(searchTextBox);
        sendKeys(searchTextBoxElement, searchText);
    }

    /**
     * Click on search button
     */
    public void clickOnGoogleSearch() {
        log.info("Clicking on search button");
        sendKeys(searchTextBoxElement, Keys.ENTER);
    }

    /**
     * Get the boolean value of the search results is displayed or not
     * @return boolean value
     */
    public boolean isSearchResultsDisplayed() {
        try {
            log.info("Awaiting search results display");
            getVisibleElement(searchResults);
            return true;
        } catch (Exception ex) {
            log.info("Exception occurred. Search Results not displayed");
        }
        return false;
    }

    /**
     * Get the search results
     * @return List of search results
     */
    public List<WebElement> getSearchResults() {
        return getElements(By.xpath("//div[@class='g tF2Cxc']"));
    }

    @Override protected void load() {}
    @Override protected void isLoaded() throws Error {}
}
