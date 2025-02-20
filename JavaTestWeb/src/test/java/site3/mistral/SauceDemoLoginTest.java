package site3.mistral;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoLoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("https://www.saucedemo.com/v1");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Verify Page Structure
    @Test(description = "Verify the presence of all login page elements")
    public void verifyPageStructure() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        WebElement acceptedUsernamesLabel = driver.findElement(By.cssSelector("#login_column > div:nth-child(1) > div > h5"));
        WebElement passwordLabel = driver.findElement(By.xpath("//h6[contains(text(), 'Password')]"));

        List<WebElement> usernamesFields = driver.findElements(By.cssSelector("#login_column > div:nth-child(1) > div > div > div > div"));
        List<WebElement> passwordFieldUnderColumn = driver.findElements(By.xpath("//div[contains(@class, 'password')]"));

        Assert.assertEquals(usernameField.isDisplayed(), true);
        Assert.assertEquals(passwordField.isDisplayed(), true);
        Assert.assertEquals(loginButton.isDisplayed(), true);
        Assert.assertEquals(acceptedUsernamesLabel.isDisplayed(), true);
        Assert.assertEquals(passwordLabel.isDisplayed(), true);
        Assert.assertEquals(usernamesFields.size(), 4);
        Assert.assertEquals(passwordFieldUnderColumn.size(), 1);
    }

    // Test Successful Login
    @Test(description = "Login with valid usernames and password")
    public void testSuccessfulLogin() {
        String validUsername = "standard_user";
        String validPassword = "secret_sauce";

        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.sendKeys(validUsername);
        passwordField.sendKeys(validPassword);
        loginButton.click();

        // Since the next page doesn't load immediately, use implicit wait to avoid TimeoutException
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String title = driver.getTitle();
        Assert.assertEquals(title, "Products");
    }

    // Test Login Failure
    @Test(description = "Login with invalid usernames and password")
    public void testLoginFailure() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        String invalidUsername = "non_existing_user";
        String validPassword = "secret_sauce";

        usernameField.sendKeys(invalidUsername);
        passwordField.sendKeys(validPassword);
        loginButton.click();

        // Wait for error message to appear and check its text
        WebElement errorMessage = driver.findElement(By.cssSelector("#login_container > div > form > div > p"));
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        Assert.assertEquals(errorMessage.getText(), expectedErrorMessage);
    }
}


/* 
This code uses TestNG annotations to organize the tests, FirefoxDriver for web browsing, and WebElement methods to interact with the page elements. It covers all the test cases specified in the requirements, including verifying page structure, testing successful login, handling login failure, checking error messages, and validating UI elements.

*/
