import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class LoginPageTest(unittest.TestCase):
    def setUp(self):
        # Initialize the WebDriver (using Chrome in this example)
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        self.base_url = "https://wavingtest.github.io/system-healing-test/"
        self.wait = WebDriverWait(self.driver, 10)
        
    def tearDown(self):
        # Close the browser after each test
        self.driver.quit()

    def test_page_structure(self):
        """Test to verify the page structure and content"""
        driver = self.driver
        driver.get(self.base_url)

        # Verify left column content
        left_column_text = driver.find_element(By.CSS_SELECTOR, ".left-column").text
        expected_text = "Seja bem vindo, acesse e aproveite todo o conteúdo, somos uma equipe de profissionais empenhados em trazer o melhor conteúdo direcionado a você, usuário."
        self.assertEqual(left_column_text, expected_text)

        # Verify right column elements
        greeting = driver.find_element(By.CSS_SELECTOR, ".greeting").text
        self.assertEqual(greeting, "Olá! Seja bem vindo. faça o seu login agora")

        # Verify form elements exist
        self.assertTrue(driver.find_element(By.ID, "username").is_displayed())
        self.assertTrue(driver.find_element(By.ID, "password").is_displayed())
        self.assertTrue(driver.find_element(By.ID, "login-button").is_displayed())
        
        # Verify links exist
        self.assertTrue(driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?").is_displayed())
        self.assertTrue(driver.find_element(By.LINK_TEXT, "Criar uma conta").is_displayed())

        # Verify social media icons
        social_icons = driver.find_elements(By.CSS_SELECTOR, ".social-icons a")
        self.assertEqual(len(social_icons), 4)

    def test_login_functionality(self):
        """Test login functionality with correct and incorrect credentials"""
        driver = self.driver
        driver.get(self.base_url)

        # Test successful login
        self.perform_login("correct_user", "correct_password")
        success_message = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".success-message"))
        )
        self.assertTrue(success_message.is_displayed())

        # Test failed login
        driver.get(self.base_url)  # Reset to login page
        self.perform_login("wrong_user", "wrong_password")
        error_message = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".error-message"))
        )
        self.assertTrue(error_message.is_displayed())

    def test_forgot_password_redirect(self):
        """Test the forgot password link redirection"""
        driver = self.driver
        driver.get(self.base_url)
        
        forgot_password_link = driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?")
        forgot_password_link.click()
        
        self.wait.until(EC.url_contains("forgot-password"))
        self.assertIn("forgot-password", driver.current_url)

    def test_create_account_redirect(self):
        """Test the create account link redirection"""
        driver = self.driver
        driver.get(self.base_url)
        
        create_account_link = driver.find_element(By.LINK_TEXT, "Criar uma conta")
        create_account_link.click()
        
        self.wait.until(EC.url_contains("create-account"))
        self.assertIn("create-account", driver.current_url)

    def test_social_media_redirects(self):
        """Test social media icon redirections"""
        driver = self.driver
        driver.get(self.base_url)
        
        # Dictionary of social media platforms and their expected URLs
        social_media = {
            "facebook": "facebook.com",
            "google": "google.com",
            "twitter": "twitter.com",
            "github": "github.com"
        }

        # Test each social media icon
        for platform, expected_url in social_media.items():
            icon = driver.find_element(By.CSS_SELECTOR, f".social-icons .{platform}-icon")
            icon.click()
            
            # Switch to new tab if opened
            self.wait.until(lambda d: len(d.window_handles) > 1)
            driver.switch_to.window(driver.window_handles[-1])
            
            # Verify URL contains expected platform domain
            self.wait.until(EC.url_contains(expected_url))
            self.assertIn(expected_url, driver.current_url)
            
            # Close new tab and switch back to main window
            driver.close()
            driver.switch_to.window(driver.window_handles[0])

    def perform_login(self, username, password):
        """Helper method to perform login"""
        username_field = self.driver.find_element(By.ID, "username")
        password_field = self.driver.find_element(By.ID, "password")
        login_button = self.driver.find_element(By.ID, "login-button")

        username_field.clear()
        username_field.send_keys(username)
        password_field.clear()
        password_field.send_keys(password)
        login_button.click()

if __name__ == "__main__":
    unittest.main()


"""
This test suite includes:

A setUp method to initialize the WebDriver and common variables
A tearDown method to clean up after each test
Individual test methods for:
Page structure verification
Login functionality (success and failure cases)
Forgot password link redirection
Create account link redirection
Social media icon redirections
A helper method for performing login
Key features:

Uses explicit waits to handle dynamic content
Includes comprehensive assertions to verify expected behavior
Handles multiple browser windows/tabs for social media redirects
Follows page object model principles
Includes detailed comments explaining each test
Uses various locator strategies (ID, CSS Selector, Link Text)
To run the tests, you would need to:

Install the required packages:
BASH

pip install selenium
pip install webdriver_manager
Make sure you have the appropriate WebDriver installed for your browser

Run the test suite:

BASH

python -m unittest login_page_test.py
Note: You might need to adjust the locators (IDs, class names, etc.) to match your actual implementation, as the ones used in this example are assumptions based on common naming conventions.

"""