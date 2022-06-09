package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.pages.GoogleHomePage;
import com.senocak.fvs.webdriver.DriverManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

@Slf4j
public class GoogleSteps {
    private GoogleHomePage googleHomePage;

    @Before
    public void setup() {
        log.info("Setting up web driver");
        DriverManager.getDriver();
        googleHomePage = GoogleHomePage.getInstance();
    }

    @And("User enters search text as {string}")
    public void userEntersSearchTextAs(String searchText) {
        googleHomePage.enterSearchText(searchText);
    }

    @When("User clicks on Google Search button")
    public void userClicksOnGoogleSearchButton() {
        googleHomePage.clickOnGoogleSearch();
    }

    @Then("Search Results is displayed")
    public void searchResultsIsDisplayed() {
        Assert.assertTrue("Search Results are not displayed", googleHomePage.isSearchResultsDisplayed());
    }

    @And("Test retrieves details of {string} result")
    public void testRetrievesResultsDetails(String total) {
        Assert.assertEquals(Integer.parseInt(total), googleHomePage.getSearchResults().size());
    }
}
