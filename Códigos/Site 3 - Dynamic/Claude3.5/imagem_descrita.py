import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class DynamicLoadingTests(unittest.TestCase):
    def setUp(self):
        # Initialize the Chrome WebDriver
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        # Set base URL
        self.base_url = "https://the-internet.herokuapp.com/dynamic_loading"
        # Set default wait time for explicit waits
        self.wait = WebDriverWait(self.driver, 10)

    def tearDown(self):
        # Close the browser after each test
        self.driver.quit()

    def test_page_structure(self):
        """Test to validate the main page structure and elements"""
        self.driver.get(self.base_url)
        
        # Verify page title
        page_header = self.driver.find_element(By.TAG_NAME, "h3")
        self.assertEqual(page_header.text, "Dynamically Loaded Page Elements")
        
        # Verify presence of both example links
        example_links = self.driver.find_elements(By.TAG_NAME, "a")
        self.assertTrue(any("Example 1" in link.text for link in example_links))
        self.assertTrue(any("Example 2" in link.text for link in example_links))

    def test_example1_hidden_element(self):
        """Test Example 1: Element hidden on page"""
        # Navigate to Example 1
        self.driver.get(f"{self.base_url}/1")
        
        # Verify initial state
        start_button = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "#start button"))
        )
        
        # Check if Hello World text is present but hidden
        hidden_element = self.driver.find_element(By.CSS_SELECTOR, "#finish")
        self.assertFalse(hidden_element.is_displayed())
        
        # Click Start button
        start_button.click()
        
        # Wait for loading to complete and verify text
        hello_text = self.wait.until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, "#finish h4"))
        )
        self.assertEqual(hello_text.text, "Hello World!")

    def test_example2_rendered_element(self):
        """Test Example 2: Element rendered after the fact"""
        # Navigate to Example 2
        self.driver.get(f"{self.base_url}/2")
        
        # Verify initial state
        start_button = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "#start button"))
        )
        
        # Verify Hello World text is not present initially
        try:
            self.driver.find_element(By.CSS_SELECTOR, "#finish h4")
            initial_text_present = True
        except:
            initial_text_present = False
        self.assertFalse(initial_text_present)
        
        # Click Start button
        start_button.click()
        
        # Wait for loading to complete and verify text
        hello_text = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "#finish h4"))
        )
        self.assertEqual(hello_text.text, "Hello World!")

    def test_loading_indicator(self):
        """Test loading indicator functionality for both examples"""
        for example in ["1", "2"]:
            with self.subTest(example=example):
                # Navigate to example
                self.driver.get(f"{self.base_url}/{example}")
                
                # Click Start button
                start_button = self.wait.until(
                    EC.presence_of_element_located((By.CSS_SELECTOR, "#start button"))
                )
                start_button.click()
                
                # Verify loading indicator appears
                try:
                    loading = self.wait.until(
                        EC.presence_of_element_located((By.CSS_SELECTOR, "#loading"))
                    )
                    self.assertTrue(loading.is_displayed())
                except TimeoutException:
                    self.fail(f"Loading indicator not found in Example {example}")
                
                # Wait for loading to complete
                self.wait.until(
                    EC.invisibility_of_element_located((By.CSS_SELECTOR, "#loading"))
                )

if __name__ == "__main__":
    unittest.main()