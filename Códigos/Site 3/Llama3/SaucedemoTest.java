import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SaucedemoTest {
    public static void main(String[] args) {
        // Set up the WebDriver
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to the login page
            driver.get("https://www.saucedemo.com/v1/");

            // Test Page Structure
            verifyPageStructure(driver);

            // Test Successful Login
            testSuccessfulLogin(driver);

            // Test Login Failure
            testLoginFailure(driver);

            // Test Error Messages
            testErrorMessage(driver);

            // Test UI Elements
            testUIElements(driver);
        } finally {
            driver.quit();
        }
    }

    private static void verifyPageStructure(WebDriver driver) {
        WebElement usernameField = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordField = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        WebElement loginButton = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));
        WebElement acceptedUsernamesColumn = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='column-left']")));
        WebElement passwordForAllUsersColumn = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='column-right']")));

        // Verify the presence of expected elements
        if (!usernameField.isDisplayed()) {
            throw new AssertionError("Username field not found");
        }
        if (!passwordField.isDisplayed()) {
            throw new AssertionError("Password field not found");
        }
        if (!loginButton.isDisplayed()) {
            throw new AssertionError("Login button not found");
        }
        if (!acceptedUsernamesColumn.isDisplayed()) {
            throw new AssertionError("Accepted usernames column not found");
        }
        if (!passwordForAllUsersColumn.isDisplayed()) {
            throw new AssertionError("Password for all users column not found");
        }

        // Verify the number of fields in the accepted usernames column
        int numFields = acceptedUsernamesColumn.findElements(By.tagName("input")).size();
        if (numFields != 4) {
            throw new AssertionError("Incorrect number of accepted usernames");
        }
    }

    private static void testSuccessfulLogin(WebDriver driver) {
        // Use each of the 4 accepted usernames along with the valid password to perform a successful login
        String[] acceptedUsernames = {"standard_user", "problem_user", "performance_basic_user", "performance_glitchy_user"};   
        for (String username : acceptedUsernames) {
            WebElement usernameField = driver.findElement(By.id("username"));
            usernameField.clear();
            usernameField.sendKeys(username);
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys("secret_sauce");
            WebElement loginButton = driver.findElement(By.name("login"));
            loginButton.click();

            // Verify that login is completed successfully
            new WebDriverWait(driver, 10).until(ExpectedConditions.urlContains("/inventory.html"));
        }
    }

    private static void testLoginFailure(WebDriver driver) {
        // Test with invalid usernames and/or invalid passwords to ensure that an error message is displayed
        String[] invalidUsernames = {"invalid_user1", "invalid_user2"};
        for (String username : invalidUsernames) {
            WebElement usernameField = driver.findElement(By.id("username"));
            usernameField.clear();
            usernameField.sendKeys(username);
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys("secret_sauce");
            WebElement loginButton = driver.findElement(By.name("login"));
            loginButton.click();

            // Verify that an error message is displayed
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='error-message']")));
        }
    }

    private static void testErrorMessage(WebDriver driver) {
        // Test Error Messages
        String[] invalidCredentials = {"standard_user", "wrong_password"};
        for (String[] credentials : invalidCredentials) {
            WebElement usernameField = driver.findElement(By.id("username"));
            usernameField.clear();
            usernameField.sendKeys(credentials[0]);
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.clear();
            passwordField.sendKeys(credentials[1]);
            WebElement loginButton = driver.findElement(By.name("login"));
            loginButton.click();

            // Verify that the error message displayed is accurate and clear
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='error-message']")));
        }
    }

    private static void testUIElements(WebDriver driver) {
        // Test UI Elements
        WebElement acceptedUsernamesColumn = driver.findElement(By.xpath("//div[@class='column-left']"));
        String[] expectedAcceptedUsernames = {"standard_user", "problem_user", "performance_basic_user", "performance_glitchy_user"};
        for (String username : expectedAcceptedUsernames) {
            boolean found = false;
            for (WebElement field : acceptedUsernamesColumn.findElements(By.tagName("input"))) {
                if (field.getAttribute("value").equals(username)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new AssertionError("Expected username not found");
            }
        }

        WebElement passwordForAllUsersColumn = driver.findElement(By.xpath("//div[@class='column-right']"));
        String expectedPassword = "secret_sauce";
        boolean found = false;
        for (WebElement field : passwordForAllUsersColumn.findElements(By.tagName("input"))) {
            if (field.getAttribute("value").equals(expectedPassword)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new AssertionError("Expected password not found");
        }
    }
}

/*
Note that this is just a sample code and you may need to modify it to fit your specific use case. Additionally, you should replace the `System.setProperty` line with the actual path to the ChromeDriver executable on your system.

Also, please note that this test code is not exhaustive and you may want to add more tests based on your specific requirements.
 */