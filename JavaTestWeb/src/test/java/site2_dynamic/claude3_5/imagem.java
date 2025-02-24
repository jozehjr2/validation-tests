package site2_dynamic.claude3_5;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.After;
import org.junit.Assert;
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
    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_content";

    @Before
    public void setUp() {
        // Setup WebDriver
        System.setProperty("webdriver.chrome.driver", "src\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testPageStructure() {
        driver.get(BASE_URL);

        // Test 1: Verify page title and subtitle
        WebElement title = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h3[text()='Dynamic Content']")));
        Assert.assertTrue("Title should be visible", title.isDisplayed());

        WebElement subtitle = driver.findElement(
            By.xpath("//div[@class='example']/p[1]"));
        Assert.assertTrue("Subtitle should contain expected text",
            subtitle.getText().contains("demonstrates the ever-evolving nature of content"));

        // Test 2: Verify presence of three images and text blocks
        List<WebElement> images = driver.findElements(
            By.xpath("(//div[@id='content'])[2]"));
        Assert.assertEquals("Should have exactly 3 images", 3, images.size());

        List<WebElement> textBlocks = driver.findElements(
            By.xpath("//div[@class='row']//div[@class='large-10 columns']"));
        Assert.assertEquals("Should have exactly 3 text blocks", 3, textBlocks.size());

        // Test 3: Verify "click here" link
        WebElement clickHereLink = driver.findElement(By.linkText("click here"));
        Assert.assertTrue("Click here link should be visible", clickHereLink.isDisplayed());
        Assert.assertTrue("Link should contain correct URL",
            clickHereLink.getAttribute("href").contains("with_content=static"));
    }

    @Test
    public void testDynamicContent() {
        driver.get(BASE_URL);

        // Capture initial content
        List<ContentItem> initialContent = captureCurrentContent();

        // Refresh page and verify content changes
        for (int i = 0; i < 3; i++) {
            driver.navigate().refresh();
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[@class='row']//img")));

            List<ContentItem> newContent = captureCurrentContent();
            boolean hasChanged = !compareContent(initialContent, newContent);
            Assert.assertTrue("Content should change after refresh", hasChanged);

            initialContent = newContent;
        }
    }

    @Test
    public void testStaticContent() {
        // Navigate to static content version
        driver.get(BASE_URL + "?with_content=static");

        // Capture initial static content
        List<ContentItem> initialStaticContent = captureCurrentContent();

        // Refresh and verify content remains unchanged
        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@class='row']//img")));

        List<ContentItem> newStaticContent = captureCurrentContent();
        Assert.assertTrue("Static content should remain unchanged",
            compareContent(initialStaticContent, newStaticContent));
    }

    private List<ContentItem> captureCurrentContent() {
        List<ContentItem> content = new ArrayList<>();
        List<WebElement> images = driver.findElements(
            By.xpath("//div[@class='row']//img"));
        List<WebElement> texts = driver.findElements(
            By.xpath("//div[@class='row']//div[@class='large-10 columns']"));

        for (int i = 0; i < images.size(); i++) {
            content.add(new ContentItem(
                images.get(i).getAttribute("src"),
                texts.get(i).getText()
            ));
        }
        return content;
    }

    private boolean compareContent(List<ContentItem> content1, List<ContentItem> content2) {
        if (content1.size() != content2.size()) return false;
        for (int i = 0; i < content1.size(); i++) {
            if (!content1.get(i).equals(content2.get(i))) return false;
        }
        return true;
    }

    // Helper class to store content items
    private static class ContentItem {
        private final String imageSrc;
        private final String text;

        public ContentItem(String imageSrc, String text) {
            this.imageSrc = imageSrc;
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ContentItem that = (ContentItem) o;
            return Objects.equals(imageSrc, that.imageSrc) &&
                   Objects.equals(text, that.text);
        }

        @Override
        public int hashCode() {
            return Objects.hash(imageSrc, text);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}