package site1_dynamic.gpt4;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class imagem {
    public static void main(String[] args) {
        // Set the path to the chromedriver executable
        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        // Initialize the WebDriver
        WebDriver driver = new ChromeDriver();
        try {
            // Open the web application
            driver.get("https://the-internet.herokuapp.com/dynamic_controls");

            // Validate Page Structure
            // Check for the presence of the title and subtitle
            String pageTitle = driver.getTitle();
            assert pageTitle.contains("The Internet");

            WebElement header = driver.findElement(By.xpath("//h4[contains(text(), 'Dynamic Controls')]"));
            assert header.isDisplayed();

            // Verify the checkbox and remove button exist when the page loads
            WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
            WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(), 'Remove')]"));
            assert checkbox.isDisplayed();
            assert removeButton.isDisplayed();

            // Verify the text field is disabled by default
            WebElement textField = driver.findElement(By.xpath("//input[@type='text']"));
            assert !textField.isEnabled();

            // Test Checkbox Removal and Addition
            // Click "Remove" and verify the checkbox disappears
            removeButton.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOf(checkbox));

            // Check if the button text changes to "Add"
            WebElement addButton = driver.findElement(By.xpath("//button[contains(text(), 'Add')]"));
            assert addButton.isDisplayed();

            // Click "Add" and verify the checkbox reappears
            addButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='checkbox']")));
            checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
            assert checkbox.isDisplayed();

            // Test Enable/Disable Functionality
            // Verify that the text field is disabled by default
            assert !textField.isEnabled();

            // Click "Enable" and confirm the appearance of the message "Wait for it..."
            WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]"));
            enableButton.click();
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")));
            assert message.getText().contains("Wait for it...");

            // Ensure the loading bar appears before the text field is enabled
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("loading")));

            // Verify that the text field is enabled after the process is complete
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']")));
            assert textField.isEnabled();

        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}