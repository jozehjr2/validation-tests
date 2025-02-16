import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BugBankLoginTest {

    // URL of the web application
    public static final String BUG_BANK_URL = "https://bugbank.netlify.app/";

    // Locators for the login page elements
    private static final By EMAIL_FIELD = By.cssSelector("#email");
    private static final By PASSWORD_FIELD = By.cssSelector("#password");
    private static final By ACCESS_BUTTON = By.xpath("//button[contains(@href,'login')]");
    private static final By REGISTRAR_BUTTON = By.cssSelector("#registrar");

    // Test to verify the page structure is correct
    public void testPageStructure() {
        WebDriver driver = createDriver();
        driver.get(BUG_BANK_URL);

        // Verify the left column text
        WebElement leftColumn = driver.findElement(By.cssSelector(".left-column"));
        assertEquals("BugBank. The bank with bugs and flaws your way. Make transfers and payments with bugs and practice testing successfully in a near-real scenario!", leftColumn.getText());

        // Verify the right column elements
        WebElement rightColumn = driver.findElement(By.cssSelector(".right-column"));
        assertEquals("Email", rightColumn.findElement(By.tagName("input")).getAttribute("name"));
        assertEquals("Password", rightColumn.findElement(By.tagName("input")).getAttribute("name"));
        assertEquals("Access", rightColumn.findElement(By.tagName("button")).getAttribute("href"));
    }

    // Test to verify successful login with valid email and password
    public void testSuccessfulLogin() {
        WebDriver driver = createDriver();
        driver.get(BUG_BANK_URL);

        // Enter valid email and password
        driver.findElement(By.cssSelector(EMAIL_FIELD)).sendKeys("valid.email@example.com");
        driver.findElement(By.cssSelector(PASSWORD_FIELD)).sendKeys("password123");

        // Click the "Access" button
        driver.findElement(By.xpath(ACCESS_BUTTON)).click();

        // Verify login success
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement loggedInUser = wait.until(ExpectedCondition.textToBePresentInElement(By.xpath("//div[contains(@class, 'logged-in')]"), "Logged in"));
        assertTrue(loggedInUser.isDisplayed());
    }

    // Test to verify login failure with invalid email or password
    public void testLoginFailure() {
        WebDriver driver = createDriver();
        driver.get(BUG_BANK_URL);

        // Enter invalid email or password
        if (driver.findElement(By.cssSelector(EMAIL_FIELD)).getAttribute("value") != null) {
            driver.findElement(By.cssSelector(EMAIL_FIELD)).sendKeys("InvalidEmail");
        } else {
            driver.findElement(By.cssSelector(PASSWORD_FIELD)).sendKeys("InvalidPassword");
        }

        // Click the "Access" button
        driver.findElement(By.xpath(ACCESS_BUTTON)).click();

        // Verify login failure
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement errorMessage = wait.until(ExpectedCondition.textToBePresentInElement(By.xpath("//div[contains(@class, 'error')]"), "Invalid email or password"));
        assertTrue(errorMessage.isDisplayed());
    }

    // Test to verify redirection to registration page or modal when clicking the Registrar button
    public void testRegistration() {
        WebDriver driver = createDriver();
        driver.get(BUG_BANK_URL);

        // Click the Registrar button
        driver.findElement(By.cssSelector(REGISTRAR_BUTTON)).click();

        // Verify redirection to registration page or modal
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement registrationPageOrModal = wait.until(ExpectedCondition.titleIs("Registration"));
        assertTrue(registrationPageOrModal.isDisplayed());
    }

    // Test to verify the text "Meet our requirements. The application does not have a database, all information is stored in local memory." is displayed below the buttons
    public void testTextDisplay() {
        WebDriver driver = createDriver();
        driver.get(BUG_BANK_URL);

        // Verify text display
        WebElement buttonRow = driver.findElement(By.xpath("//div[contains(@class, 'buttons')]"));
        assertEquals("Meet our requirements. The application does not have a database, all information is stored in local memory.", buttonRow.findElement(By.tagName("p")).getText());
    }

    private static WebDriver createDriver() {
        System.setProperty("webdriver.chrome", "elenium-chromedriver"); // Replace with your preferred driver
        return new ChromeDriver();
    }
}


/* 
 * In this code, we define several test methods:

* `testPageStructure`: This method verifies that the page structure (text in the left column and elements in the right column) is correct.
* `testSuccessfulLogin`: This method tests successful login with valid email and password.
* `testLoginFailure`: This method tests login failure with invalid email or password.
* `testRegistration`: This method tests redirection to registration page or modal when clicking the "Registrar" button.
* `testTextDisplay`: This method verifies that the text "Meet our requirements. The application does not have a database, all information is stored in local memory." is displayed below the buttons.

In each test method, we use appropriate locators (ID, name, class, XPath, or CSS selectors) for the elements and include assertions to validate the expected outcomes for each test scenario. We also handle dynamic content loading using implicit or explicit waits where applicable.

Note that this is just an example code and you may need to modify it based on your specific requirements and testing scenarios. 

 * 
 * 
 */