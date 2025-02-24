package site3_dynamic.claude3_5;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class codigo_html {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_loading";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL);
    }

    @Test
    public void testPageStructure() {
        // Verify page title and description
        WebElement title = driver.findElement(By.tagName("h3"));
        Assert.assertEquals(title.getText(), "Dynamically Loaded Page Elements",
            "Page title is incorrect");

        WebElement description = driver.findElement(By.cssSelector(".example > p:first-of-type"));
        Assert.assertTrue(description.getText().contains("common to see an action"),
            "Page description is incorrect");

        // Verify Example 1 link
        WebElement example1Link = driver.findElement(By.cssSelector("a[href='/dynamic_loading/1']"));
        Assert.assertTrue(example1Link.isDisplayed(), "Example 1 link is not visible");
        Assert.assertTrue(example1Link.getText().contains("Element on page that is hidden"),
            "Example 1 link text is incorrect");

        // Verify Example 2 link
        WebElement example2Link = driver.findElement(By.cssSelector("a[href='/dynamic_loading/2']"));
        Assert.assertTrue(example2Link.isDisplayed(), "Example 2 link is not visible");
        Assert.assertTrue(example2Link.getText().contains("Element rendered after the fact"),
            "Example 2 link text is incorrect");
    }

    @Test
    public void testHiddenElementBehavior() {
        // Click Example 1 link
        driver.findElement(By.cssSelector("a[href='/dynamic_loading/1']")).click();

        // Verify navigation
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/dynamic_loading/1"),
            "Navigation to Example 1 failed");

        // Verify Start button presence
        WebElement startButton = driver.findElement(By.cssSelector("#start button"));
        Assert.assertTrue(startButton.isDisplayed(), "Start button is not visible");

        // Verify hidden element exists in DOM but is not visible
        WebElement hiddenElement = driver.findElement(By.cssSelector("#finish h4"));
        Assert.assertFalse(hiddenElement.isDisplayed(),
            "Hello World text should be hidden initially");

        // Click Start button
        startButton.click();

        // Verify loading indicator appears and then disappears
        WebElement loading = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("loading")));
        Assert.assertTrue(loading.isDisplayed(), "Loading indicator not shown");
        
        wait.until(ExpectedConditions.invisibilityOf(loading));

        // Verify Hello World text is now visible
        WebElement visibleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#finish h4")));
        Assert.assertEquals(visibleElement.getText(), "Hello World!",
            "Hello World text is incorrect");
    }

    @Test
    public void testRenderedElementBehavior() {
        // Click Example 2 link
        driver.findElement(By.cssSelector("a[href='/dynamic_loading/2']")).click();

        // Verify navigation
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/dynamic_loading/2"),
            "Navigation to Example 2 failed");

        // Verify Start button presence
        WebElement startButton = driver.findElement(By.cssSelector("#start button"));
        Assert.assertTrue(startButton.isDisplayed(), "Start button is not visible");

        // Verify element is not present in DOM initially
        Assert.assertEquals(driver.findElements(By.cssSelector("#finish h4")).size(), 0,
            "Hello World element should not exist initially");

        // Click Start button
        startButton.click();

        // Verify loading indicator appears and then disappears
        WebElement loading = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("loading")));
        Assert.assertTrue(loading.isDisplayed(), "Loading indicator not shown");
        
        wait.until(ExpectedConditions.invisibilityOf(loading));

        // Wait for and verify Hello World text appears
        WebElement renderedElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#finish h4")));
        Assert.assertEquals(renderedElement.getText(), "Hello World!",
            "Hello World text is incorrect");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}