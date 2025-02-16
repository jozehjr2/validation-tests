import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class LoginPageTest(unittest.TestCase):
    def setUp(self):
        # Initialize the WebDriver
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        self.base_url = "https://wavingtest.github.io/system-healing-test/"
        self.wait = WebDriverWait(self.driver, 10)
        
    def tearDown(self):
        self.driver.quit()

    def test_page_structure(self):
        """Test to verify the page structure matches the design"""
        driver = self.driver
        driver.get(self.base_url)

        # Verify left column content
        left_text = driver.find_element(By.XPATH, "//div[contains(text(), 'Seja bem vindo, acesse e aproveite todo o conteúdo')]").text
        expected_left_text = "Seja bem vindo, acesse e aproveite todo o conteúdo, somos uma equipe de profissionais empenhados em trazer o melhor conteúdo direcionado a você, usuário."
        self.assertEqual(left_text, expected_left_text)

        # Verify right column elements
        # Check main heading
        heading = driver.find_element(By.XPATH, "//div[contains(text(), 'Olá! Seja bem vindo.')]").text
        self.assertEqual(heading, "Olá! Seja bem vindo.")

        # Check sub-heading
        sub_heading = driver.find_element(By.XPATH, "//div[contains(text(), 'faça o seu login agora')]").text
        self.assertEqual(sub_heading, "faça o seu login agora")

        # Verify form elements
        username_field = driver.find_element(By.XPATH, "//input[@placeholder='username']")
        password_field = driver.find_element(By.XPATH, "//input[@placeholder='password']")
        login_button = driver.find_element(By.XPATH, "//button[text()='Login']")

        self.assertTrue(username_field.is_displayed())
        self.assertTrue(password_field.is_displayed())
        self.assertTrue(login_button.is_displayed())

    def test_login_functionality(self):
        """Test login with various credentials"""
        driver = self.driver
        driver.get(self.base_url)

        # Test successful login
        self.perform_login("valid_user", "valid_password")
        try:
            success_element = self.wait.until(
                EC.presence_of_element_located((By.CLASS_NAME, "success-message"))
            )
            self.assertTrue(success_element.is_displayed())
        except TimeoutException:
            self.fail("Login success message not displayed")

        # Test failed login
        driver.get(self.base_url)
        self.perform_login("invalid_user", "invalid_password")
        try:
            error_element = self.wait.until(
                EC.presence_of_element_located((By.CLASS_NAME, "error-message"))
            )
            self.assertTrue(error_element.is_displayed())
        except TimeoutException:
            self.fail("Login error message not displayed")

    def test_forgot_password_link(self):
        """Test forgot password link"""
        driver = self.driver
        driver.get(self.base_url)
        
        forgot_password = driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?")
        forgot_password.click()
        
        self.wait.until(EC.url_contains("forgot-password"))
        self.assertIn("forgot-password", driver.current_url)

    def test_create_account_link(self):
        """Test create account link"""
        driver = self.driver
        driver.get(self.base_url)
        
        create_account = driver.find_element(By.LINK_TEXT, "Criar uma conta")
        create_account.click()
        
        self.wait.until(EC.url_contains("create-account"))
        self.assertIn("create-account", driver.current_url)

    def test_social_media_icons(self):
        """Test social media icon redirections"""
        driver = self.driver
        driver.get(self.base_url)
        
        # Dictionary mapping social media icons to their expected URLs
        social_media = {
            "Facebook": "facebook.com",
            "Google": "google.com",
            "Twitter": "twitter.com",
            "GitHub": "github.com"
        }

        # Get all social media icons
        social_icons = driver.find_elements(By.CSS_SELECTOR, ".social-icons img")
        
        for icon in social_icons:
            icon.click()
            
            # Switch to new tab
            self.wait.until(lambda d: len(d.window_handles) > 1)
            driver.switch_to.window(driver.window_handles[-1])
            
            # Get the icon's platform from alt text or other attribute
            platform = icon.get_attribute("alt")
            expected_url = social_media[platform]
            
            # Verify URL
            self.wait.until(EC.url_contains(expected_url))
            self.assertIn(expected_url, driver.current_url.lower())
            
            # Close tab and switch back
            driver.close()
            driver.switch_to.window(driver.window_handles[0])

    def perform_login(self, username, password):
        """Helper method to perform login"""
        username_field = self.driver.find_element(By.XPATH, "//input[@placeholder='username']")
        password_field = self.driver.find_element(By.XPATH, "//input[@placeholder='password']")
        login_button = self.driver.find_element(By.XPATH, "//button[text()='Login']")

        username_field.clear()
        username_field.send_keys(username)
        password_field.clear()
        password_field.send_keys(password)
        login_button.click()

if __name__ == "__main__":
    unittest.main()


"""
This code has been specifically adapted to match the login page shown in the image, with:

Correct text content verification for both left and right columns
Proper element locators matching the visual layout
Tests for all visible elements including:
Username and password fields
Login button
"Esqueceu a sua senha?" link
"Criar uma conta" link
Social media icons (Facebook, Google, Twitter, GitHub)
To run the tests:

Install required packages:
BASH

pip install selenium
pip install webdriver_manager
Ensure you have Chrome WebDriver installed

Run the tests:

BASH

python -m unittest login_page_test.py
The locators used in this code are based on the visual elements shown in the image. You may need to adjust them slightly depending on the actual HTML implementation of the page.

"""