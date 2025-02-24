package site2_dynamic;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class imagem_descrita {
    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\arian\\Documents\\Thinklogic\\Tools\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");
    }

    @Test(description = "Validate Page Structure")
    public void validatePageStructure() {
        WebElement title = driver.findElement(By.id("title"));
        Assert.assertTrue(title.isDisplayed(), "Title is not displayed.");

        WebElement subTitle = driver.findElement(By.cssSelector("#wirepoints h3"));
        Assert.assertTrue(subTitle.isDisplayed(), "Subtitle is not displayed.");

        WebElement checkbox = driver.findElement(By.id("checkbox"));
        Assert.assertTrue(checkbox.isDisplayed(), "Checkbox is not displayed.");

        WebElement removeButton = driver.findElement(By.id("remove-button"));
        Assert.assertTrue(removeButton.isDisplayed(), "Remove button is not displayed.");

        WebElement textField = driver.findElement(By.id("input-example"));
        Assert.assertFalse(textField.isEnabled(), "TextField should be disabled by default.");
    }

    @Test(description = "Test Checkbox Removal and Addition")
    public void testCheckboxRemovalAndAddition() {
        WebElement removeButton = driver.findElement(By.id("remove-button"));
        removeButton.click();

        // Wait for the checkbox to disappear using implicit wait
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfElementLocated(By.id("checkbox")));

        WebElement addButton = driver.findElement(By.id("add-button"));
        Assert.assertEquals(addButton.getText(), "Add", "Button text should be 'Add' after removal.");
        addButton.click();

        // Wait for the checkbox to reappear using implicit wait
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
    }

    @Test(description = "Test Enable/Disable Functionality")
    public void testEnableDisableFunctionality() {
        // Find the 'Enable' button and click it
        WebElement enableButton = driver.findElement(By.id("enable-button"));
        enableButton.click();

        // Wait for the message to appear using implicit wait
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Wait for it...')]")));

        // Find the loading bar and verify its visibility
        WebElement loadingBar = driver.findElement(By.id("loading-bar"));
        Assert.assertTrue(loadingBar.isDisplayed(), "Loading bar should be displayed during the process.");

        // Wait for the text field to be enabled using implicit wait
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("input-example"))));

        // Find the text field and verify its enabling
        WebElement textField = driver.findElement(By.id("input-example"));
        Assert.assertTrue(textField.isEnabled(), "TextField should be enabled after the process is complete.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
