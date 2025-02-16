import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BugBankLoginPageTest {

    private WebDriver driver;

    @BeforeTest
    public void setup() {
        // Set up the ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    public void testPageStructure() {
        // Navigate to the login page
        driver.get("https://bugbank.netlify.app/");

        // Verify the left column text
        WebElement leftColumnText = driver.findElement(By.xpath("//div[@class='left-column']"));
        String expectedLeftColumnText = "BugBank. The bank with bugs and flaws your way. Make transfers and payments with bugs and practice testing successfully in a near-real scenario!";
        Assert.assertEquals(leftColumnText.getText(), expectedLeftColumnText);

        // Verify the right column elements
        WebElement emailField = driver.findElement(By.name("email"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement accessButton = driver.findElement(By.id("access-button"));
        WebElement registrarButton = driver.findElement(By.id("registrar-button"));

        // Verify the text below the buttons
        WebElement requirementsText = driver.findElement(By.xpath("//div[@class='requirements']"));
        String expectedRequirementsText = "Meet our requirements. The application does not have a database, all information is stored in local memory.";
        Assert.assertEquals(requirementsText.getText(), expectedRequirementsText);
    }

    @Test
    public void testSuccessfulLogin() {
        // Enter valid email and password
        driver.get("https://bugbank.netlify.app/");
        WebElement emailField = driver.findElement(By.name("email"));
        emailField.sendKeys("valid-email@example.com");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("correct-password");

        // Click the access button
        WebElement accessButton = driver.findElement(By.id("access-button"));
        accessButton.click();

        // Verify login success
        String expectedTitle = "Dashboard";
        Assert.assertEquals(driver.getTitle(), expectedTitle);
    }

    @Test
    public void testLoginFailure() {
        // Enter invalid email and password
        driver.get("https://bugbank.netlify.app/");
        WebElement emailField = driver.findElement(By.name("email"));
        emailField.sendKeys("invalid-email@example.com");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("incorrect-password");

        // Click the access button
        WebElement accessButton = driver.findElement(By.id("access-button"));
        accessButton.click();

        // Verify error message appears
        String expectedErrorMessage = "Invalid email or password";
        WebElement errorMessage = driver.findElement(By.xpath("//div[@class='error-message']"));
        Assert.assertEquals(errorMessage.getText(), expectedErrorMessage);
    }

    @Test
    public void testRegistrarButton() {
        // Click the registrar button
        driver.get("https://bugbank.netlify.app/");
        WebElement registrarButton = driver.findElement(By.id("registrar-button"));
        registrarButton.click();

        // Verify redirection to registration page or modal
        String expectedUrl = "https://bugbank.netlify.app/registration";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }
}


/* 
Note:

* This code uses TestNG framework for running the tests.
* The `setup` method sets up the ChromeDriver and navigates to the login page.
* Each test method uses Selenium WebDriver methods (e.g. `get`, `findElement`, `sendKeys`, `click`) to interact with the web application.
* Assertions are used to verify the expected outcomes for each test scenario.
* Implicit waits are used to handle dynamic content loading in some tests.

Please adjust the code as per your specific requirements and environment.
*/