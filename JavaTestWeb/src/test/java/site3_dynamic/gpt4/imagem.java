package site3_dynamic.gpt4;

import java.time.Duration;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class imagem {
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "https://the-internet.herokuapp.com/dynamic_loading";

    @Before
    public void setUp() {
        // Initialize the Chrome WebDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Set default wait time for explicit waits
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void tearDown() {
        // Close the browser after each test
        driver.quit();
    }

    @Test
    public void testPageStructure() {
        // Navigate to the base URL
        driver.get(baseUrl);

        // Verify page title
        WebElement pageHeader = driver.findElement(By.tagName("h3"));
        assertEquals("Dynamically Loaded Page Elements", pageHeader.getText());

        // Verify presence of both example links
        WebElement example1Link = driver.findElement(By.linkText("Example 1: Element on page that is hidden"));
        WebElement example2Link = driver.findElement(By.linkText("Example 2: Element rendered after the fact"));
        assertTrue(example1Link.isDisplayed());
        assertTrue(example2Link.isDisplayed());
    }

    @Test
    public void testExample1HiddenElement() {
        // Navigate to Example 1
        driver.get(baseUrl + "/1");

        // Verify initial state
        WebElement startButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#start button")));

        // Check if Hello World text is present but hidden
        WebElement hiddenElement = driver.findElement(By.cssSelector("#finish"));
        assertTrue(!hiddenElement.isDisplayed());

        // Click Start button
        startButton.click();

        // Wait for loading to complete and verify text
        WebElement helloText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));
        assertEquals("Hello World!", helloText.getText());
    }

    @Test
    public void testExample2RenderedElement() {
        // Navigate to Example 2
        driver.get(baseUrl + "/2");

        // Verify initial state
        WebElement startButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#start button")));

        // Verify Hello World text is not present initially
        assertTrue(driver.findElements(By.cssSelector("#finish h4")).isEmpty());

        // Click Start button
        startButton.click();

        // Wait for loading to complete and verify text
        WebElement helloText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));
        assertEquals("Hello World!", helloText.getText());
    }
}