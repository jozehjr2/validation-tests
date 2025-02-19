package site3.claude;

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

public class SauceDemoLoginTestDescribedImage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://www.saucedemo.com/v1/";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final List<String> VALID_USERNAMES = Arrays.asList(
            "standard_user",
            "locked_out_user",
            "problem_user",
            "performance_glitch_user"
    );

    @BeforeClass
    public void setUp() {
        // Initialize WebDriver and configuration
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(BASE_URL);
    }

    @Test(priority = 1)
    public void verifyPageStructure() {
        // Test login form elements
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed");

        // Test instruction labels
        WebElement usernamesLabel = driver.findElement(By.xpath("//div[contains(text(),'Accepted usernames are:')]"));
        WebElement passwordLabel = driver.findElement(By.xpath("//div[contains(text(),'Password for all users:')]"));

        Assert.assertTrue(usernamesLabel.isDisplayed(), "Usernames label is not displayed");
        Assert.assertTrue(passwordLabel.isDisplayed(), "Password label is not displayed");

        // Verify username list
        List<WebElement> usernamesList = driver.findElements(By.cssSelector("div.login_credentials > text"));
        Assert.assertEquals(usernamesList.size(), VALID_USERNAMES.size(), 
            "Number of displayed usernames doesn't match expected count");

        // Verify password field
        WebElement passwordInfo = driver.findElement(By.cssSelector("div.login_password"));
        Assert.assertTrue(passwordInfo.getText().contains(VALID_PASSWORD), 
            "Valid password is not displayed correctly");
    }

    @Test(priority = 2)
    public void testSuccessfulLogin() {
        for (String username : VALID_USERNAMES) {
            // Skip locked_out_user as it's designed to fail
            if (username.equals("locked_out_user")) continue;

            // Perform login
            performLogin(username, VALID_PASSWORD);

            // Verify successful login
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), 
                "Login failed for username: " + username);

            // Return to login page for next iteration
            driver.navigate().to(BASE_URL);
        }
    }

    @Test(priority = 3)
    public void testLoginFailure() {
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
    public void testEmptyCredentials() {
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

    @Test(priority = 5)
    public void verifyInstructionalContent() {
        // Verify username instructions
        WebElement credentialsSection = driver.findElement(By.className("login_credentials"));
        for (String username : VALID_USERNAMES) {
            Assert.assertTrue(credentialsSection.getText().contains(username), 
                "Username " + username + " not found in instructions");
        }

        // Verify password instructions
        WebElement passwordSection = driver.findElement(By.className("login_password"));
        Assert.assertTrue(passwordSection.getText().contains(VALID_PASSWORD), 
            "Valid password not found in instructions");
    }

    private void performLogin(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

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

Proper Setup and Teardown:

WebDriver initialization
Window maximization
Implicit and explicit waits
Browser cleanup
Comprehensive Test Cases:

Page structure verification
Successful login with all valid usernames
Various failure scenarios
Empty credential handling
Instructional content verification
Helper Methods:

performLogin: Encapsulates login functionality
verifyErrorMessage: Handles error message verification
Best Practices:

Constant definitions for reusable values
Clear test priorities
Comprehensive assertions
Error handling
Clean code structure with comments
TestNG Annotations:

@BeforeClass for one-time setup
@BeforeMethod for per-test setup
@Test with priorities
@AfterClass for cleanup
To use this code:

Replace "path/to/chromedriver" with your actual ChromeDriver path
Ensure you have the following dependencies in your project:
Selenium WebDriver
TestNG
ChromeDriver
Run the tests using TestNG
The tests will execute in order and provide detailed feedback about any failures that occur.

*/