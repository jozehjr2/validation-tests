package site2.modificado;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest3 {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://wavingtest.github.io/system-healing-test/";

    @BeforeEach
    public void setUp() {
        // Set up Chrome driver with headless option
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Navigate to the application URL
        driver.get(BASE_URL);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Verify basic page structure and elements are present
     */
    @Test
    public void testPageStructure() {
        // Verify container exists
        WebElement container = driver.findElement(By.cssSelector("[data-test-id='login-container']"));
        assertTrue(container.isDisplayed(), "Container should be visible");

        // Verify banner section
        WebElement banner = driver.findElement(By.cssSelector("[data-test-id='login-banner']"));
        assertTrue(banner.isDisplayed(), "Banner should be visible");
        
        // Verify welcome message
        WebElement welcomeMessage = driver.findElement(By.cssSelector("[data-test-id='login-welcome-message']"));
        assertTrue(welcomeMessage.getText().contains("Seja bem vindo, acesse e aproveite todo o conteúdo"), 
                  "Welcome message should contain expected text");

        // Verify login box
        WebElement loginBox = driver.findElement(By.cssSelector("[data-test-id='login-box']"));
        assertTrue(loginBox.isDisplayed(), "Login box should be visible");
        
        // Verify greeting
        WebElement greeting = driver.findElement(By.cssSelector("[data-test-id='login-greeting']"));
        assertEquals("Greeting text should match", "Olá! Seja bem vindo.", greeting.getText());
        
        // Verify form elements
        WebElement loginForm = driver.findElement(By.cssSelector("[data-test-id='login-form']"));
        assertTrue(loginForm.isDisplayed(), "Login form should be visible");
        
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        assertTrue(usernameInput.isDisplayed(), "Username input should be visible");
        
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        assertTrue(passwordInput.isDisplayed(), "Password input should be visible");
        
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        assertEquals("Login button text should be 'Login'", "Login", loginButton.getText());
        
        // Verify links
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        assertTrue(forgotPasswordLink.isDisplayed(), "Forgot password link should be visible");
        assertEquals("Forgot password text should match", "Esqueceu a sua senha?", forgotPasswordLink.getText().trim());
        
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        assertTrue(createAccountLink.isDisplayed(), "Create account link should be visible");
        assertEquals("Create account text should match", "Criar uma conta", createAccountLink.getText().trim());
        
        // Verify social media icons
        WebElement socialIcons = driver.findElement(By.cssSelector("[data-test-id='login-social-icons']"));
        assertTrue(socialIcons.isDisplayed(), "Social media section should be visible");
        
        // Check all social icons
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-facebook']")).isDisplayed(), 
                 "Facebook icon should be visible");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-google']")).isDisplayed(), 
                 "Google icon should be visible");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-twitter']")).isDisplayed(), 
                 "Twitter icon should be visible");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-github']")).isDisplayed(), 
                 "Github icon should be visible");
    }

    /**
     * Test successful login scenario
     * Note: Since this is a static page, we're mocking the success by checking for the toast message
     */
    @Test
    public void testSuccessfulLogin() {
        // Enter valid credentials (assuming these credentials work for the static demo)
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("password123");
        loginButton.click();
        
        // Since this is a static page, we're checking if the toast is displayed
        // In a real application, we would check for redirection or session creation
        try {
            // Wait for the toast to appear
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[data-test-id='login-toast']")));
            WebElement toastMessage = driver.findElement(By.cssSelector("[data-test-id='login-toast-message']"));
            
            assertTrue(toast.isDisplayed(), "Toast should be visible on successful login");
            assertEquals("Toast message should indicate successful login", 
                        "Login feito com sucesso!", toastMessage.getText());
        } catch (Exception e) {
            fail("Toast message not displayed after login attempt: " + e.getMessage());
        }
    }

    /**
     * Test login failure scenario
     */
    @Test
    public void testFailedLogin() {
        // Enter invalid credentials
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        
        usernameInput.sendKeys("wronguser");
        passwordInput.sendKeys("wrongpassword");
        loginButton.click();
        
        // Check for error message
        try {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[data-test-id='login-error-message']")));
            
            assertTrue(errorMessage.isDisplayed(), "Error message should be displayed");
            assertFalse(errorMessage.getText().isEmpty(), "Error message should not be empty");
        } catch (Exception e) {
            fail("Error message element not found or not visible: " + e.getMessage());
        }
    }

    /**
     * Test forgot password link redirection
     */
    @Test
    public void testForgotPasswordLink() {
        // Click on forgot password link
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        forgotPasswordLink.click();
        
        // Wait for page to load and verify URL
        wait.until(ExpectedConditions.urlContains("password.html"));
        
        assertEquals("Should redirect to password recovery page", 
                    BASE_URL + "password.html", driver.getCurrentUrl());
    }

    /**
     * Test create account link redirection
     */
    @Test
    public void testCreateAccountLink() {
        // Click on create account link
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        createAccountLink.click();
        
        // Wait for page to load and verify URL
        wait.until(ExpectedConditions.urlContains("account.html"));
        
        assertEquals("Should redirect to account creation page", 
                    BASE_URL + "account.html", driver.getCurrentUrl());
    }

    /**
     * Test social media links
     * Note: Since social links usually open new tabs/windows, we'll check for new window handles
     */
    @Test
    public void testSocialMediaRedirects() {
        testSocialMediaRedirect("login-social-facebook", "facebook.com");
    }
    
    @Test
    public void testGoogleRedirect() {
        testSocialMediaRedirect("login-social-google", "google.com");
    }
    
    @Test
    public void testTwitterRedirect() {
        testSocialMediaRedirect("login-social-twitter", "twitter.com");
    }
    
    @Test
    public void testGithubRedirect() {
        testSocialMediaRedirect("login-social-github", "github.com");
    }
    
    /**
     * Helper method to test social media redirects
     */
    private void testSocialMediaRedirect(String socialIconId, String expectedDomainPart) {
        // Store the original window handle
        String originalWindow = driver.getWindowHandle();
        
        // Get initial window handles count
        int initialHandlesCount = driver.getWindowHandles().size();
        
        // Click on social media icon
        WebElement socialIcon = driver.findElement(By.cssSelector("[data-test-id='" + socialIconId + "']"));
        socialIcon.click();
        
        // Wait for new window/tab
        wait.until(ExpectedConditions.numberOfWindowsToBe(initialHandlesCount + 1));
        
        // Switch to new window/tab
        Set<String> windowHandles = driver.getWindowHandles();
        String newWindowHandle = windowHandles.stream()
                .filter(handle -> !handle.equals(originalWindow))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No new window was opened"));
        
        driver.switchTo().window(newWindowHandle);
        
        try {
            // Wait for the page to load
            wait.until(ExpectedConditions.urlContains(expectedDomainPart));
            
            // Verify the URL contains expected domain
            assertTrue(driver.getCurrentUrl().contains(expectedDomainPart), 
                      "URL should contain " + expectedDomainPart);
        } finally {
            // Close the new window and switch back to original
            driver.close();
            driver.switchTo().window(originalWindow);
        }
    }
}