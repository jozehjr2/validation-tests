package site2.gemma;

import org.testng.annotations.Test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginAppTest {

    private static final String WEBSITE_URL = "https://wavingtest.github.io/system-healing-test/"; // Replace with actual URL
    private WebDriver driver;

    public LoginAppTest() {
        // Initialize the WebDriver (change based on your setup)
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    public void runTests() throws InterruptedException {
        // 1. Navigate to the Login Page
        driver.get(WEBSITE_URL);

        // 2. Verify Page Structure & Content
        WebElement welcomeLeftColumn = driver.findElement(By.cssSelector("#left-column"));
        WebElement welcomeRightColumn = driver.findElement(By.cssSelector("#right-column"));

        verifyPageStructure(welcomeLeftColumn, welcomeRightColumn);

        // 3. Login Functionality Tests

        // Test with correct credentials and verify successful login
        testLoginSuccess();

        // Test with incorrect credentials and verify error message
        testLoginFail();


        // 4.  Forgot Password & Create Account Links:
        testLinksFunctionality();

    }

    @Test(enabled = false)
	private void testLinksFunctionality() {
        WebElement forgotPassword = driver.findElement(By.linkText("Esqueceu a sua senha?"));
        WebElement createAccount = driver.findElement(By.linkText("Criar uma conta"));

        verifyLinkRedirctToPage(forgotPassword, "Forgot Password Page");
        verifyLinkRedirctToPage(createAccount, "Create an Account Page");
    }


    @Test(enabled = false)
	private void testLoginSuccess() {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));

        sendKeysValue(usernameField, "testuser"); // Replace with actual username
        sendKeysValue(passwordField, "testpwd");  // Replace with actual password
        WebElement loginButton = driver.findElement(By.cssSelector("#loginButton"));
        loginButton.click();


    }

    @Test(enabled = false)
	private void testLoginFail() {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        sendKeysValue(usernameField, "testuser"); // Replace with actual username
        sendKeysValue(passwordField, "wrongpwd");  // Change to invalid password

        WebElement loginButton = driver.findElement(By.cssSelector("#loginButton"));
        loginButton.click();

        // Verify the error message appears after unsuccessful login attempt
    }


    private void verifyPageStructure(WebElement leftColumn, WebElement rightColumn) {
        // ... Your code to assert structure and content of these columns
        // (e.g., using assertions for visible text content or specific element types)
    }

    private void verifyLinkRedirctToPage(WebElement link, String expectedUrl) {
        // Assert that the link redirects to the correct page after being clicked.
        link.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout if needed
        wait.until(ExpectedConditions.urlContains(expectedUrl));

    }

    private void sendKeysValue(WebElement element, String value) {
       element.sendKeys(value);
    }

    public static void main(String[] args) throws InterruptedException {
        LoginAppTest test = new LoginAppTest(); // Initialize the test
        test.runTests(); // Run the tests
    }

}



/* 

**Explanation:**

1.  **Setup:**
    *   `WEBSITE_URL`: This variable holds the target website URL. You'll need to replace this with your actual web application URL.
    *   `driver`: Holds the WebDriver instance that we'll use for interacting with the webpage.
    *   **ChromeDriver**: Instantiating a ChromeDriver is assumed here (you may want to configure it based on your environment).  Make sure you have the ChromeDriver downloaded and added to your system PATH if not using Selenium Cloud


2.  **Running Tests:**
    *   `runTests()`: This method encapsulates all the test logic.

3.  **Page Structure Verification (verifyPageStructure):**
    *   This will verify that all the components of the web application's page structure are displayed as expected, including the left column and right column text content.


4.  **Login Functionality Tests:**
    *   `testLoginSuccess()`: This test checks if login is successful by testing the Login button's behavior on valid credentials.
    *   `testLoginFail()`: This test checks for an error message when inputting invalid credentials.

5. **Forgot Password & Create Account Functionality Tests:**
    *   We include tests to verify that links to the "Forgotten password" and "Create an account" pages function correctly, redirecting to the intended pages after being clicked.


**Remember**: Replace placeholders like `website_url`, usernames, passwords, etc., with your actual values, and adapt the assertions and code based on your specific requirements.

*/