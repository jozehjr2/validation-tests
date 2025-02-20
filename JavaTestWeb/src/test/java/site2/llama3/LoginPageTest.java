package site2.llama3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set the ChromeDriver executable path
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");

        // Create a new instance of ChromeDriver
        driver = new ChromeDriver();
    }

    @Test
    public void testPageStructure() {
        // Navigate to the login page
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Verify that the left column text matches the expected content
        WebElement leftColumnText = driver.findElement(By.xpath("//div[@class='left-column-text']"));
        String actualText = leftColumnText.getText();
        String expectedText = "Seja bem vindo, acesse e aproveite todo o conteúdo, somos uma equipe de profissionais empenhados em trazer o melhor conteúdo direcionado a você, usuário.";
        assert actualText.equals(expectedText);

        // Verify that the right column contains the expected elements
        WebElement rightColumn = driver.findElement(By.xpath("//div[@class='right-column']"));
        WebElement greetingText = rightColumn.findElement(By.tagName("p"));
        String actualGreetingText = greetingText.getText();
        String expectedGreetingText = "Olá! Seja bem vindo. faça o seu login agora.";
        assert actualGreetingText.equals(expectedGreetingText);

        // Verify that the username field exists
        WebElement usernameField = driver.findElement(By.name("username"));
        assert usernameField.isDisplayed();

        // Verify that the password field exists
        WebElement passwordField = driver.findElement(By.name("password"));
        assert passwordField.isDisplayed();

        // Verify that the "Esqueceu a sua senha?" hyperlink exists and is clickable
        WebElement forgotPasswordLink = driver.findElement(By.linkText("Esqueceu a sua senha?"));
        assert forgotPasswordLink.isDisplayed() && forgotPasswordLink.isEnabled();

        // Verify that the "Criar uma conta" hyperlink exists and is clickable
        WebElement createAccountLink = driver.findElement(By.linkText("Criar uma conta"));
        assert createAccountLink.isDisplayed() && createAccountLink.isEnabled();
    }

    @Test
    public void testLoginButton() {
        // Navigate to the login page
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Enter valid username and password
        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("valid_username");
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("valid_password");

        // Click the "Login" button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Verify that the login is successful
        assert driver.getTitle().contains("Dashboard");

        // Enter invalid username and/or password
        usernameField.clear();
        usernameField.sendKeys("invalid_username");
        passwordField.clear();
        passwordField.sendKeys("invalid_password");

        // Click the "Login" button again
        loginButton.click();

        // Verify that an error message is displayed
        WebElement errorMessage = driver.findElement(By.xpath("//div[@class='error-message']"));
        String actualErrorMessage = errorMessage.getText();
        String expectedErrorMessage = "Senha ou usuário incorreto.";
        assert actualErrorMessage.equals(expectedErrorMessage);
    }

    @Test
    public void testForgotPasswordLink() {
        // Navigate to the login page
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Click the "Esqueceu a sua senha?" hyperlink
        WebElement forgotPasswordLink = driver.findElement(By.linkText("Esqueceu a sua senha?"));
        forgotPasswordLink.click();

        // Verify that the Forgot Password page is displayed
        String expectedPageTitle = "Forgot Password";
        assert driver.getTitle().equals(expectedPageTitle);
    }

    @Test
    public void testCreateAccountLink() {
        // Navigate to the login page
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Click the "Criar uma conta" hyperlink
        WebElement createAccountLink = driver.findElement(By.linkText("Criar uma conta"));
        createAccountLink.click();

        // Verify that the Create an Account page is displayed
        String expectedPageTitle = "Create an Account";
        assert driver.getTitle().equals(expectedPageTitle);
    }

    @Test
    public void testSocialMediaIcons() {
        // Navigate to the login page
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Click each social media icon and verify that it redirects to the correct platform
        WebElement facebookIcon = driver.findElement(By.xpath("//a[@href='https://www.facebook.com']"));
        facebookIcon.click();
        assert driver.getTitle().contains("Facebook");

        WebElement googleIcon = driver.findElement(By.xpath("//a[@href='https://www.google.com']"));
        googleIcon.click();
        assert driver.getTitle().contains("Google");

        WebElement xIcon = driver.findElement(By.xpath("//a[@href='https://www.x.com']")); // Note: X is not a real platform    
        xIcon.click();
        assert driver.getTitle().contains("X"); // Assuming the redirection is correct, but this will fail if it's not

        WebElement githubIcon = driver.findElement(By.xpath("//a[@href='https://www.github.com']"));
        githubIcon.click();
        assert driver.getTitle().contains("GitHub");
    }
}


/* 
This code uses Selenium WebDriver to navigate to the login page and perform various tests, including:

1. Verifying that the page structure matches the description.
2. Testing the functionality of the "Login" button with correct and incorrect username/password combinations.
3. Verifying the redirection of the "Esqueceu a sua senha?" hyperlink to the Forgot Password page.
4. Verifying the redirection of the "Criar uma conta" hyperlink to the Create an Account page.
5. Testing that each social media icon redirects to the correct platform.

Note that this code assumes that the web application URL is `https://wavingtest.github.io/system-healing-test/` and that the page structure remains consistent across different scenarios. You may need to modify the code to accommodate changes in the page structure or functionality.
*/