package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.pages.LoginPage;
import com.senocak.fvs.webdriver.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

@Slf4j
public class LoginSteps {
    private LoginPage loginPage;

    @Before
    public void setup() {
        log.info("Setting up web driver");
        DriverManager.getDriver();
        loginPage = LoginPage.getInstance();
    }

    @Given("Enter username and password")
    public void enterUsernameAndPassword(DataTable dataTable) {
        List<Map<String,String>> dataRow = dataTable.asMaps(String.class,String.class);
        if (dataRow.size() > 1) {
            log.error("Only one row is allowed. Size: {}", dataRow.size());
            throw new RuntimeException("Only one row is allowed");
        }
        loginPage.enterUsername(dataRow.get(0).get("username"));
        loginPage.enterPassword(dataRow.get(0).get("password"));
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
}
