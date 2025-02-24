package site1_dynamic.gpt4;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class codigo_html {

    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_controls";

    public static void main(String[] args) {
        // Set up Chrome WebDriver
        //System.setProperty("webdriver.chrome.driver", "path_to_chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get(BASE_URL);

            // Validate Page Structure
            validatePageStructure(driver);
            
            // Test Checkbox Removal and Addition
            testCheckboxRemovalAddition(driver, wait);

            // Test Enable/Disable Functionality
            testEnableDisableFunctionality(driver, wait);

        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void validatePageStructure(WebDriver driver) {
        // Check for title and subtitle
        assertElementPresent(driver, By.xpath("//h4[text()='Dynamic Controls']"), "Title");
        assertElementPresent(driver, By.xpath("//p[text()='This example demonstrates when elements (e.g., checkbox, input field, etc.) are changed asynchronously.']"), "Subtitle");

        // Check for checkbox and remove button
        assertElementPresent(driver, By.id("checkbox"), "Checkbox");
        assertElementPresent(driver, By.xpath("//form[@id='checkbox-example']//button[text()='Remove']"), "Remove button");

        // Check that the text field is disabled by default
        WebElement inputField = driver.findElement(By.xpath("//form[@id='input-example']/input"));
        if (!inputField.getAttribute("disabled").equals("true")) {
            throw new AssertionError("Text field is not disabled by default.");
        }
    }

    private static void testCheckboxRemovalAddition(WebDriver driver, WebDriverWait wait) {
        WebElement removeButton = driver.findElement(By.xpath("//form[@id='checkbox-example']//button"));
        removeButton.click();

        // Verify the checkbox disappears and button text changes to "Add"
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("checkbox")));
        assertElementPresent(driver, By.xpath("//form[@id='checkbox-example']//button[text()='Add']"), "Add button");

        // Click "Add" and verify the checkbox reappears
        WebElement addButton = driver.findElement(By.xpath("//form[@id='checkbox-example']//button[text()='Add']"));
        addButton.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkbox")));
        assertElementPresent(driver, By.id("checkbox"), "Checkbox after addition");
    }

    private static void testEnableDisableFunctionality(WebDriver driver, WebDriverWait wait) {
        WebElement enableButton = driver.findElement(By.xpath("//form[@id='input-example']/button"));
        enableButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("message")));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("message"), "It's enabled!"));

        // Wait for the text field to be enabled
        WebElement inputField = driver.findElement(By.xpath("//input[@type='text']"));
        wait.until(ExpectedConditions.attributeToBe(inputField, "disabled", "false"));

        if (inputField.getAttribute("disabled") != null) {
            throw new AssertionError("Text field is not enabled after the process.");
        }
    }

    private static void assertElementPresent(WebDriver driver, By locator, String elementName) {
        if (driver.findElements(locator).isEmpty()) {
            throw new AssertionError(elementName + " is not present on the page.");
        }
    }
}