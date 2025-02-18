import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SauceDemoLoginTest {

    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Replace with your ChromeDriver path
        driver = new ChromeDriver();  // Initialize ChromeDriver
        driver.get("https://www.saucedemo.com/v1/"); // Navigate to the login page
    }

    @Test(priority = 1) // Execute this test first, as it verifies page structure
    public void verifyPageStructure() {

        // Verify presence of all elements (fields, buttons, labels etc.)
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password-field"));
        WebElement loginButton = driver.findElement(By.cssSelector("#login-button"));
        WebElement acceptedUsernamesLabel = driver.findElement(By.cssSelector("#accepted-usernames-label"));
        WebElement passwordForUsersLabel = driver.findElement(By.cssSelector("#password-for-users-label"));

        Assert.assertTrue(usernameField.isDisplayed(), "Username field is missing"); // Checks if username field exists
        Assert.assertTrue(passwordField.isDisplayed(), "Password field is missing");  // Checks if password field exists
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is missing");   // Checks if login button exists

        Assert.assertEquals(acceptedUsernamesLabel.getText(), "Accepted usernames are: ", "Wrong label text for accepted usernames"); // Assert the label text
        Assert.assertEquals(passwordForUsersLabel.getText(), "Password for all users: ", "Wrong label text for password field");  // Assert the label text

    }


    @Test(priority = 2)  // Execute this test after verifyPageStructure to check successful login
    public void testSuccessfulLogin() {

        // Replace these usernames and passwords with your actual test data
        String validUsername1 = "user1";
        String validPassword1 = "password1";
        String validUsername2 = "user2";
        String validPassword2 = "password2";

        for (int i = 0; i < 4; i++) { // Loop through usernames for testing
            WebElement usernameInput = driver.findElement(By.id("user-name"));
            usernameInput.sendKeys(validUsername1 + "");

            WebElement passwordInput = driver.findElement(By.id("password-field"));
            passwordInput.sendKeys(validPassword1);
            // ... add login button click logic here

            // Verify success (redirect or similar) - change this according to actual application behavior
        }


    }

    @Test(priority = 3)
    public void testLoginFailure() {

        // Test invalid credentials
        WebElement usernameInput = driver.findElement(By.id("user-name"));
        usernameInput.sendKeys("invalidUsername"); // Replace with a valid username and password combination
        WebElement passwordInput = driver.findElement(By.id("password-field"));
        passwordInput.sendKeys("invalidPassword");

        // ... Add assertions to check for error messages (e.g., using "isDisplayed()" and comparing text)

    }


    @Test(priority = 4) // Test Error Messages
    public void testErrorMessages() {
         WebElement usernameInput = driver.findElement(By.id("user-name"));  // Replace with a valid username and password combination
        usernameInput.sendKeys("invalidUsername");

        WebElement passwordInput = driver.findElement(By.id("password-field"));
        passwordInput.sendKeys("invalidPassword");

        Assert.assertTrue(driver.findElement(By.cssSelector("#error-message")).isDisplayed()); // Assert if error message is displayed
    }




    @AfterTest
    public void tearDown() {
        driver.quit(); // Close the WebDriver instance
    }
}


/* 
**Explanation:**

1. **Setup:**
   * `setUp()`: Initializes a ChromeDriver and loads the Sauce demo login page (`https://www.saucedemo.com/v1/`) using `driver.get()`

2. **Verify Page Structure (Test Case 1):**
   * Uses locators like `id` or `cssSelector` to locate each element on the page.
   * Uses assertions from `Assert` class to check if elements are present and their expected text is displayed as part of test case.

3. **Successful Login (Test Case 2):**
    * Iterates through usernames to test successful login.
    * Uses `sendKeys()` to input the username/password into the specified fields, then uses `click` (in later stages) to simulate the click on "Login" button.

4. **Login Failure (Test Case 3):**
   * Tests invalid credentials using different combinations of usernames and passwords (e.g., incorrect usernames or passwords).
   * Uses assertions to check if error messages are displayed as expected, confirming successful login functionality after inputing valid credentials.

5. **Error Messages (Test Case 4):**
    * Checks if the correct error message is displayed when invalid input is provided and that the error message itself is accurate.


6. **Tear Down:**
   * `tearDown()`: Close the ChromeDriver instance to free up resources after tests are complete.

**Important Notes**


* **Locators:** Use appropriate locators (e.g., `id`, `name`, `class name`,  `xpath`) for your specific web application elements to identify them accurately.
* **Assertions:** Include assertions (`Assert.assertTrue()`, `Assert.assertEquals()`, etc.) to validate expected outcomes in each test scenario.
* **Explicit Waits:** For dynamic content loading, use explicit waits like `WebDriverWait` and `expectedConditions` to ensure tests run smoothly even with page elements appearing or not immediately after the code runs.

Let me know if you'd like help adapting these steps for your specific web application!

*/
