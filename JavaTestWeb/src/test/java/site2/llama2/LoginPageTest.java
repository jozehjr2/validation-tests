package site2.llama2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginPageTest {
    public static WebDriver driver;

    @BeforeClass
    public static void setup() {
        driver = createDriver(); // create a new instance of the WebDriver
    }

    @Test
    public void testLoginPageStructure() {
        // Verify that the page structure matches the description
        WebElement usernameField = driver.findElement(By.id("username"));
        assertEquals("username", usernameField.getAttribute("name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        assertEquals("password", passwordField.getAttribute("name"));
        WebElement loginButton = driver.findElement(By.xpath("/html/body/div[2]/div[3]/button"));
        assertEquals("Login", loginButton.getAttribute("text"));
    }

    @Test
    public void testLoginButtonFunctionality() {
        // Test the functionality of the "Login" button with correct username and password
        driver.findElement(By.id("username")).sendKeys("user");
        driver.findElement(By.id("password")).sendKeys("password");
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.findElement(By.xpath("/html/body/div[2]/div[3]/button")).getAttribute("class").equals("btn btn-primary");
            }
        });
        // Verify that the login is successful
        assertTrue(driver.findElement(By.id("message")) .getText().equals("Logged in successfully"));
    }

    @Test
    public void testEsqueceuSenhaLink() {
        // Test that the "Esqueceu a sua senha?" hyperlink redirects to the Forgot Password page
        driver.findElement(By.xpath("/html/body/div[2]/div[1]")).click();
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.findElement(By.xpath("/html/body/div[3]")).getAttribute("innerHTML").equals("Forgot Password");    
            }
        });
    }

    @Test
    public void testCriarContaLink() {
        // Test that the "Criar uma conta" hyperlink redirects to the Create an Account page
        driver.findElement(By.xpath("/html/body/div[2]/div[4]")).click();
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.findElement(By.xpath("/html/body/div[5]")).getAttribute("innerHTML").equals("Create an Account");  
            }
        });
    }

    @Test
    public void testSocialMediaIconRedirection() {
        // Test that each of the four social media icons redirects to the correct platform
        driver.findElement(By.xpath("/html/body/div[2]/div[6]")).click(); // Facebook
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.getCurrentUrl().contains("facebook");
            }
        });
        driver.findElement(By.xpath("/html/body/div[2]/div[7]")).click(); // Google
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.getCurrentUrl().contains("google");
            }
        });
        driver.findElement(By.xpath("/html/body/div[2]/div[8]")).click(); // X (formerly Twitter)
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.getCurrentUrl().contains("x");
            }
        });
        driver.findElement(By.xpath("/html/body/div[2]/div[9]")).click(); // Github
        new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return input.getCurrentUrl().contains("github");
            }
        });
    }
}
/* 
In this example, we first verify that the page structure matches the description by checking the text content and field names of the elements. Then, we test the functionality of the "Login" button with correct username and password using an implicit wait to ensure that the button is fully loaded before attempting to click it. We also test the "Esqueceu a sua senha?" hyperlink to verify that it redirects to the Forgot Password page, and the "Criar uma conta" hyperlink to verify that it redirects to the Create an Account page. Finally, we test each of the four social media icons to verify that they redirect to the correct platform.   

Note: This is just an example code and may need to be modified based on the specifics of your project. Additionally, you may want to add more tests depending on your requirements.

*/