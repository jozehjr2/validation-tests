package site2.mistral;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginPageTest {

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void verifyPageStructure() {
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Left column text
        String leftColumnText = driver.findElement(By.cssSelector("#login .col-left > p")).getText();
        Assert.assertEquals(leftColumnText, "Seja bem vindo, acesse e aproveite todo o conteúdo, somos uma equipe de profissionais empenhados em trazer o melhor conteúdo direcionado a você, usuário.");

        // Right column elements
        WebElement rightColumn = driver.findElement(By.cssSelector("#login .col-right"));
        Assert.assertEquals(rightColumn.getText(), "Olá! Seja bem vindo. faça o seu login agora");

        WebElement usernameField = driver.findElement(By.id("username"));
        Assert.assertNotNull(usernameField);

        WebElement passwordField = driver.findElement(By.id("password"));
        Assert.assertNotNull(passwordField);

        WebElement forgotPasswordLink = driver.findElement(By.linkText("Esqueceu a sua senha?"));
        Assert.assertNotNull(forgotPasswordLink);

        WebElement createAccountLink = driver.findElement(By.linkText("Criar uma conta"));
        Assert.assertNotNull(createAccountLink);

        // Social media icons
        List<WebElement> socialMediaIcons = driver.findElements(By.cssSelector("#login .col-right a"));
        Assert.assertEquals(socialMediaIcons.size(), 4);
    }

    @Test
    public void testLogin() {
        driver.get("https://wavingtest.github.io/system-healing-test/");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("#login form button"));

        // Test correct login
        usernameField.sendKeys("validUsername");
        passwordField.sendKeys("validPassword");
        loginButton.click();
        // Assuming there is no redirect in this case, you can add an assertion to verify the user is on the dashboard page or any expected subsequent page.

        // Test incorrect login (with invalid username)
        usernameField.clear();
        passwordField.clear();
        usernameField.sendKeys("invalidUsername");
        passwordField.sendKeys("validPassword");
        loginButton.click();
        WebElement errorMessage = driver.findElement(By.cssSelector("#login .error-message"));
        Assert.assertNotNull(errorMessage);

        // Test incorrect login (with invalid password)
        usernameField.clear();
        passwordField.clear();
        usernameField.sendKeys("validUsername");
        passwordField.sendKeys("invalidPassword");
        loginButton.click();
        errorMessage = driver.findElement(By.cssSelector("#login .error-message"));
        Assert.assertNotNull(errorMessage);
    }

    @Test
    public void testHyperlinkRedirects() {
        driver.get("https://wavingtest.github.io/system-healing-test/");

        // Forgot Password link
        WebElement forgotPasswordLink = driver.findElement(By.linkText("Esqueceu a sua senha?"));
        forgotPasswordLink.click();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/forgot-password"));

        // Create an Account link
        WebElement createAccountLink = driver.findElement(By.linkText("Criar uma conta"));
        createAccountLink.click();
        currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/create-account"));
    }

    @Test
    public void testSocialMediaRedirects() {
        driver.get("https://wavingtest.github.io/system-healing-test/");

        List<WebElement> socialMediaIcons = driver.findElements(By.cssSelector("#login .col-right a"));

        for (WebElement icon : socialMediaIcons) {
            String url = icon.getAttribute("href");
            switch (url) {
                case "https://www.facebook.com":
                    icon.click();
                    String currentUrl = driver.getCurrentUrl();
                    Assert.assertTrue(currentUrl.contains("facebook"));
                    break;
                case "https://accounts.google.com":
                    icon.click();
                    currentUrl = driver.getCurrentUrl();
                    Assert.assertTrue(currentUrl.contains("google"));
                    break;
                case "https://x.co": // Twitter URL is different for X (formerly Twitter)
                    icon.click();
                    currentUrl = driver.getCurrentUrl();
                    Assert.assertTrue(currentUrl.contains("twitter"));
                    break;
                case "https://github.com":
                    icon.click();
                    currentUrl = driver.getCurrentUrl();
                    Assert.assertTrue(currentUrl.contains("github"));
                    break;
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
/* 
This code is a JUnit test for a webpage. It covers functionalities like:
1. Checking if all elements are present (username, password fields, login button, forgot password link, create account link, and social media icons)
2. Testing the login functionality with valid credentials and invalid ones
3. Verifying if the hyperlinks redirect to their respective pages (forgot password, create an account, and social media links)

Please provide a brief explanation for each test case.

Also, this test assumes that the webpage uses a form for login and the URL changes when navigating to other pages or clicking on links.
If you see any issues with the code or if there are better practices I can suggest, feel free to let me know!

Lastly, it's important to note that this is just a simple example of a test case. A real-world application may require more comprehensive testing and handling edge cases.

---

The provided JUnit test covers the following functionalities:

1. `testHyperlinkRedirects()` - This method checks if all hyperlinks (forgot password link, create an account link, and social media icons) redirect to their respective pages upon clicking them. It does this by first clicking on each link, storing the current URL in a variable, and then verifying that the stored URL contains the expected part (e.g., "/forgot-password" for the forgot password link).

2. `testSocialMediaRedirects()` - This method is similar to the previous one but for social media icons. It stores the current URL after clicking each icon and verifies that it contains the corresponding service's domain name (e.g., "facebook" or "github").

3. `testLogin()` - This method tests the login functionality by first entering valid credentials, then invalid ones for both the username and password fields, and finally checking if an error message appears when using invalid data. Since there is no redirect after a successful login in this example, you can add an assertion to verify the user is on the dashboard page or any expected subsequent page if that's the case in your application.

4. `testHyperlinkRedirects()` - This method checks if all essential elements are present on the webpage (username, password fields, login button, forgot password link, create account link, and social media icons). It does this by asserting that each element is not null or non-empty.

Lastly, it's good practice to include an `@AfterEach` method to quit the driver after each test case to ensure resources are released as soon as possible.

*/

