import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import java.time.Duration;
import java.util.List;

public class SwagLabsLoginTest {
    private static final String BASE_URL = "https://www.saucedemo.com/v1/";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String[] VALID_USERNAMES = {"standard_user", "locked_out_user", "problem_user", "performance_glitch_user"};
    
    public static void main(String[] args) {
        // Set up Chrome WebDriver
        System.setProperty("webdriver.chrome.driver", "path_to_chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        try {
            // Verify page structure
            verifyPageStructure(driver);
            
            // Test successful login
            testSuccessfulLogin(driver);
            
            // Test login failure
            testLoginFailure(driver);
            
            // Test error message accuracy
            testErrorMessages(driver);
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void verifyPageStructure(WebDriver driver) {
        driver.get(BASE_URL);

        // Check for the username and password fields
        assertElementPresent(driver, By.id("user-name"), "Username field");
        assertElementPresent(driver, By.id("password"), "Password field");

        // Check for the login button
        assertElementPresent(driver, By.id("login-button"), "Login button");

        // Check for "Accepted usernames are:" and "Password for all users:"
        assertElementPresent(driver, By.xpath("//h4[text()='Accepted usernames are:']"), "Accepted usernames label");
        assertElementPresent(driver, By.xpath("//h4[text()='Password for all users:']"), "Password label");

        // Check for the four usernames and one password
        List<WebElement> usernameElements = driver.findElements(By.xpath("//div[@id='login_credentials']/text()"));
        if (usernameElements.size() != 4) {
            throw new AssertionError("Expected 4 accepted usernames, found " + usernameElements.size());
        }
        WebElement passwordElement = driver.findElement(By.xpath("//div[@class='login_password']"));
        if (!passwordElement.getText().contains(VALID_PASSWORD)) {
            throw new AssertionError("Expected password not found.");
        }
    }

    private static void testSuccessfulLogin(WebDriver driver) {
        for (String username : VALID_USERNAMES) {
            driver.get(BASE_URL);
            performLogin(driver, username, VALID_PASSWORD);
            
            // Assert successful login - example placeholder assertion
            // Assuming redirection or message, adjust the locator accordingly.
            assertElementPresent(driver, By.className("success-message"), "Success message");

            driver.navigate().refresh();
        }
    }

    private static void testLoginFailure(WebDriver driver) {
        driver.get(BASE_URL);
        performLogin(driver, "invalid_user", VALID_PASSWORD);
        assertElementPresent(driver, By.className("error-message"), "Error message for invalid username");
        
        performLogin(driver, VALID_USERNAMES[0], "wrong_password");
        assertElementPresent(driver, By.className("error-message"), "Error message for invalid password");
    }

    private static void testErrorMessages(WebDriver driver) {
        driver.get(BASE_URL);
        performLogin(driver, "invalid_user", "wrong_password");
        String errorMessage = driver.findElement(By.className("error-message"))
                                    .getText();
        if (!errorMessage.equals("Invalid username or password.")) {
            throw new AssertionError("Unexpected error message: " + errorMessage);
        }
    }

    private static void performLogin(WebDriver driver, String username, String password) {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));

        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginButton.click();
    }

    private static void assertElementPresent(WebDriver driver, By locator, String elementName) {
        try {
            WebElement element = driver.findElement(locator);
            if (!element.isDisplayed()) {
                throw new AssertionError(elementName + " is not visible.");
            }
        } catch (NoSuchElementException e) {
            throw new AssertionError(elementName + " is not present.");
        }
    }
}



/*
Key Features of the Code:
Page Structure Verification: Ensures all required elements (username, password fields, and labels) are present.
Successful Login Testing: Loops through all valid usernames and verifies successful login.
Login Failure Handling: Tests with invalid credentials to check for error messages.
Error Message Verification: Ensures correct error messages for invalid inputs.
Assertions: Validates expected outcomes using assertions for each test scenario.
Make sure to update "path_to_chromedriver" with the actual path to your ChromeDriver executable.

*/