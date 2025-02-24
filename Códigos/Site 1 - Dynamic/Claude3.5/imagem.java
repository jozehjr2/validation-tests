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

public class imagem {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_controls";

    @BeforeClass
    public void setUp() {
        // Initialize WebDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL);
    }

    @Test(priority = 1)
    public void testPageStructure() {
        // Test Case 1: Validate initial page structure
        
        // Verify page title and header
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("The Internet"));
        
        WebElement header = driver.findElement(By.cssSelector("h4"));
        Assert.assertTrue(header.isDisplayed());
        Assert.assertEquals(header.getText(), "Dynamic Controls");

        // Verify checkbox section
        WebElement checkbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
        Assert.assertTrue(checkbox.isDisplayed(), "Checkbox should be visible");

        WebElement removeButton = driver.findElement(By.cssSelector("button[onclick='swapCheckbox()']"));
        Assert.assertTrue(removeButton.isDisplayed(), "Remove button should be visible");

        // Verify input field section
        WebElement textField = driver.findElement(By.cssSelector("input[type='text']"));
        Assert.assertFalse(textField.isEnabled(), "Text field should be disabled by default");
    }

    @Test(priority = 2)
    public void testCheckboxFunctionality() {
        // Test Case 2: Test checkbox removal and addition
        
        // Initial state verification
        WebElement checkbox = driver.findElement(By.cssSelector("input[type='checkbox']"));
        WebElement removeButton = driver.findElement(By.cssSelector("button[onclick='swapCheckbox()']"));
        
        // Test Remove functionality
        removeButton.click();
        wait.until(ExpectedConditions.invisibilityOf(checkbox));
        
        // Verify button text changed to "Add"
        WebElement addButton = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick='swapCheckbox()']"))
        );
        Assert.assertEquals(addButton.getText(), "Add", "Button text should change to 'Add'");
        
        // Test Add functionality
        addButton.click();
        WebElement newCheckbox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='checkbox']"))
        );
        Assert.assertTrue(newCheckbox.isDisplayed(), "Checkbox should reappear");
    }

    @Test(priority = 3)
    public void testInputFieldFunctionality() {
        // Test Case 3: Test enable/disable functionality
        
        // Initial state verification
        WebElement textField = driver.findElement(By.cssSelector("input[type='text']"));
        Assert.assertFalse(textField.isEnabled(), "Text field should be disabled initially");
        
        // Click Enable button
        WebElement enableButton = driver.findElement(By.cssSelector("button[onclick='swapInput()']"));
        enableButton.click();
        
        
        // Verify text field becomes enabled
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='text']")));
        Assert.assertTrue(textField.isEnabled(), "It's enabled!");
        
        // Test disable functionality
        WebElement disableButton = driver.findElement(By.cssSelector("button[onclick='swapInput()']"));
        disableButton.click();
        
        // Verify text field becomes disabled again
        wait.until(ExpectedConditions.not(
            ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='text']"))
        ));
        Assert.assertFalse(textField.isEnabled(), "It's disabled!");
    }

    @AfterClass
    public void tearDown() {
        // Clean up
        if (driver != null) {
            driver.quit();
        }
    }
}