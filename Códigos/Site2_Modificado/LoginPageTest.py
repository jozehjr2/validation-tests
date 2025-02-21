import unittest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import time

class LoginPageTest(unittest.TestCase):
    """Test suite for login page functionality using data-test-id attributes."""
    
    def setUp(self):
        """Setup method to initialize the WebDriver before each test."""
        # Setup Chrome WebDriver
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        # Navigate to the application URL
        self.base_url = "https://wavingtest.github.io/system-healing-test/"
        self.driver.get(self.base_url)
        # Set implicit wait
        self.driver.implicitly_wait(10)
    
    def tearDown(self):
        """Teardown method to close the WebDriver after each test."""
        self.driver.quit()
    
    def test_verify_page_structure(self):
        """Test to verify the login page structure using data-test-id attributes."""
        # Verify container exists
        container = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-container']")
        self.assertTrue(container.is_displayed(), "Login container is not displayed")
        
        # Verify banner section
        banner = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-banner']")
        self.assertTrue(banner.is_displayed(), "Banner section is not displayed")
        
        # Verify welcome message
        welcome_message = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-welcome-message']")
        self.assertTrue("Seja bem vindo" in welcome_message.text, "Welcome message is incorrect")
        
        # Verify login box
        login_box = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-box']")
        self.assertTrue(login_box.is_displayed(), "Login box is not displayed")
        
        # Verify greeting
        greeting = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-greeting']")
        self.assertEqual("Olá! Seja bem vindo.", greeting.text, "Greeting text is incorrect")
        
        # Verify form title
        form_title = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-form-title']")
        self.assertEqual("faça o seu login agora", form_title.text.lower(), "Form title is incorrect")
        
        # Verify input fields
        username_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-username-input']")
        self.assertTrue(username_input.is_displayed(), "Username input field is not displayed")
        self.assertEqual("username", username_input.get_attribute("placeholder"), "Username placeholder is incorrect")
        
        password_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-password-input']")
        self.assertTrue(password_input.is_displayed(), "Password input field is not displayed")
        self.assertEqual("password", password_input.get_attribute("placeholder"), "Password placeholder is incorrect")
        
        # Verify forgot password link
        forgot_password_link = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-forgot-password-link']")
        self.assertTrue(forgot_password_link.is_displayed(), "Forgot password link is not displayed")
        self.assertTrue("Esqueceu a sua senha?" in forgot_password_link.text, "Forgot password text is incorrect")
        
        # Verify login button
        login_button = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-submit-button']")
        self.assertTrue(login_button.is_displayed(), "Login button is not displayed")
        self.assertEqual("Login", login_button.text, "Login button text is incorrect")
        
        # Verify create account link
        create_account_link = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-create-account-link']")
        self.assertTrue(create_account_link.is_displayed(), "Create account link is not displayed")
        self.assertTrue("Criar uma conta" in create_account_link.text, "Create account text is incorrect")
        
        # Verify social media icons
        social_icons = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-social-icons']")
        self.assertTrue(social_icons.is_displayed(), "Social media icons section is not displayed")
        
        # Verify individual social icons
        social_platforms = ["facebook", "google", "twitter", "github"]
        for platform in social_platforms:
            icon = self.driver.find_element(By.CSS_SELECTOR, f"[data-test-id='login-social-{platform}']")
            self.assertTrue(icon.is_displayed(), f"{platform} icon is not displayed")
    
    def test_successful_login(self):
        """Test successful login with correct credentials."""
        # Enter valid username and password (assuming these are valid credentials)
        username_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-username-input']")
        username_input.send_keys("validuser")
        
        password_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-password-input']")
        password_input.send_keys("validpassword")
        
        # Click login button
        login_button = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-submit-button']")
        login_button.click()
        
        # Wait for successful login toast message
        try:
            toast = WebDriverWait(self.driver, 5).until(
                EC.visibility_of_element_located((By.CSS_SELECTOR, "[data-test-id='login-toast']"))
            )
            toast_message = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-toast-message']")
            self.assertTrue("sucesso" in toast_message.text.lower(), "Success message not displayed after login")
        except TimeoutException:
            self.fail("Login success toast did not appear")
    
    def test_failed_login(self):
        """Test login failure with incorrect credentials."""
        # Enter invalid username and password
        username_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-username-input']")
        username_input.send_keys("invaliduser")
        
        password_input = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-password-input']")
        password_input.send_keys("invalidpassword")
        
        # Click login button
        login_button = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-submit-button']")
        login_button.click()
        
        # Check for error message
        try:
            error_message = WebDriverWait(self.driver, 5).until(
                EC.visibility_of_element_located((By.CSS_SELECTOR, "[data-test-id='login-error-message']"))
            )
            self.assertTrue(error_message.is_displayed(), "Error message not displayed after failed login")
        except TimeoutException:
            self.fail("Login error message did not appear")
    
    def test_forgot_password_link(self):
        """Test the forgot password link redirection."""
        # Click on forgot password link
        forgot_password_link = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-forgot-password-link']")
        forgot_password_link.click()
        
        # Wait for the page to load and verify URL
        WebDriverWait(self.driver, 5).until(
            lambda driver: "password.html" in driver.current_url
        )
        self.assertTrue("password.html" in self.driver.current_url, 
                       "Redirection to forgot password page failed")
    
    def test_create_account_link(self):
        """Test the create account link redirection."""
        # Click on create account link
        create_account_link = self.driver.find_element(By.CSS_SELECTOR, "[data-test-id='login-create-account-link']")
        create_account_link.click()
        
        # Wait for the page to load and verify URL
        WebDriverWait(self.driver, 5).until(
            lambda driver: "account.html" in driver.current_url
        )
        self.assertTrue("account.html" in self.driver.current_url, 
                       "Redirection to create account page failed")
    
    def test_social_media_redirections(self):
        """Test redirection for each social media icon."""
        # Dictionary mapping social platform to expected domain in URL
        social_platforms = {
            "facebook": "facebook.com",
            "google": "google.com",
            "twitter": "twitter.com",
            "github": "github.com"
        }
        
        # Test each social media icon
        for platform, domain in social_platforms.items():
            # Navigate back to login page for each test
            self.driver.get(self.base_url)
            
            # Find and click social media icon
            social_icon = self.driver.find_element(By.CSS_SELECTOR, f"[data-test-id='login-social-{platform}']")
            self.driver.execute_script("arguments[0].scrollIntoView(true);", social_icon)
            social_icon.click()
            
            # Store the current window handle
            login_window = self.driver.current_window_handle
            
            # Wait for new window/tab
            try:
                WebDriverWait(self.driver, 5).until(
                    lambda driver: len(driver.window_handles) > 1
                )
                
                # Switch to new window
                for window_handle in self.driver.window_handles:
                    if window_handle != login_window:
                        self.driver.switch_to.window(window_handle)
                        break
                        
                # Wait for page to load and verify URL contains expected domain
                WebDriverWait(self.driver, 10).until(
                    lambda driver: domain in driver.current_url.lower() or
                                  self.driver.title.lower().find(platform) != -1
                )
                
                # Close current window and switch back to login page
                self.driver.close()
                self.driver.switch_to.window(login_window)
                
            except TimeoutException:
                self.fail(f"Redirection to {platform} failed or took too long")

if __name__ == "__main__":
    unittest.main()