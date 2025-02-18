from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import unittest

class TestLoginPage(unittest.TestCase):

    def setUp(self):
        # Initialize the WebDriver
        self.driver = webdriver.Chrome()  # You can use any driver you prefer
        self.driver.get("https://wavingtest.github.io/system-healing-test/")
        self.driver.implicitly_wait(10)  # Implicit wait for dynamic content

    def test_page_structure(self):
        driver = self.driver

        # Verify left column text
        left_column_text = driver.find_element(By.XPATH, "//div[@class='left-column']").text
        self.assertEqual(left_column_text, "Seja bem vindo, acesse e aproveite todo o conteúdo, somos uma equipe de profissionais empenhados em trazer o melhor conteúdo direcionado a você, usuário.")

        # Verify right column greeting text
        greeting_text = driver.find_element(By.XPATH, "//div[@class='right-column']//h1").text
        self.assertEqual(greeting_text, "Olá! Seja bem vindo. faça o seu login agora")

        # Verify presence of username and password fields
        self.assertTrue(driver.find_element(By.NAME, "username"))
        self.assertTrue(driver.find_element(By.NAME, "password"))

        # Verify presence of "Esqueceu a sua senha?" link
        forgot_password_link = driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?")
        self.assertTrue(forgot_password_link)

        # Verify presence of "Login" button
        login_button = driver.find_element(By.XPATH, "//button[text()='Login']")
        self.assertTrue(login_button)

        # Verify presence of "Criar uma conta" link
        create_account_link = driver.find_element(By.LINK_TEXT, "Criar uma conta")
        self.assertTrue(create_account_link)

        # Verify presence of social media icons
        social_media_icons = driver.find_elements(By.XPATH, "//div[@class='social-icons']//a")
        self.assertEqual(len(social_media_icons), 4)

    def test_login_functionality(self):
        driver = self.driver

        # Test with correct username and password
        driver.find_element(By.NAME, "username").send_keys("correct_username")
        driver.find_element(By.NAME, "password").send_keys("correct_password")
        driver.find_element(By.XPATH, "//button[text()='Login']").click()

        # Wait for successful login indication (e.g., redirection or welcome message)
        try:
            WebDriverWait(driver, 10).until(EC.url_changes("https://wavingtest.github.io/system-healing-test/"))
            # Add assertion for successful login, e.g., checking URL or welcome message
        except TimeoutException:
            self.fail("Login with correct credentials failed.")

        # Test with incorrect username and/or password
        driver.get("https://wavingtest.github.io/system-healing-test/")  # Reload the page
        driver.find_element(By.NAME, "username").send_keys("wrong_username")
        driver.find_element(By.NAME, "password").send_keys("wrong_password")
        driver.find_element(By.XPATH, "//button[text()='Login']").click()

        # Verify error message is displayed
        error_message = driver.find_element(By.XPATH, "//div[@class='error-message']").text
        self.assertEqual(error_message, "Invalid username or password.")

    def test_forgot_password_redirection(self):
        driver = self.driver

        # Click on "Esqueceu a sua senha?" link
        driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?").click()

        # Verify redirection to Forgot Password page
        WebDriverWait(driver, 10).until(EC.url_contains("forgot-password"))
        self.assertIn("forgot-password", driver.current_url)

    def test_create_account_redirection(self):
        driver = self.driver

        # Click on "Criar uma conta" link
        driver.find_element(By.LINK_TEXT, "Criar uma conta").click()

        # Verify redirection to Create an Account page
        WebDriverWait(driver, 10).until(EC.url_contains("create-account"))
        self.assertIn("create-account", driver.current_url)

    def test_social_media_redirections(self):
        driver = self.driver

        # Verify each social media icon redirects to the correct platform
        social_media_links = {
            "Facebook": "facebook.com",
            "Google": "google.com",
            "X": "twitter.com",
            "Github": "github.com"
        }

        social_media_icons = driver.find_elements(By.XPATH, "//div[@class='social-icons']//a")
        for icon, expected_url in zip(social_media_icons, social_media_links.values()):
            icon.click()
            WebDriverWait(driver, 10).until(EC.number_of_windows_to_be(2))
            driver.switch_to.window(driver.window_handles[1])
            self.assertIn(expected_url, driver.current_url)
            driver.close()
            driver.switch_to.window(driver.window_handles[0])

    def tearDown(self):
        # Close the WebDriver
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()


"""
Explanation:
Setup and Teardown: The setUp method initializes the WebDriver and opens the web application. The tearDown method closes the WebDriver after each test.
Test Methods: Each test method covers a specific scenario:
test_page_structure: Verifies the structure and content of the login page.
test_login_functionality: Tests the login button with both correct and incorrect credentials.
test_forgot_password_redirection: Checks the redirection of the "Esqueceu a sua senha?" link.
test_create_account_redirection: Checks the redirection of the "Criar uma conta" link.
test_social_media_redirections: Verifies that each social media icon redirects to the correct platform.
Assertions: Used to validate expected outcomes, such as text content, presence of elements, and URL redirections.
Waits: Implicit and explicit waits are used to handle dynamic content loading and ensure elements are available before interacting with them.
Make sure to replace "correct_username" and "correct_password" with actual valid credentials for the successful login test. Also, adjust the locators (e.g., XPath, CSS selectors) based on the actual HTML structure of the web application.

"""