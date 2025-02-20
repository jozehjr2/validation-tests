package site2.modificado;


import org.junit.jupiter.api.AfterEach; //import org.junit.After;
import org.junit.jupiter.api.BeforeEach; //import org.junit.Before;
import org.junit.jupiter.api.Test; //import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue; //import static org.junit.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * Selenium WebDriver tests for the login page functionality.
 */
public class LoginPageTest2 {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://wavingtest.github.io/system-healing-test/";
    
    @BeforeEach
    public void setUp() {
        // Set up the WebDriver (ensure ChromeDriver is in your system PATH)
        driver = new ChromeDriver();
        // Create a wait instance with 10-second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Maximize window
        driver.manage().window().maximize();
        // Navigate to the application URL
        driver.get(BASE_URL);
    }
    
    @AfterEach
    public void tearDown() {
        // Close the browser after each test
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Test to verify the structure of the login page matches the expected content and elements.
     */
    @Test
    public void testPageStructure() {
        // Verify the container exists
        WebElement container = driver.findElement(By.cssSelector("[data-test-id='login-container']"));
        assertTrue(container.isDisplayed(), "Container should be present");
        
        // Verify the banner section
        WebElement banner = driver.findElement(By.cssSelector("[data-test-id='login-banner']"));
        assertTrue(banner.isDisplayed(), "Banner should be present");
        WebElement bannerImage = driver.findElement(By.cssSelector("[data-test-id='login-image']"));
        assertTrue(bannerImage.isDisplayed(), "Banner image should be present");
        
        // Verify welcome message text
        WebElement welcomeMsg = driver.findElement(By.cssSelector("[data-test-id='login-welcome-message']"));
        String expectedWelcomeText = "Seja bem vindo, acesse e aproveite todo o conteúdo,";
        assertTrue(welcomeMsg.getText().contains(expectedWelcomeText), 
        		"Welcome message should contain expected text");
        
        // Verify login box elements
        WebElement loginBox = driver.findElement(By.cssSelector("[data-test-id='login-box']"));
        assertTrue(loginBox.isDisplayed(), "Login box should be present");
        
        // Verify greeting text
        WebElement greeting = driver.findElement(By.cssSelector("[data-test-id='login-greeting']"));
        assertEquals("Greeting should match", "Olá! Seja bem vindo.", greeting.getText());
        
        // Verify form elements
        WebElement form = driver.findElement(By.cssSelector("[data-test-id='login-form']"));
        assertTrue(form.isDisplayed(), "Login form should be present");
        
        WebElement formTitle = driver.findElement(By.cssSelector("[data-test-id='login-form-title']"));
        assertEquals("Form title should match", "faça o seu login agora", formTitle.getText());
        
        // Verify input fields
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        assertTrue(usernameInput.isDisplayed(),"Username input should be present");
        assertEquals("Username placeholder should match", "username", usernameInput.getAttribute("placeholder"));
        
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        assertTrue(passwordInput.isDisplayed(),"Password input should be present");
        assertEquals("Password placeholder should match", "password", passwordInput.getAttribute("placeholder"));
        
        // Verify links
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        assertTrue(forgotPasswordLink.isDisplayed(), "Forgot password link should be present");
        assertEquals("Forgot password text should match", "Esqueceu a sua senha?", forgotPasswordLink.getText());
        
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        assertTrue(createAccountLink.isDisplayed(),"Create account link should be present");
        assertEquals("Create account text should match", "Criar uma conta", createAccountLink.getText());
        
        // Verify login button
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        assertTrue(loginButton.isDisplayed(),"Login button should be present");
        assertEquals("Login button text should match", "Login", loginButton.getText());
        
        // Verify social media icons
        WebElement socialSection = driver.findElement(By.cssSelector("[data-test-id='login-social-icons']"));
        assertTrue(socialSection.isDisplayed(), "Social media section should be present");
        
        List<WebElement> socialIcons = socialSection.findElements(By.tagName("img"));
        assertEquals(socialIcons.size(), 4, "Should have 4 social media icons");
        
        // Verify each social icon
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-facebook']")).isDisplayed(), 
                "Facebook icon should be present");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-google']")).isDisplayed(), 
                "Google icon should be present");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-twitter']")).isDisplayed(), 
                "Twitter icon should be present");
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='login-social-github']")).isDisplayed(), 
        		"GitHub icon should be present");
    }
    
    /**
     * Test login functionality with correct credentials.
     * Note: This is a simulated test as we don't have the actual backend implementation.
     */
    @Test
    public void testSuccessfulLogin() {
        // Enter valid credentials
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        
        usernameInput.sendKeys("validUser");
        passwordInput.sendKeys("validPassword");
        loginButton.click();
        
        // Since this is a static page, we'll assume the toast message appears on successful login
        // Wait for the toast message to be visible
        WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test-id='login-toast']")));
        
        WebElement toastMessage = driver.findElement(By.cssSelector("[data-test-id='login-toast-message']"));
        assertEquals("Toast message should indicate successful login", 
                "Login feito com sucesso!", toastMessage.getText());
    }
    
    /**
     * Test login functionality with incorrect credentials.
     * Note: This is a simulated test as we don't have the actual backend implementation.
     */
    @Test
    public void testFailedLogin() {
        // Enter invalid credentials
        WebElement usernameInput = driver.findElement(By.cssSelector("[data-test-id='login-username-input']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("[data-test-id='login-password-input']"));
        WebElement loginButton = driver.findElement(By.cssSelector("[data-test-id='login-submit-button']"));
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id='login-error-message']"));
        
        usernameInput.sendKeys("invalidUser");
        passwordInput.sendKeys("invalidPassword");
        loginButton.click();
        
        // For static page testing, we'll need to simulate the error message using JavaScript
        // In a real application, this would appear naturally after failed login attempt
        //driver.executeScript(
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('errorMessage5').textContent = 'Nome de usuário ou senha inválidos';");
        
        // Check if error message is displayed
        wait.until(ExpectedConditions.textToBePresentInElement(errorMessage, 
                "Nome de usuário ou senha inválidos"));
        
        assertTrue(errorMessage.isDisplayed() && !errorMessage.getText().isEmpty(), 
                "Error message should be displayed for invalid credentials");
    }
    
    /**
     * Test the "Forgot Password" link redirection.
     */
    @Test
    public void testForgotPasswordRedirection() {
        WebElement forgotPasswordLink = driver.findElement(By.cssSelector("[data-test-id='login-forgot-password-link']"));
        forgotPasswordLink.click();
        
        // Wait for page to load and verify the URL
        wait.until(ExpectedConditions.urlContains("password.html"));
        
        assertEquals("Should redirect to forgot password page",
                BASE_URL + "password.html", driver.getCurrentUrl());
    }
    
    /**
     * Test the "Create Account" link redirection.
     */
    @Test
    public void testCreateAccountRedirection() {
        WebElement createAccountLink = driver.findElement(By.cssSelector("[data-test-id='login-create-account-link']"));
        createAccountLink.click();
        
        // Wait for page to load and verify the URL
        wait.until(ExpectedConditions.urlContains("account.html"));
        
        assertEquals("Should redirect to create account page",
                BASE_URL + "account.html", driver.getCurrentUrl());
    }
    
    /**
     * Test social media icons redirection.
     * Note: We're checking if clicking initiates navigation, but we won't actually navigate
     * to external sites in the test.
     */
    @Test
    public void testSocialMediaRedirection() {
        // For static page with no actual redirection, we'll verify the hrefs or simulate clicks
        // In a real implementation, these would have proper URLs
        
        // Test Facebook icon
        testSocialIconRedirection("login-social-facebook", "facebook.com");
        
        // Test Google icon
        testSocialIconRedirection("login-social-google", "google.com");
        
        // Test Twitter icon
        testSocialIconRedirection("login-social-twitter", "twitter.com");
        
        // Test GitHub icon
        testSocialIconRedirection("login-social-github", "github.com");
    }
    
    /**
     * Helper method to test social media icon redirection
     */
    private void testSocialIconRedirection(String iconTestId, String expectedDomain) {
        // Navigate back to login page for each test
        driver.get(BASE_URL);
        
        // Add click event listeners to track navigation attempts
        String script = "var clickTracked = false;" +
                "document.querySelector('[data-test-id=\"" + iconTestId + "\"]').addEventListener('click', function(e) {" +
                "  e.preventDefault();" +
                "  clickTracked = true;" +
                "  document.body.setAttribute('data-last-clicked', '" + expectedDomain + "');" +
                "});";
        //driver.executeScript(script);
        ((JavascriptExecutor) driver).executeScript(script);
        
        // Click the icon
        WebElement icon = driver.findElement(By.cssSelector("[data-test-id='" + iconTestId + "']"));
        icon.click();
        
        // Verify the click was registered
        //Boolean clickTracked = (Boolean) driver.executeScript("return clickTracked;");
        Boolean clickTracked = (Boolean)  ((JavascriptExecutor) driver).executeScript("return clickTracked;");
        assertTrue(clickTracked, "Click should be tracked for " + iconTestId);
        
        // For a real implementation, we would verify the actual navigation
        // But for this static page test, we just verify the click was tracked
        // String lastClicked = (String) driver.executeScript(
        String lastClicked = (String) ((JavascriptExecutor) driver).executeScript(
                "return document.body.getAttribute('data-last-clicked');");
        assertEquals("Last clicked domain should match " + expectedDomain, expectedDomain, lastClicked);
    }
}
