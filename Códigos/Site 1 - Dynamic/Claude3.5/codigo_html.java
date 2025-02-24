package site1_dynamic.claude3_5;

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
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_controls";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        // Initialize explicit wait with 10 seconds timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL);
    }

    @Test
    public void testPageStructure() {
        // Verify page title
        WebElement title = driver.findElement(By.tagName("h4"));
        Assert.assertEquals(title.getText(), "Dynamic Controls",
            "Page title is incorrect");

        // Verify page description
        WebElement description = driver.findElement(By.cssSelector("p"));
        Assert.assertTrue(description.getText().contains("demonstrates when elements"),
            "Page description is incorrect");

        // Verify checkbox section exists
        WebElement checkboxForm = driver.findElement(By.id("checkbox-example"));
        WebElement checkbox = checkboxForm.findElement(By.cssSelector("input[type='checkbox']"));
        WebElement removeButton = checkboxForm.findElement(By.cssSelector("button"));
        
        Assert.assertTrue(checkbox.isDisplayed(), "Checkbox is not visible");
        Assert.assertEquals(removeButton.getText(), "Remove",
            "Remove button text is incorrect");

        // Verify input section exists
        WebElement inputForm = driver.findElement(By.id("input-example"));
        WebElement textInput = inputForm.findElement(By.cssSelector("input[type='text']"));
        WebElement enableButton = inputForm.findElement(By.cssSelector("button"));

        Assert.assertTrue(textInput.isDisplayed(), "Text input is not visible");
        Assert.assertFalse(textInput.isEnabled(), "Text input should be disabled by default");
        Assert.assertEquals(enableButton.getText(), "Enable",
            "Enable button text is incorrect");
    }

    @Test
    public void testCheckboxRemovalAndAddition() {
        // Get initial elements
        WebElement checkboxForm = driver.findElement(By.id("checkbox-example"));
        WebElement removeButton = checkboxForm.findElement(By.cssSelector("button"));

        // Click Remove button
        removeButton.click();

        // Wait for loading message and verify it
        WebElement loadingMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("loading")));
        Assert.assertTrue(loadingMessage.getText().contains("Wait for it..."),
            "Loading message not displayed");

        // Wait for checkbox to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector("#checkbox input[type='checkbox']")));

        // Verify success message
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("message")));
        Assert.assertEquals(message.getText(), "It's gone!",
            "Removal confirmation message is incorrect");

        // Verify button text changed to Add
        Assert.assertEquals(removeButton.getText(), "Add",
            "Button text did not change to Add");

        // Click Add button
        removeButton.click();

        // Wait for loading message again
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loading")));

        // Wait for checkbox to reappear
        WebElement newCheckbox = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#checkbox")));
        Assert.assertTrue(newCheckbox.isDisplayed(),
            "Checkbox did not reappear");

        // Verify success message
        message = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("message")));
        Assert.assertEquals(message.getText(), "It's back!",
            "Addition confirmation message is incorrect");
    }

    @Test
    public void testEnableDisableFunctionality() {
        // Get initial elements
        WebElement inputForm = driver.findElement(By.id("input-example"));
        WebElement textInput = inputForm.findElement(By.cssSelector("input[type='text']"));
        WebElement enableButton = inputForm.findElement(By.cssSelector("button"));

        // Verify initial state
        Assert.assertFalse(textInput.isEnabled(),
            "Text input should be disabled initially");

        // Click Enable button
        enableButton.click();

        // Wait for loading message and verify it
        WebElement loadingMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("loading")));
        Assert.assertTrue(loadingMessage.getText().contains("Wait for it..."),
            "Loading message not displayed");

        // Wait for loading to complete and verify input is enabled
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading")));
        wait.until(ExpectedConditions.elementToBeClickable(textInput));
        
        Assert.assertTrue(textInput.isEnabled(),
            "Text input should be enabled after clicking Enable");

        // Verify success message
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("message")));
        Assert.assertEquals(message.getText(), "It's enabled!",
            "Enable confirmation message is incorrect");

        // Test disabling the input
        enableButton.click();
        
        // Wait for loading to complete
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading")));
        
        // Verify input is disabled again
        Assert.assertFalse(textInput.isEnabled(),
            "Text input should be disabled after clicking Disable");

        // Verify success message
        message = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("message")));
        Assert.assertEquals(message.getText(), "It's disabled!",
            "Disable confirmation message is incorrect");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}