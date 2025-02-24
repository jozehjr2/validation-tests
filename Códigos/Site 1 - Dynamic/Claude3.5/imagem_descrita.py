import unittest
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class DynamicControlsTest(unittest.TestCase):
    def setUp(self):
        """Set up the test environment before each test method."""
        self.driver = webdriver.Chrome()
        self.driver.get("https://the-internet.herokuapp.com/dynamic_controls")
        self.wait = WebDriverWait(self.driver, 10)

    def tearDown(self):
        """Clean up after each test method."""
        if self.driver:
            self.driver.quit()

    def test_page_structure(self):
        """Test Case 1: Validate initial page structure and elements."""
        try:
            # Verify page title and header
            self.assertIn("Dynamic Controls", self.driver.title)
            time.sleep(2)
            header = self.driver.find_element(By.XPATH, "//h4[normalize-space()='Dynamic Controls']")
            self.assertTrue(header.is_displayed())
            self.assertEqual(header.text, "Dynamic Controls")

            # Verify checkbox section
            checkbox = self.driver.find_element(By.XPATH, "//input[@type='checkbox']")
            remove_button = self.driver.find_element(By.CSS_SELECTOR, "button[onclick='swapCheckbox()']")
            self.assertTrue(checkbox.is_displayed())
            self.assertTrue(remove_button.is_displayed())

            # Verify input field section
            text_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='text']")
            self.assertFalse(text_field.is_enabled())

        except Exception as e:
            self.fail(f"Page structure validation failed: {str(e)}")

    def test_checkbox_functionality(self):
        """Test Case 2: Test checkbox removal and addition functionality."""
        try:
            # Initial state verification
            time.sleep(2)
            checkbox = self.driver.find_element(By.CSS_SELECTOR, "input[type='checkbox']")
            remove_button = self.driver.find_element(By.CSS_SELECTOR, "button[onclick='swapCheckbox()']")
            
            # Test Remove functionality
            remove_button.click()
            self.wait.until(EC.invisibility_of_element_located(
                (By.XPATH, "//input[@type='checkbox']']")))
            
            # Verify button text changed to "Add"
            add_button = self.wait.until(EC.presence_of_element_located(
                (By.CSS_SELECTOR, "button[onclick='swapCheckbox()']")))
            self.assertEqual(add_button.text, "Add")
            
            # Test Add functionality
            add_button.click()
            checkbox = self.wait.until(EC.presence_of_element_located(
                (By.XPATH, "//input[@type='checkbox']")))
            self.assertTrue(checkbox.is_displayed())

        except Exception as e:
            self.fail(f"Checkbox functionality test failed: {str(e)}")

    def test_input_field_functionality(self):
        """Test Case 3: Test enable/disable functionality of the input field."""
        try:
            time.sleep(2)
            # Initial state verification
            text_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='text']")
            self.assertFalse(text_field.is_enabled())
            
            # Click Enable button
            enable_button = self.driver.find_element(By.XPATH, "//button[normalize-space()='Enable']")
            enable_button.click()
            
            # Verify "Wait for it..." message
            message = self.wait.until(EC.presence_of_element_located((By.ID, "message")))
            self.assertIn("Wait for it...", message.text)
            
            # Verify loading bar appears
            loading = self.wait.until(EC.presence_of_element_located((By.ID, "loading")))
            self.assertTrue(loading.is_displayed())
            
            # Verify text field becomes enabled
            self.wait.until(EC.element_to_be_clickable((By.CSS_SELECTOR, "input[type='text']")))
            text_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='text']")
            self.assertTrue(text_field.is_enabled())
            
            # Test disable functionality
            disable_button = self.driver.find_element(By.CSS_SELECTOR, "//button[normalize-space()='Disable']")
            disable_button.click()
            
            # Verify text field becomes disabled again
            self.wait.until_not(EC.element_to_be_clickable((By.CSS_SELECTOR, "input[type='text']")))
            self.assertFalse(text_field.is_enabled())

        except Exception as e:
            self.fail(f"Input field functionality test failed: {str(e)}")

def main():
    unittest.main(verbosity=2)

if __name__ == "__main__":
    main()