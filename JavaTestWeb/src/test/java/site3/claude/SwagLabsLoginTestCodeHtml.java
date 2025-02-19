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
import java.util.List;

public class SwagLabsLoginTestCodeHtml {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://www.saucedemo.com/v1/";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String[] VALID_USERNAMES = {
        "standard_user",
        "locked_out_user",
        "problem_user",
        "performance_glitch_user"
    };

    @BeforeClass
    public void setUp() {
        // Initialize ChromeDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(BASE_URL);
    }

    @Test
    public void testPageStructure() {
        // Verify presence of login form elements
        Assert.assertTrue(driver.findElement(By.id("user-name")).isDisplayed(), 
            "Username field not displayed");
        Assert.assertTrue(driver.findElement(By.id("password")).isDisplayed(), 
            "Password field not displayed");
        Assert.assertTrue(driver.findElement(By.id("login-button")).isDisplayed(), 
            "Login button not displayed");

        // Verify credentials section
        WebElement credentialsSection = driver.findElement(By.className("login_credentials"));
        Assert.assertTrue(credentialsSection.getText().contains("Accepted usernames are:"),
            "Username heading not found");
        
        // Verify password section
        WebElement passwordSection = driver.findElement(By.className("login_password"));
        Assert.assertTrue(passwordSection.getText().contains("Password for all users:"),
            "Password heading not found");

        // Verify all valid usernames are displayed
        String credentialsText = credentialsSection.getText();
        for (String username : VALID_USERNAMES) {
            Assert.assertTrue(credentialsText.contains(username),
                "Username " + username + " not found in credentials list");
        }

        // Verify password is displayed
        Assert.assertTrue(passwordSection.getText().contains(VALID_PASSWORD),
            "Valid password not displayed");
    }

    @Test
    public void testSuccessfulLogin() {
        for (String username : VALID_USERNAMES) {
            // Skip locked_out_user as it's designed to fail
            if (!username.equals("locked_out_user")) {
                // Enter credentials
                driver.findElement(By.id("user-name")).clear();
                driver.findElement(By.id("user-name")).sendKeys(username);
                driver.findElement(By.id("password")).clear();
                driver.findElement(By.id("password")).sendKeys(VALID_PASSWORD);
                driver.findElement(By.id("login-button")).click();

                // Verify successful login by checking for inventory page
                wait.until(ExpectedConditions.urlContains("/inventory.html"));
                Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                    "Login failed for user: " + username);

                // Navigate back to login page for next iteration
                driver.get(BASE_URL);
            }
        }
    }

    @Test
    public void testLoginFailure() {
        String[] invalidCredentials = {
            "invalid_user",
            "",
            "standard_user"  // Valid username but will be used with invalid password
        };
        String[] invalidPasswords = {
            "wrong_password",
            "",
            "invalid_sauce"
        };

        for (int i = 0; i < invalidCredentials.length; i++) {
            // Enter invalid credentials
            driver.findElement(By.id("user-name")).clear();
            driver.findElement(By.id("user-name")).sendKeys(invalidCredentials[i]);
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys(invalidPasswords[i]);
            driver.findElement(By.id("login-button")).click();

            // Verify error message
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='error']")));
            Assert.assertTrue(errorElement.isDisplayed(), 
                "Error message not displayed for invalid credentials");
            Assert.assertFalse(errorElement.getText().isEmpty(),
                "Error message is empty");
        }
    }

    @Test
    public void testLockedOutUser() {
        // Test locked out user specifically
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys(VALID_PASSWORD);
        driver.findElement(By.id("login-button")).click();

        // Verify specific error message for locked out user
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test='error']")));
        Assert.assertTrue(errorElement.getText().contains("locked out"),
            "Locked out error message not displayed");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

/*

Test Setup:

Uses ChromeDriver (you'll need to have it installed and configured)
Implements TestNG annotations for test lifecycle management
Sets up WebDriverWait for handling dynamic elements


Test Cases:

testPageStructure: Verifies all UI elements are present and correctly displayed
testSuccessfulLogin: Tests login with all valid usernames (except locked_out_user)
testLoginFailure: Tests various invalid credential combinations
testLockedOutUser: Specifically tests the locked out user scenario


Key Features:

Uses explicit waits for handling dynamic content
Includes comprehensive assertions
Handles all test requirements from the specification
Includes proper cleanup in tearDown method



To use this test suite, you'll need:

Java installed
TestNG framework
Selenium WebDriver dependencies
ChromeDriver matching your Chrome browser version

*/