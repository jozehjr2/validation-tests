import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FlashElements;

public class SauceLoginTest {
    public static void main(String[] args) {
        // Launch Selenium WebDriver with the appropriate options
        System.setProperty("webdriver.chrome", "path/to/chromedriver");
        ChromeDriver driver = new ChromeDriver();

        // Navigate to the login page
        driver.get("https://www.saucedemo.com/v1/");

        // Verify page structure
        verifyPageStructure();

        // Test successful login
        testSuccessfulLogin();

        // Test login failure
        testLoginFailure();

        // Test UI elements
        verifyUIElements();

        driver.quit();
    }

    private static void verifyPageStructure() {
        // Find the username field
        WebElement usernameField = driver.findElement(By.id("username"));
        assertEquals("Username field not found", true, usernameField.isDisplayed());

        // Find the password field
        WebElement passwordField = driver.findElement(By.id("password"));
        assertEquals("Password field not found", true, passwordField.isDisplayed());

        // Find the login button
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(), 'Login')]"));
        assertEquals("Login button not found", true, loginButton.isDisplayed());

        // Verify labels
        WebElement acceptedUsersLabel = driver.findElement(By.id("accepted-usernames"));
        WebElement passwordForAllUsersLabel = driver.findElement(By.id("password-for-all-users"));
        assertEquals("Accepted users label not found", true, acceptedUsersLabel.isDisplayed());
        assertEquals("Password for all users label not found", true, passwordForAllUsersLabel.isDisplayed());
    }

    private static void testSuccessfulLogin() {
        // Enter valid username and password
        driver.findElement(By.id("username")).sendKeys("valid_username");
        driver.findElement(By.id("password")).sendKeys("valid_password");

        // Click the login button
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();

        // Verify redirection to another page or display of a success message
        assertTrue("Login was not successful", driver.getCurrentUrl().endsWith("success.html"));
    }

    private static void testLoginFailure() {
        // Enter invalid username and/or password
        driver.findElement(By.id("username")).sendKeys("invalid_username");
        if (!driver.findElement(By.id("password")).isDisplayed()) {
            driver.findElement(By.id("password")).sendKeys(" invalid_password");
        }

        // Click the login button
        driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();

        // Verify error message is displayed
        String errorMessage = driver.findElement(By.id("error-message")).getText();
        assertEquals("Invalid username and/or password", errorMessage);
    }

    private static void verifyUIElements() {
        // Verify the "Accepted usernames are:" column contents
        WebElement acceptedUsersColumn = driver.findElement(By.id("accepted-usernames"));
        for (int i = 0; i < 4; i++) {
            WebElement acceptedUsername = acceptedUsersColumn.findElement(By.tagName("span"));
            assertEquals("Invalid username found in Accepted usernames column", true, acceptedUsername.isDisplayed());
        }

        // Verify the "Password for all users:" column contents
        WebElement passwordForAllUsersColumn = driver.findElement(By.id("password-for-all-users"));
        assertEquals("Invalid password found in Password for all users column", true, passwordForAllUsersColumn.isDisplayed());
    }
}
/* 
In this code, we first launch Selenium WebDriver with the appropriate options and navigate to the login page using `get()` method. Then, we use various methods (e.g., `findElement()`, `sendKeys()`, `click()`, `getText()`, etc.) to interact with the elements on the page and verify their presence and contents.

For each test scenario, we define an `ExpectedCondition` that specifies the expected condition for the element(s) being verified. For example, in `testSuccessfulLogin()`, we use `By.id()` to find the username and password fields, and `By.xpath()` to find the login button. We then enter valid credentials, click the login button, and verify that the page redirects to another page or displays a success message.

In `testLoginFailure()`, we enter invalid credentials, click the login button, and verify that an error message is displayed.   

Finally, in `verifyUIElements()`, we verify that the contents of the "Accepted usernames are:" and "Password for all users:" columns match the expected values.

Note that this code assumes that the WebElement locators are defined correctly and that the page structure does not change between tests. You may need to modify the code to handle dynamic content loading or other page variations.

*/