package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.pages.ForgotPasswordPage;
import com.senocak.fvs.pages.LoginPage;
import com.senocak.fvs.webdriver.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import java.util.List;
import java.util.Map;

@Slf4j
public class AuthSteps {
    private LoginPage loginPage;
    private ForgotPasswordPage forgotPasswordPage;

    @Before
    public void setup() {
        log.info("Setting up web driver");
        DriverManager.getDriver();
        loginPage = LoginPage.getInstance();
        forgotPasswordPage = ForgotPasswordPage.getInstance();
    }

    @After
    public void tearDown() {
        log.info("Tearing down web driver");
        //DriverManager.closeDriver();
    }

    @Given("open login page")
    public void open_login_page() {
        loginPage.homePage();
    }

    @Given("Enter email and password")
    public void enter_email_and_password(DataTable dataTable) {
        List<Map<String,String>> dataRow = dataTable.asMaps(String.class,String.class);
        if (dataRow.size() != 1) {
            log.error("Only one row is allowed. Size: {}", dataRow.size());
            throw new RuntimeException("Only one row is allowed");
        }
        loginPage.enterEmail(dataRow.get(0).get("email"));
        loginPage.enterPassword(dataRow.get(0).get("password"));
    }

    @When("Click login button")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    @Then("I should see {string} message")
    public void i_should_see_message(String arg0) {
        Assert.assertTrue(loginPage.verifyPopupMessage(arg0));
    }

    @Given("open forgot password page")
    public void openForgotPasswordPage() {
        forgotPasswordPage.homePage();
    }

    @Given("Enter {string} for reset password")
    public void enterForResetPassword(String arg0) {
        forgotPasswordPage.enterEmail(arg0);
    }

    @When("Click send reset button")
    public void clickSendResetButton() {
        forgotPasswordPage.click_send_reset();
    }
}
