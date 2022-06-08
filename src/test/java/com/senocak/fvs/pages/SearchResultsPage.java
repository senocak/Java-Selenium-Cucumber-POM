package com.senocak.fvs.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SearchResultsPage extends Page {
    private static SearchResultsPage instance = null;
    private final By searchResults = By.id("result-stats");
    private String searchResultReference = "(//div[@class='tF2Cxc'])[%s]";

    /**
     * Singleton instance of SearchResultsPage
     * @return SearchResultsPage instance
     */
    public static SearchResultsPage getInstance() {
        if (instance == null)
            instance = new SearchResultsPage();
        return instance;
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
            log.info("Exception occured. Search Results not displayed");
            return false;
        }
    }

    /**
     * Get the search results
     * @param nthResult nth result
     * @return Map of search results
     */
    public Map<String, String> getSearchResults(String nthResult) {
        Map<String, String> searchResultDetails = new LinkedHashMap<>();
        searchResultReference = String.format(searchResultReference, nthResult);

        searchResultDetails.put("Result Title", driver.findElement(By.xpath(searchResultReference + "/div[1]/a/h3")).getText());
        searchResultDetails.put("Result Title HyperLink", driver.findElement(By.xpath(searchResultReference + "/div[1]/a")).getAttribute("href"));
        searchResultDetails.put("Result Description", driver.findElement(By.xpath(searchResultReference + "/div[2]/div/span")).getText());

        try {
            List<WebElement> resultLinks = driver.findElements(By.xpath(searchResultReference + "/div[2]//a"));
            for (int index = 0; index < resultLinks.size(); index++) {
                searchResultDetails.put("Result Additional link " + (index + 1), resultLinks.get(index).getText() + " " + resultLinks.get(index).getAttribute("href"));
            }
        } catch (Exception ex) {
            log.info("Additional Links are not shown as part of this result");
        }
        return searchResultDetails;
    }

    @Override protected void load() {}
    @Override protected void isLoaded() throws Error {}
}
