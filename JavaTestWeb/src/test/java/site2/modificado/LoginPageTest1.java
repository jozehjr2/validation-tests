package site2.modificado;

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

public class LoginPageTest1 {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://wavingtest.github.io/system-healing-test/";
    
    /**
     * Setup method to initialize the WebDriver before tests
     */
    @BeforeClass
    public void setUp() {
        // Initialize ChromeDriver
        // Note: Ensure chromedriver.exe path is set in system properties or added to PATH
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        // Configure wait for dynamic elements
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Set implicit wait for all elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }
    
    /**
     * Navigate to login page before each test
     */
    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(BASE_URL);
        
        // Verify we're on the login page by checking the title
        Assert.assertEquals(driver.getTitle(), "Login", "Page title doesn't match expected 'Login'");
    }
    
    /**
     * Test case to verify page structure and content
     */
    @Test(priority = 1)
    public void testPageStructure() {
        // Verify container exists
        WebElement container = driver.findElement(By.cssSelector("[data-test-id='login-container']"));
        Assert.assertTrue(container.isDisplayed(), "Login container is not displayed");
        
        // Verify banner and welcome message
        WebElement banner = driver.findElement(By.cssSelector("[data-test-id='login-banner']"));
        Assert.assertTrue(banner.isDisplayed(), "Banner is not displayed");
        
        WebElement welcomeMessage = driver.findElement(By.cssSelector("[data-test-id='login-welcome-message']"));
        Assert.assertTrue(welcomeMessage.getText().contains("Seja bem vindo"), 
                "Welcome message does not contain expected text");
        
        // Verify login box elements
        WebElement loginBox = driver.findElement(By.cssSelector("[data-test-id='login-box']"));
        Assert.assertTrue(loginBox.isDisplayed(), "Login box is not displayed");
        
        WebElement greeting = driver.findElement(By.cssSelector("[data-test-id='login-greeting']"));
        Assert.assertEquals(greeting.getText(), "Olá! Seja bem vindo.", "Greeting text doesn't match");
        
        WebElement formTitle = driver.findElement(By.cssSelector("[data-test-id='login-form-title']"));
        Assert.assertEquals(formTitle.getText().toLowerCase(), "faça o seu login agora", 
                "Form title doesn't match");
        
        // Verify input fields
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        Assert.assertTrue(usernameInput.isDisplayed(), "Username input field is not displayed");
        Assert.assertEquals(usernameInput.getAttribute("placeholder"), "username", 
                "Username placeholder doesn't match");
        
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        Assert.assertTrue(passwordInput.isDisplayed(), "Password input field is not displayed");
        Assert.assertEquals(passwordInput.getAttribute("placeholder"), "password", 
                "Password placeholder doesn't match");
        
        // Verify links
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        Assert.assertTrue(forgotPasswordLink.isDisplayed(), "Forgot password link is not displayed");
        Assert.assertEquals(forgotPasswordLink.getText(), "Esqueceu a sua senha?", 
                "Forgot password text doesn't match");
        
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        Assert.assertTrue(createAccountLink.isDisplayed(), "Create account link is not displayed");
        Assert.assertEquals(createAccountLink.getText(), "Criar uma conta", 
                "Create account text doesn't match");
        
        // Verify login button
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed");
        Assert.assertEquals(loginButton.getText(), "Login", "Login button text doesn't match");
        
        // Verify social media icons
        List<WebElement> socialIcons = driver.findElements(By.cssSelector("[data-test-id='login-social-icons'] img"));
        Assert.assertEquals(socialIcons.size(), 4, "Expected 4 social media icons");
    }
    
    /**
     * Test case for successful login
     */
    @Test(priority = 2)
    public void testSuccessfulLogin() {
        // Enter valid credentials (these would need to be replaced with actual valid credentials)
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        
        usernameInput.sendKeys("validUsername");
        passwordInput.sendKeys("validPassword");
        loginButton.click();
        
        // Verify successful login toast appears
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id='login-toast']")));
        WebElement toastMessage = driver.findElement(By.cssSelector("[data-test-id='login-toast-message']"));
        
        Assert.assertTrue(toast.isDisplayed(), "Success toast notification is not displayed");
        Assert.assertEquals(toastMessage.getText(), "Login feito com sucesso!", 
                "Success message doesn't match expected text");
    }
    
    /**
     * Test case for login with invalid credentials
     */
    @Test(priority = 3)
    public void testInvalidLogin() {
        // Enter invalid credentials
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        
        usernameInput.sendKeys("invalidUsername");
        passwordInput.sendKeys("invalidPassword");
        loginButton.click();
        
        // Verify error message is displayed
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id='login-error-message']")));
        
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message is not displayed");
        Assert.assertFalse(errorMessage.getText().isEmpty(), "Error message is empty");
    }
    
    /**
     * Test case to verify "Forgot Password" link redirection
     */
    @Test(priority = 4)
    public void testForgotPasswordRedirection() {
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        forgotPasswordLink.click();
        
        // Wait for page to load and verify URL
        wait.until(ExpectedConditions.urlContains("password.html"));
        Assert.assertTrue(driver.getCurrentUrl().endsWith("password.html"), 
                "URL doesn't end with 'password.html'");
    }
    
    /**
     * Test case to verify "Create Account" link redirection
     */
    @Test(priority = 5)
    public void testCreateAccountRedirection() {
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        createAccountLink.click();
        
        // Wait for page to load and verify URL
        wait.until(ExpectedConditions.urlContains("account.html"));
        Assert.assertTrue(driver.getCurrentUrl().endsWith("account.html"), 
                "URL doesn't end with 'account.html'");
    }
    
    /**
     * Test case to verify social media icons redirection
     */
    @Test(priority = 6)
    public void testSocialMediaRedirection() {
        // Store the main window handle to return later
        String mainWindowHandle = driver.getWindowHandle();
        
        // Test Facebook icon
        WebElement facebookIcon = driver.findElement(By.cssSelector("[data-test-id='login-social-facebook']"));
        facebookIcon.click();
        verifyRedirection(mainWindowHandle, "facebook.com");
        
        // Test Google icon
        WebElement googleIcon = driver.findElement(By.cssSelector("[data-test-id='login-social-google']"));
        googleIcon.click();
        verifyRedirection(mainWindowHandle, "google.com");
        
        // Test Twitter icon
        WebElement twitterIcon = driver.findElement(By.cssSelector("[data-test-id='login-social-twitter']"));
        twitterIcon.click();
        verifyRedirection(mainWindowHandle, "twitter.com");
        
        // Test GitHub icon
        WebElement githubIcon = driver.findElement(By.cssSelector("[data-test-id='login-social-github']"));
        githubIcon.click();
        verifyRedirection(mainWindowHandle, "github.com");
    }
    
    /**
     * Helper method to verify social media redirection
     */
    private void verifyRedirection(String mainWindowHandle, String expectedUrl) {
        // Wait for new window/tab to open
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        
        // Switch to new window/tab
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        
        // Verify URL contains expected domain
        wait.until(ExpectedConditions.urlContains(expectedUrl));
        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl), 
                "URL doesn't contain '" + expectedUrl + "'");
        
        // Close the tab and switch back to main window
        driver.close();
        driver.switchTo().window(mainWindowHandle);
    }
    
    /**
     * Cleanup method to close browser after tests
     */
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}