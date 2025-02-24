package site2_dynamic.gpt4;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class codigo_html {

    private static final String BASE_URL = "https://the-internet.herokuapp.com/dynamic_content";
    private static final String STATIC_CONTENT_URL = "https://the-internet.herokuapp.com/dynamic_content?with_content=static";

    public static void main(String[] args) {
        // Set up Chrome WebDriver
        //System.setProperty("webdriver.chrome.driver", "path_to_chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Validate Page Structure
            validatePageStructure(driver);

            // Test Dynamic Content Behavior
            testDynamicContentBehavior(driver);

            // Test Static Content Behavior
            //testStaticContentBehavior(driver);

        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static void validatePageStructure(WebDriver driver) {
        driver.get(BASE_URL);

        // Check for title and subtitle
        assertElementPresent(driver, By.xpath("//h3[text()='Dynamic Content']"), "Title");
        assertElementPresent(driver, By.xpath("//p[contains(text(), 'This example demonstrates the ever-evolving nature of content')]"), "Subtitle");

        // Check for three images and their respective text
        List<WebElement> images = driver.findElements(By.xpath("//div[@id='content']//img"));
        if (images.size() != 3) {
            throw new AssertionError("Expected 3 images, but found " + images.size());
        }

        List<WebElement> textBlocks = driver.findElements(By.xpath("//div[@id='content']//div[@class='large-10 columns']"));
        if (textBlocks.size() != 3) {
            throw new AssertionError("Expected 3 text blocks, but found " + textBlocks.size());
        }

        // Check for "click here" link with correct href attribute
        WebElement clickHereLink = driver.findElement(By.linkText("click here"));
        if (!clickHereLink.getAttribute("href").contains("with_content=static")) {
            throw new AssertionError("Click here link does not have the expected href attribute.");
        }
    }

    private static void testDynamicContentBehavior(WebDriver driver) {
        driver.get(BASE_URL);
        
        // Capture initial content
        List<String> initialContent = capturePageContent(driver);
        
        driver.navigate().refresh();
        
        // Capture post-refresh content
        List<String> refreshedContent = capturePageContent(driver);
        
        if (initialContent.equals(refreshedContent)) {
            throw new AssertionError("Content did not change after refresh.");
        }

        System.out.println("Dynamic content successfully changed after refresh.");
    }

    private static void testStaticContentBehavior(WebDriver driver) {
        driver.get(STATIC_CONTENT_URL);

        // Capture initial content
        List<String> initialContent = capturePageContent(driver);

        driver.navigate().refresh();

        // Capture post-refresh content
        List<String> refreshedContent = capturePageContent(driver);

        if (!initialContent.equals(refreshedContent)) {
            throw new AssertionError("Content changed on static content page after refresh.");
        }

        System.out.println("Static content remained unchanged after refresh.");
    }

    private static List<String> capturePageContent(WebDriver driver) {
        List<WebElement> contentElements = driver.findElements(By.xpath("//div[@id='content']//div[@class='large-10 columns']"));
        return contentElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private static void assertElementPresent(WebDriver driver, By locator, String elementName) {
        if (driver.findElements(locator).isEmpty()) {
            throw new AssertionError(elementName + " is not present on the page.");
        }
    }
}