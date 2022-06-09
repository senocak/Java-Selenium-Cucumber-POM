package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.pages.GoogleHomePage;
import com.senocak.fvs.pages.LoginPage;
import com.senocak.fvs.utility.Enums.PageType;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseSteps {
    private final LoginPage loginPage = LoginPage.getInstance();
    private final GoogleHomePage googleHomePage = GoogleHomePage.getInstance();

    @Given("going to {string} page")
    public void openBrowserAndGoToHomepage(String pageType) {
        switch (PageType.fromString(pageType)) {
            case LOGIN:
                loginPage.homePage();
                break;
            case HOME:
                googleHomePage.navigateToGoogleHomePage();
                break;
            default:
                throw new IllegalArgumentException("Invalid page type");
        }
    }
}
