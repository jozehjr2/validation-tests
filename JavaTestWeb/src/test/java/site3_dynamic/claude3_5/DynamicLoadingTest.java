package site3_dynamic.claude3_5;

import java.time.Duration;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DynamicLoadingTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_loading";

    @Before
    public void setUp() {
        // Initialize ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        // Initialize explicit wait with 10 seconds timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testPageStructure() {
        // Navigate to the main page
        driver.get(BASE_URL);

        // Verify page title
        WebElement pageTitle = driver.findElement(By.tagName("h3"));
        assertEquals("Dynamically Loaded Page Elements", pageTitle.getText());

        // Verify presence of example links
        WebElement example1Link = driver.findElement(
            By.linkText("Example 1: Element on page that is hidden"));
        WebElement example2Link = driver.findElement(
            By.linkText("Example 2: Element rendered after the fact"));

        // Assert links are displayed and have correct text
        assertTrue("Example 1 link should be visible", example1Link.isDisplayed());
        assertTrue("Example 2 link should be visible", example2Link.isDisplayed());
    }

    @Test
    public void testExample1HiddenElement() {
        // Navigate to Example 1
        driver.get(BASE_URL + "/1");

        // Verify presence of Start button
        WebElement startButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("#start button"))
        );

        // Verify Hello World text exists but is hidden
        WebElement finishElement = driver.findElement(By.id("finish"));
        assertFalse("Element should be hidden initially", finishElement.isDisplayed());

        // Click Start button
        startButton.click();

        // Verify loading indicator appears
        WebElement loadingIndicator = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loading"))
        );
        assertTrue("Loading indicator should be visible", loadingIndicator.isDisplayed());

        // Wait for Hello World text to become visible
        WebElement helloWorldText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4"))
        );
        assertEquals("Hello World!", helloWorldText.getText());
    }

    @Test
    public void testExample2RenderedElement() {
        // Navigate to Example 2
        driver.get(BASE_URL + "/2");

        // Verify presence of Start button
        WebElement startButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("#start button"))
        );

        // Verify Hello World text is not present initially
        assertTrue("Element should not exist initially", 
            driver.findElements(By.cssSelector("#finish h4")).isEmpty());

        // Click Start button
        startButton.click();

        // Verify loading indicator appears
        WebElement loadingIndicator = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("loading"))
        );
        assertTrue("Loading indicator should be visible", loadingIndicator.isDisplayed());

        // Wait for loading indicator to disappear
        wait.until(
            ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))
        );

        // Verify Hello World text appears and has correct content
        WebElement helloWorldText = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4"))
        );
        assertEquals("Hello World!", helloWorldText.getText());
    }

    @Test
    public void testLoadingIndicatorBehavior() {
        // Test loading indicator behavior for both examples
        String[] examples = {"/1", "/2"};
        
        for (String example : examples) {
            driver.get(BASE_URL + example);
            
            // Click Start button
            WebElement startButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#start button"))
            );
            startButton.click();

            // Verify loading indicator appears and then disappears
            WebElement loadingIndicator = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("loading"))
            );
            assertTrue("Loading indicator should be visible", loadingIndicator.isDisplayed());

            wait.until(
                ExpectedConditions.invisibilityOfElementLocated(By.id("loading"))
            );
        }
    }

    @After
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}