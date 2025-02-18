import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class SwagLabsLoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://www.saucedemo.com/v1/";
    
    // Test data
    private static final List<String> VALID_USERNAMES = Arrays.asList(
        "standard_user",
        "locked_out_user",
        "problem_user",
        "performance_glitch_user"
    );
    private static final String VALID_PASSWORD = "secret_sauce";

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(BASE_URL);
    }

    @Test(priority = 1)
    public void verifyLoginPageStructure() {
        // Verify login form elements
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".btn_action"));

        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed");

        // Verify instruction sections
        WebElement usernamesSection = driver.findElement(By.className("login_credentials"));
        WebElement passwordSection = driver.findElement(By.className("login_password"));

        Assert.assertTrue(usernamesSection.getText().contains("Accepted usernames are:"), 
            "Usernames section header is missing");
        Assert.assertTrue(passwordSection.getText().contains("Password for all users:"), 
            "Password section header is missing");

        // Verify all valid usernames are listed
        String credentialsText = usernamesSection.getText();
        for (String username : VALID_USERNAMES) {
            Assert.assertTrue(credentialsText.contains(username), 
                "Username " + username + " is not listed in credentials");
        }

        // Verify password is listed
        Assert.assertTrue(passwordSection.getText().contains(VALID_PASSWORD), 
            "Valid password is not displayed");
    }

    @Test(priority = 2)
    public void testSuccessfulLogins() {
        for (String username : VALID_USERNAMES) {
            // Skip locked_out_user as it's designed to fail
            if (username.equals("locked_out_user")) continue;

            performLogin(username, VALID_PASSWORD);

            // Verify successful login
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), 
                "Login failed for user: " + username);

            // Return to login page for next test
            driver.navigate().to(BASE_URL);
        }
    }

    @Test(priority = 3)
    public void testLoginFailures() {
        // Test invalid username
        performLogin("invalid_user", VALID_PASSWORD);
        verifyErrorMessage("Epic sadface: Username and password do not match any user in this service");

        // Test invalid password
        performLogin(VALID_USERNAMES.get(0), "wrong_password");
        verifyErrorMessage("Epic sadface: Username and password do not match any user in this service");

        // Test locked out user
        performLogin("locked_out_user", VALID_PASSWORD);
        verifyErrorMessage("Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(priority = 4)
    public void testEmptyFields() {
        // Test empty username
        performLogin("", VALID_PASSWORD);
        verifyErrorMessage("Epic sadface: Username is required");

        // Test empty password
        performLogin(VALID_USERNAMES.get(0), "");
        verifyErrorMessage("Epic sadface: Password is required");

        // Test both empty
        performLogin("", "");
        verifyErrorMessage("Epic sadface: Username is required");
    }

    private void performLogin(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".btn_action"));

        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }

    private void verifyErrorMessage(String expectedMessage) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test='error']")));
        Assert.assertEquals(errorElement.getText(), expectedMessage, 
            "Error message doesn't match expected text");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}



/*
This code includes:

Setup and Configuration:

WebDriver initialization
Constant definitions for test data
Page URL configuration
Test Methods:

verifyLoginPageStructure: Validates all UI elements
testSuccessfulLogins: Tests all valid login scenarios
testLoginFailures: Tests various failure scenarios
testEmptyFields: Tests form validation
Helper Methods:

performLogin: Handles login form submission
verifyErrorMessage: Validates error messages
Best Practices:

Explicit waits for dynamic elements
Clear assertions with meaningful messages
Proper test organization and priorities
Cleanup after test execution
To use this code:

Replace "path/to/chromedriver" with your actual ChromeDriver path
Ensure you have these dependencies:
Selenium WebDriver
TestNG
ChromeDriver
Run the tests using TestNG
The tests will systematically verify:

Page structure and elements
Successful login scenarios
Various error conditions
Form validation
Error message accuracy
Each test includes appropriate assertions and error handling to ensure reliable test results.

*/