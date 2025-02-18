import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class LoginPageTest(unittest.TestCase):
    BASE_URL = "https://wavingtest.github.io/system-healing-test/"
    
    def setUp(self):
        # Initialize WebDriver (Chrome in this example)
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        self.driver.get(self.BASE_URL)
        
        # Wait for page to load
        WebDriverWait(self.driver, 10).until(
            EC.presence_of_element_located((By.ID, "formLogin2"))
        )
    
    def tearDown(self):
        # Close browser after each test
        self.driver.quit()
    
    def test_page_structure(self):
        """Verify page structure and content"""
        # Check page title
        self.assertEqual(self.driver.title, "Login")
        
        # Verify welcome message
        welcome_text = self.driver.find_element(By.TAG_NAME, "h1").text
        self.assertEqual(welcome_text, "Olá! Seja bem vindo.")
        
        # Check login form elements
        username_input = self.driver.find_element(By.ID, "username-555")
        password_input = self.driver.find_element(By.ID, "password-88888")
        login_button = self.driver.find_element(By.ID, "btnLogin65")
        
        self.assertTrue(username_input.is_displayed())
        self.assertTrue(password_input.is_displayed())
        self.assertTrue(login_button.is_displayed())
    
    def test_successful_login(self):
        """Test login with correct credentials"""
        username_input = self.driver.find_element(By.ID, "username-555")
        password_input = self.driver.find_element(By.ID, "password-88888")
        login_button = self.driver.find_element(By.ID, "btnLogin65")
        
        # Enter valid credentials (replace with actual test credentials)
        username_input.send_keys("testuser")
        password_input.send_keys("correctpassword")
        login_button.click()
        
        # Wait for success toast
        try:
            success_toast = WebDriverWait(self.driver, 10).until(
                EC.visibility_of_element_located((By.ID, "toast"))
            )
            success_text = success_toast.find_element(By.TAG_NAME, "span").text
            self.assertEqual(success_text, "Login feito com sucesso!")
        except TimeoutException:
            self.fail("Login success message not displayed")
    
    def test_failed_login(self):
        """Test login with incorrect credentials"""
        username_input = self.driver.find_element(By.ID, "username-555")
        password_input = self.driver.find_element(By.ID, "password-88888")
        login_button = self.driver.find_element(By.ID, "btnLogin65")
        
        # Enter invalid credentials
        username_input.send_keys("invaliduser")
        password_input.send_keys("wrongpassword")
        login_button.click()
        
        # Check error message
        error_message = self.driver.find_element(By.ID, "errorMessage5")
        self.assertTrue(error_message.is_displayed())
        self.assertNotEqual(error_message.text, "")
    
    def test_forgot_password_link(self):
        """Verify forgot password link redirection"""
        forgot_password_link = self.driver.find_element(By.XPATH, "//a[contains(@href, 'password.html')]")
        forgot_password_link.click()
        
        # Wait for redirection and verify URL
        WebDriverWait(self.driver, 10).until(
            EC.url_contains("password.html")
        )
        self.assertIn("password.html", self.driver.current_url)
    
    def test_create_account_link(self):
        """Verify create account link redirection"""
        create_account_link = self.driver.find_element(By.XPATH, "//a[contains(@href, 'account.html')]")
        create_account_link.click()
        
        # Wait for redirection and verify URL
        WebDriverWait(self.driver, 10).until(
            EC.url_contains("account.html")
        )
        self.assertIn("account.html", self.driver.current_url)
    
    def test_social_media_links(self):
        """Test social media icon redirects"""
        social_platforms = [
            {"selector": "img[alt='facebook']", "expected_url": "facebook.com"},
            {"selector": "img[alt='google']", "expected_url": "google.com"},
            {"selector": "img[alt='twitter']", "expected_url": "twitter.com"},
            {"selector": "img[alt='github']", "expected_url": "github.com"}
        ]
        
        for platform in social_platforms:
            social_icon = self.driver.find_element(By.CSS_SELECTOR, platform["selector"])
            social_icon.click()
            
            # Wait and verify redirection
            WebDriverWait(self.driver, 10).until(
                EC.url_contains(platform["expected_url"])
            )
            self.assertIn(platform["expected_url"], self.driver.current_url)
            
            # Return to original page for next test
            self.driver.back()
            WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.ID, "formLogin2"))
            )

if __name__ == "__main__":
    unittest.main()


"""
Key points about the test script:

Uses Python's unittest framework for test organization
Implements comprehensive test scenarios as requested
Uses Selenium WebDriver with explicit waits for dynamic content
Covers page structure verification, login functionality, link redirections
Includes error handling and assertions for each test case

Prerequisites:

Install Selenium WebDriver: pip install selenium
Download appropriate WebDriver (ChromeDriver in this example)
Replace placeholder credentials with actual test credentials

Recommendations:

Modify social media link tests with actual platform URLs
Add more robust error handling as needed
Consider adding screenshot capture for failed tests

"""