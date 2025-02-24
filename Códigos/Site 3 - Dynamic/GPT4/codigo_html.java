package site3_dynamic.gpt4;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class codigo_html {
    public static void main(String[] args) {
        // Set up WebDriver
        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to the dynamic loading page
            driver.get("https://the-internet.herokuapp.com/dynamic_loading");
            
            // Validate page structure
            WebElement title = driver.findElement(By.tagName("h3"));
            WebElement example1Link = driver.findElement(By.linkText("Example 1: Element on page that is hidden"));
            WebElement example2Link = driver.findElement(By.linkText("Example 2: Element rendered after the fact"));
            
            assert title.getText().contains("Dynamically Loaded Page Elements");
            assert example1Link.isDisplayed();
            assert example2Link.isDisplayed();
            
            // Test Example 1 - Hidden Element Behavior
            example1Link.click();
            wait.until(ExpectedConditions.urlContains("/dynamic_loading/1"));
            
            WebElement startButton = driver.findElement(By.tagName("button"));
            assert startButton.isDisplayed();
            
            startButton.click();
            WebElement loadingBar = driver.findElement(By.id("loading"));
            wait.until(ExpectedConditions.invisibilityOf(loadingBar));
            
            WebElement helloWorldText = driver.findElement(By.id("finish"));
            wait.until(ExpectedConditions.visibilityOf(helloWorldText));
            assert helloWorldText.getText().equals("Hello World!");
            
            // Navigate back to main page
            driver.navigate().back();
            
            // Test Example 2 - Rendered Element Behavior
            example2Link.click();
            wait.until(ExpectedConditions.urlContains("/dynamic_loading/2"));
            
            startButton = driver.findElement(By.tagName("button"));
            assert startButton.isDisplayed();
            
            startButton.click();
            loadingBar = driver.findElement(By.id("loading"));
            wait.until(ExpectedConditions.invisibilityOf(loadingBar));
            
            helloWorldText = driver.findElement(By.id("finish"));
            wait.until(ExpectedConditions.visibilityOf(helloWorldText));
            assert helloWorldText.getText().equals("Hello World!");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}