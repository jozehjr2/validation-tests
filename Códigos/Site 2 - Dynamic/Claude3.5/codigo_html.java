package site2_dynamic.claude3_5;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class codigo_html {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_content";
    private static final String STATIC_URL = BASE_URL + "?with_content=static";

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testPageStructure() {
        driver.get(BASE_URL);

        // Verify page title and description
        WebElement title = driver.findElement(By.tagName("h3"));
        Assert.assertEquals(title.getText(), "Dynamic Content",
            "Page title is incorrect");

        WebElement description = driver.findElement(By.cssSelector(".example > p:first-of-type"));
        Assert.assertTrue(description.getText().contains("demonstrates the ever-evolving nature"),
            "Page description is incorrect");

        // Verify static content link
        WebElement staticLink = driver.findElement(By.cssSelector("a[href*='with_content=static']"));
        Assert.assertTrue(staticLink.isDisplayed(), "Static content link is not visible");
        Assert.assertTrue(staticLink.getAttribute("href").contains("with_content=static"),
            "Static content link href is incorrect");

        // Verify presence of three content rows
        List<WebElement> contentRows = driver.findElements(By.cssSelector("#content .row .row"));
        Assert.assertEquals(contentRows.size(), 3,
            "Expected 3 content rows but found " + contentRows.size());

        // Verify each row has an image and text
        for (int i = 0; i < contentRows.size(); i++) {
            WebElement row = contentRows.get(i);
            WebElement image = row.findElement(By.tagName("img"));
            WebElement text = row.findElement(By.cssSelector(".large-10.columns"));
            
            Assert.assertTrue(image.isDisplayed(),
                "Image in row " + (i + 1) + " is not visible");
            Assert.assertTrue(text.getText().length() > 0,
                "Text in row " + (i + 1) + " is empty");
        }
    }

    @Test
    public void testDynamicContentBehavior() {
        driver.get(BASE_URL);

        // Store initial content
        List<String> initialImages = new ArrayList<>();
        List<String> initialTexts = new ArrayList<>();
        captureContent(initialImages, initialTexts);

        // Refresh page multiple times and verify content changes
        for (int i = 0; i < 3; i++) {
            driver.navigate().refresh();
            
            List<String> newImages = new ArrayList<>();
            List<String> newTexts = new ArrayList<>();
            captureContent(newImages, newTexts);

            // Verify at least some content has changed
            boolean hasChanges = false;
            for (int j = 0; j < 3; j++) {
                if (!initialImages.get(j).equals(newImages.get(j)) || 
                    !initialTexts.get(j).equals(newTexts.get(j))) {
                    hasChanges = true;
                    break;
                }
            }
            
            Assert.assertTrue(hasChanges,
                "Content did not change after refresh " + (i + 1));

            // Update initial content for next iteration
            initialImages = newImages;
            initialTexts = newTexts;
        }
    }

    @Test
    public void testStaticContentBehavior() {
        driver.get(STATIC_URL);

        // Store initial content
        List<String> initialImages = new ArrayList<>();
        List<String> initialTexts = new ArrayList<>();
        captureContent(initialImages, initialTexts);

        // Refresh page multiple times and verify some content remains static
        for (int i = 0; i < 3; i++) {
            driver.navigate().refresh();
            
            List<String> newImages = new ArrayList<>();
            List<String> newTexts = new ArrayList<>();
            captureContent(newImages, newTexts);

            // Verify that at least some content remains unchanged
            boolean hasStaticContent = false;
            for (int j = 0; j < 3; j++) {
                if (initialImages.get(j).equals(newImages.get(j)) || 
                    initialTexts.get(j).equals(newTexts.get(j))) {
                    hasStaticContent = true;
                    break;
                }
            }
            
            Assert.assertTrue(hasStaticContent,
                "No static content found after refresh " + (i + 1));
        }
    }

    private void captureContent(List<String> images, List<String> texts) {
        List<WebElement> contentRows = driver.findElements(By.cssSelector("#content .row .row"));
        for (WebElement row : contentRows) {
            WebElement image = row.findElement(By.tagName("img"));
            WebElement text = row.findElement(By.cssSelector(".large-10.columns"));
            
            images.add(image.getAttribute("src"));
            texts.add(text.getText().trim());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}