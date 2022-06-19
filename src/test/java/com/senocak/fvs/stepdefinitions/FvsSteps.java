package com.senocak.fvs.stepdefinitions;

import com.senocak.fvs.config.User;
import com.senocak.fvs.pages.DemoPage;
import com.senocak.fvs.pages.ForgotPasswordPage;
import com.senocak.fvs.pages.LoginPage;
import com.senocak.fvs.pages.Page;
import com.senocak.fvs.pages.ProfilePage;
import com.senocak.fvs.utility.Constants;
import com.senocak.fvs.utility.Enums.PageType;
import com.senocak.fvs.utility.zulip.ZulipClient;
import com.senocak.fvs.webdriver.DriverManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import java.util.List;
import java.util.Map;

@Slf4j
public class FvsSteps {
    private final DriverManager driverManager = DriverManager.getInstance();
    private static WebDriver driver;
    private LoginPage loginPage;
    private ForgotPasswordPage forgotPasswordPage;
    private ProfilePage profilePage;
    private DemoPage demoPage;
    private ZulipClient zulipClient;

    @Before
    public void setup() {
        log.info("Setting up web driver");
        driver = driverManager.getDriver();
        loginPage = LoginPage.getInstance(driver);
        forgotPasswordPage = ForgotPasswordPage.getInstance(driver);
        profilePage = ProfilePage.getInstance(driver);
        demoPage = DemoPage.getInstance(driver);
        zulipClient = ZulipClient.getInstance();
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("Tearing down web driver");
        if (scenario.isFailed()) {
            log.debug("Scenario failed, taking screenshot");
            scenario.attach(driverManager.getScreenshot(), "image/png", "Screenshot");
        }
        //zulipClient.createReport(scenario);
        driverManager.getLogs();
        driverManager.closeDriver();
    }

    // Generic steps
    @Given("open {string} page")
    public void open_login_page(String page) {
        switch (PageType.fromString(page)) {
            case LOGIN:
                loginPage.get();
                break;
            case FORGOT_PASSWORD:
                forgotPasswordPage.get();
                break;
            case PROFILE:
                profilePage.get();
                break;
            case DEMO:
                demoPage.get();
                break;
            default:
                Assert.fail("Unknown page type: " + page);
        }
    }

    // Login page
    @Given("Enter email and password")
    public void enter_email_and_password(DataTable dataTable) {
        List<Map<String,String>> dataRow = dataTable.asMaps(String.class,String.class);
        if (dataRow.size() != 1) {
            log.error("Only one row is allowed. Size: {}", dataRow.size());
            throw new RuntimeException("Only one row is allowed. Size: " + dataRow.size());
        }
        String email = dataRow.get(0).get("email");
        User extractedUser = Constants.extractUserFromString(email);
        email = extractedUser != null ? extractedUser.getEmail() : email;
        loginPage.enterEmail(email);

        String password = dataRow.get(0).get("password");
        User extractedPassword = Constants.extractUserFromString(password);
        password = extractedPassword != null ? extractedPassword.getPassword() : password;
        loginPage.enterPassword(password);
    }

    @When("Click login button")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    // Forgot password page
    @Given("Enter {string} for reset password")
    public void enterForResetPassword(String arg0) {
        User extracted = Constants.extractUserFromString(arg0);
        if (extracted != null) {
            arg0 = extracted.getEmail();
        }
        forgotPasswordPage.enterEmail(arg0);
    }

    @When("Click send reset button")
    public void clickSendResetButton() {
        forgotPasswordPage.click_send_reset();
    }

    // Profile page
    @When("I update my profile")
    public void iUpdateMyProfile(DataTable dataTable) {
        List<Map<String,String>> dataRow = dataTable.asMaps(String.class,String.class);
        if (dataRow.size() != 1) {
            log.error("Only one row is allowed. Size: {}", dataRow.size());
            throw new RuntimeException("Only one row is allowed. Size: " + dataRow.size());
        }
        String name = dataRow.get(0).get("name");
        User extractedName = Constants.extractUserFromString(name);
        name = extractedName != null ? extractedName.getName() : name;

        String surname = dataRow.get(0).get("surname");
        User extractedSurname = Constants.extractUserFromString(surname);
        surname = extractedSurname != null ? extractedSurname.getSurname() : surname;

        String email = dataRow.get(0).get("email");
        User extractedUser = Constants.extractUserFromString(email);
        email = extractedUser != null ? extractedUser.getEmail() : email;

        String company = dataRow.get(0).get("company");
        User extractedCompany = Constants.extractUserFromString(company);
        company = extractedCompany != null ? extractedCompany.getCompany() : company;

        String location = dataRow.get(0).get("location");
        User extractedLocation = Constants.extractUserFromString(location);
        location = extractedLocation != null ? extractedLocation.getLocation() : location;

        profilePage
                .enter_name(name)
                .enter_surname(surname)
                .enter_email(email)
                .select_company(company)
                .select_location(location)
                .click_button_save();
    }

    // Assertions
    @Then("I should see {string} message")
    public void i_should_see_message(String arg0) {
        Page p = loginPage != null ? loginPage:
            forgotPasswordPage != null ? forgotPasswordPage:
            profilePage != null ? profilePage: null;
        if (p == null) throw new RuntimeException("Page not found");

        Assert.assertTrue(p.verifyPopupMessage(arg0));
    }
}
