import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class SauceDemoLoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "https://www.saucedemo.com/v1/";

    @BeforeClass
    public void setUp() {
        // Set up the ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(baseUrl);
    }

    @Test
    public void verifyPageStructure() {
        // Verify the presence of username field
        WebElement usernameField = driver.findElement(By.id("user-name"));
        Assert.assertTrue(usernameField.isDisplayed(), "Username field is not displayed");

        // Verify the presence of password field
        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is not displayed");

        // Verify the presence of login button
        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed");

        // Verify the presence of labels
        WebElement acceptedUsernamesLabel = driver.findElement(By.xpath("//div[text()='Accepted usernames are:']"));
        Assert.assertTrue(acceptedUsernamesLabel.isDisplayed(), "Accepted usernames label is not displayed");

        WebElement passwordForAllUsersLabel = driver.findElement(By.xpath("//div[text()='Password for all users:']"));
        Assert.assertTrue(passwordForAllUsersLabel.isDisplayed(), "Password for all users label is not displayed");

        // Verify the presence of four fields under "Accepted usernames are:"
        List<WebElement> acceptedUsernames = driver.findElements(By.xpath("//div[text()='Accepted usernames are:']/following-sibling::div"));
        Assert.assertEquals(acceptedUsernames.size(), 4, "There are not 4 accepted usernames displayed");

        // Verify the presence of one field under "Password for all users:"
        List<WebElement> passwordForAllUsers = driver.findElements(By.xpath("//div[text()='Password for all users:']/following-sibling::div"));
        Assert.assertEquals(passwordForAllUsers.size(), 1, "There is not 1 password for all users displayed");
    }

    @Test
    public void testSuccessfulLogin() {
        // Retrieve valid usernames and password
        List<WebElement> acceptedUsernames = driver.findElements(By.xpath("//div[text()='Accepted usernames are:']/following-sibling::div"));
        String validPassword = driver.findElement(By.xpath("//div[text()='Password for all users:']/following-sibling::div")).getText();

        for (WebElement usernameElement : acceptedUsernames) {
            String validUsername = usernameElement.getText();

            // Perform login
            driver.findElement(By.id("user-name")).clear();
            driver.findElement(By.id("user-name")).sendKeys(validUsername);
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys(validPassword);
            driver.findElement(By.id("login-button")).click();

            // Verify successful login (assuming redirection to a new page)
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Login was not successful for username: " + validUsername);

            // Log out to test the next username
            driver.navigate().back();
        }
    }

    @Test
    public void testLoginFailure() {
        // Test with invalid username and valid password
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Verify error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container")));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed for invalid username");

        // Test with valid username and invalid password
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        // Verify error message
        errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container")));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed for invalid password");
    }

    @Test
    public void testErrorMessages() {
        // Test with invalid credentials
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();

        // Verify error message content
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container")));
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(errorMessage.getText(), expectedErrorMessage, "Error message content is incorrect");
    }

    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}

/*
Explanation:
Setup and Teardown: The setUp method initializes the WebDriver and navigates to the login page. The tearDown method closes the browser after tests are completed.
Test Methods:
verifyPageStructure: Checks the presence of all required elements on the page.
testSuccessfulLogin: Iterates over the valid usernames and performs a login with each, verifying successful login by checking the URL.
testLoginFailure: Tests invalid login attempts and verifies that an error message is displayed.
testErrorMessages: Ensures the error message content is correct when invalid credentials are used.
Assertions: Used to validate the presence of elements and the correctness of error messages.
Waits: Explicit waits are used to handle dynamic content loading, ensuring elements are present before interacting with them.
Make sure to replace "path/to/chromedriver" with the actual path to your ChromeDriver executable. Additionally, ensure that the necessary Selenium libraries are included in your project.

*/