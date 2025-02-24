from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import unittest
import time

class DynamicContentTest(unittest.TestCase):
    def setUp(self):
        """Set up the test environment before each test method."""
        self.driver = webdriver.Chrome()  # Make sure ChromeDriver is in PATH
        self.driver.implicitly_wait(10)
        self.wait = WebDriverWait(self.driver, 10)
        self.base_url = "https://the-internet.herokuapp.com/dynamic_content"

    def test_page_structure(self):
        """Test the basic structure and elements of the page."""
        self.driver.get(self.base_url)
        
        # Verify page title
        title = self.wait.until(
            EC.presence_of_element_located((By.XPATH, "//h3[text()='Dynamic Content']"))
        )
        self.assertTrue(title.is_displayed(), "Page title is not displayed")
        
        # Verify subtitle/description text
        subtitle = self.driver.find_element(By.XPATH, "//div[@class='example']/p[1]")
        self.assertTrue("demonstrates the ever-evolving nature of content" in subtitle.text,
                       "Subtitle text is incorrect")
        
        # Verify presence of three images
        images = self.driver.find_elements(By.XPATH, "(//div[@class='row'])[3]")
        self.assertEqual(len(images), 3, "Expected 3 images but found {}".format(len(images)))
        
        # Verify presence of three text blocks
        text_blocks = self.driver.find_elements(
            By.XPATH, "//div[@class='row']//div[@class='large-10 columns']"
        )
        self.assertEqual(len(text_blocks), 3, "Expected 3 text blocks but found {}".format(len(text_blocks)))
        
        # Verify "click here" link
        click_here = self.driver.find_element(By.LINK_TEXT, "click here")
        self.assertTrue(click_here.is_displayed(), "'Click here' link is not displayed")
        self.assertIn("with_content=static", click_here.get_attribute("href"),
                     "Incorrect href for 'click here' link")

    def test_dynamic_content(self):
        """Test that content changes on page refresh."""
        self.driver.get(self.base_url)
        
        # Capture initial content
        initial_content = self._capture_current_content()
        
        # Refresh page multiple times and verify content changes
        for _ in range(3):
            self.driver.refresh()
            time.sleep(1)  # Allow content to load
            new_content = self._capture_current_content()
            
            # Verify at least one element changed
            changes = sum(1 for i, j in zip(initial_content, new_content) if i != j)
            self.assertGreater(changes, 0, "Content did not change after refresh")
            
            initial_content = new_content

    def test_static_content(self):
        """Test that content remains static when using static parameter."""
        # Navigate to static content version
        self.driver.get(self.base_url + "?with_content=static")
        
        # Capture initial static content
        initial_static_content = self._capture_current_content()
        
        # Refresh and verify content remains the same
        self.driver.refresh()
        time.sleep(1)  # Allow content to load
        new_static_content = self._capture_current_content()
        
        # Compare content
        self.assertEqual(initial_static_content, new_static_content,
                        "Static content changed after refresh")

    def _capture_current_content(self):
        """Helper method to capture current page content."""
        images = self.driver.find_elements(By.XPATH, "//div[@class='row']//img")
        texts = self.driver.find_elements(
            By.XPATH, "//div[@class='row']//div[@class='large-10 columns']"
        )
        
        content = []
        for img, txt in zip(images, texts):
            content.append({
                'image_src': img.get_attribute('src'),
                'text': txt.text
            })
        return content

    def tearDown(self):
        """Clean up after each test method."""
        if self.driver:
            self.driver.quit()

if __name__ == "__main__":
    unittest.main(verbosity=2)