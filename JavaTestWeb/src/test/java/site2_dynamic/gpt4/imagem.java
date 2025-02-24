package site2_dynamic.gpt4;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class imagem {

    public static void main(String[] args) {
        // Set up WebDriver (assuming ChromeDriver is in your PATH)
        //System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // 1. Validate Page Structure
            driver.get("https://the-internet.herokuapp.com/dynamic_content");

            // Check for Page Header
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[text()='Dynamic Content']")));
            assert title.isDisplayed() : "Title is not displayed";

            // Check the presence of three images and their respective text
            List<WebElement> images = driver.findElements(By.xpath("//div[@class='row']//img"));
            assert images.size() == 3 : "There should be exactly three images";

            List<WebElement> texts = driver.findElements(By.xpath("//div[@class='row']//div[@class='large-10 columns']"));
            assert texts.size() == 3 : "There should be exactly three text sections";

            // Check the "click here" link
            WebElement clickHereLink = driver.findElement(By.linkText("click here"));
            assert clickHereLink.getAttribute("href").contains("https://the-internet.herokuapp.com/dynamic_content?with_content=static") : 
                "'click here' link does not have the correct href.";

            // 2. Test Dynamic Content Behavior
            String[] initialImagesTexts = new String[3];
            for (int i = 0; i < 3; i++) {
                initialImagesTexts[i] = images.get(i).getAttribute("src") + texts.get(i).getText();
            }

            // Refresh the page and check for changes
            driver.navigate().refresh();
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            List<WebElement> updatedImages = driver.findElements(By.xpath("//div[@class='row']//img"));
            List<WebElement> updatedTexts = driver.findElements(By.xpath("//div[@class='row']//div[@class='large-10 columns']"));
            String[] updatedImagesTexts = new String[3];
            for (int i = 0; i < 3; i++) {
                updatedImagesTexts[i] = updatedImages.get(i).getAttribute("src") + updatedTexts.get(i).getText();
            }

            boolean contentChanged = false;
            for (int i = 0; i < 3; i++) {
                if (!initialImagesTexts[i].equals(updatedImagesTexts[i])) {
                    contentChanged = true;
                    break;
                }
            }
            assert contentChanged : "Content did not change after refreshing the page.";

            // 3. Test Static Content Behavior
            clickHereLink.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='row']//img")));

            // Capture initial static content
            List<WebElement> staticImages = driver.findElements(By.xpath("//div[@class='row']//img"));
            List<WebElement> staticTexts = driver.findElements(By.xpath("//div[@class='row']//div[@class='large-10 columns']"));
            String[] initialStaticImagesTexts = new String[3];
            for (int i = 0; i < 3; i++) {
                initialStaticImagesTexts[i] = staticImages.get(i).getAttribute("src") + staticTexts.get(i).getText();
            }

            // Refresh and verify that the content remains the same
            driver.navigate().refresh();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='row']//img")));

            List<WebElement> refreshedStaticImages = driver.findElements(By.xpath("//div[@class='row']//img"));
            List<WebElement> refreshedStaticTexts = driver.findElements(By.xpath("//div[@class='row']//div[@class='large-10 columns']"));
            String[] refreshedStaticImagesTexts = new String[3];
            for (int i = 0; i < 3; i++) {
                refreshedStaticImagesTexts[i] = refreshedStaticImages.get(i).getAttribute("src") + refreshedStaticTexts.get(i).getText();
            }

            boolean staticContentUnchanged = true;
            for (int i = 0; i < 3; i++) {
                if (!initialStaticImagesTexts[i].equals(refreshedStaticImagesTexts[i])) {
                    staticContentUnchanged = false;
                    break;
                }
            }
            assert staticContentUnchanged : "Static content did not remain unchanged after refreshing.";

        } finally {
            // Close the WebDriver instance
            driver.quit();
        }
    }
}