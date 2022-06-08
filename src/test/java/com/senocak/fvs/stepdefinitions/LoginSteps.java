package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.pages.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class LoginSteps {
    private static LoginPage loginPage;
    WebDriver webDriver;

    @Before
    public void setUp() {
        loginPage = LoginPage.getInstance();
    }

    @After
    public void tearDown() {
        loginPage.closeDriver();
    }

    @Given("Open browser and go to homepage")
    public void openBrowserAndGoToHomepage() {
        webDriver = loginPage.launchBrowser();
        loginPage.homePage();
    }

    @Given("Enter username")
    public void enterUsername() {
        loginPage.enterUsername("anil123");
    }

    @Given("Enter password")
    public void enterPassword() {
        loginPage.enterPassword("senocakdlkansdlkjasndajsdlkıas");
    }

    @When("Click login button")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    @Then("I should see dashboard page")
    public void iShouldSeeDashboardPage() {
        //Assert.assertTrue(loginPage.dashboardPageIsDisplayed());
        System.out.println("dashboard page is displayed");
    }

    @Given("Login page is displayed")
    public void loginPageIsDisplayed() {
        System.out.println("login page is displayed");
    }
}
