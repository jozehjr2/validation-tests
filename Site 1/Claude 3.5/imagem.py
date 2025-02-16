import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class BugBankLoginTests(unittest.TestCase):
    def setUp(self):
        # Initialize the WebDriver (Chrome in this example)
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        self.base_url = "https://bugbank.netlify.app/"
        self.driver.implicitly_wait(10)
        self.driver.get(self.base_url)

    def tearDown(self):
        self.driver.quit()

    def test_page_structure(self):
        """Test to verify the correct page structure and elements"""
        # Verify BugBank logo/text
        logo_text = self.driver.find_element(
            By.XPATH, "//h1[contains(text(), 'BugBank')]"
        ).text
        self.assertEqual(logo_text, "BugBank")

        # Verify main heading text
        heading_text = self.driver.find_element(
            By.XPATH, "//h2[contains(text(), 'The bank with bugs and flaws your way')]"
        ).text
        self.assertEqual(heading_text, "The bank with bugs and flaws your way")

        # Verify subtext
        subtext = self.driver.find_element(
            By.XPATH, "//p[contains(text(), 'Make transfers and payments with bugs')]"
        ).text
        expected_subtext = "Make transfers and payments with bugs and practice testing successfully in a near-real scenario!"
        self.assertEqual(subtext, expected_subtext)

        # Verify email field
        email_label = self.driver.find_element(By.XPATH, "//label[text()='E-mail']")
        self.assertTrue(email_label.is_displayed())
        email_input = self.driver.find_element(By.XPATH, "//input[@placeholder='Enter your email']")
        self.assertTrue(email_input.is_displayed())

        # Verify password field
        password_label = self.driver.find_element(By.XPATH, "//label[text()='Password']")
        self.assertTrue(password_label.is_displayed())
        password_input = self.driver.find_element(By.XPATH, "//input[@placeholder='Enter your password']")
        self.assertTrue(password_input.is_displayed())

        # Verify buttons
        access_button = self.driver.find_element(By.XPATH, "//button[text()='Access']")
        self.assertTrue(access_button.is_displayed())
        registrar_button = self.driver.find_element(By.XPATH, "//button[text()='Registrar']")
        self.assertTrue(registrar_button.is_displayed())

        # Verify requirements text
        requirements_heading = self.driver.find_element(
            By.XPATH, "//p[text()='Meet our requirements']"
        )
        self.assertTrue(requirements_heading.is_displayed())
        
        requirements_text = self.driver.find_element(
            By.XPATH, "//p[contains(text(), 'The application does not have a database')]"
        ).text
        expected_req_text = "The application does not have a database, all information is stored in local memory"
        self.assertTrue(requirements_text in expected_req_text)

    def test_successful_login(self):
        """Test successful login with valid credentials"""
        # Enter valid email and password
        email_input = self.driver.find_element(
            By.XPATH, "//input[@placeholder='Enter your email']"
        )
        password_input = self.driver.find_element(
            By.XPATH, "//input[@placeholder='Enter your password']"
        )
        
        email_input.send_keys("valid@email.com")
        password_input.send_keys("validpassword")

        # Click Access button
        access_button = self.driver.find_element(By.XPATH, "//button[text()='Access']")
        access_button.click()

        # Wait for successful login (dashboard element presence)
        try:
            WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CLASS_NAME, "dashboard"))
            )
            login_successful = True
        except TimeoutException:
            login_successful = False

        self.assertTrue(login_successful, "Login was not successful")

    def test_failed_login(self):
        """Test login failure with invalid credentials"""
        # Enter invalid credentials
        email_input = self.driver.find_element(
            By.XPATH, "//input[@placeholder='Enter your email']"
        )
        password_input = self.driver.find_element(
            By.XPATH, "//input[@placeholder='Enter your password']"
        )
        
        email_input.send_keys("invalid@email.com")
        password_input.send_keys("wrongpassword")

        # Click Access button
        access_button = self.driver.find_element(By.XPATH, "//button[text()='Access']")
        access_button.click()

        # Wait for error message
        try:
            error_message = WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CLASS_NAME, "error-message"))
            )
            self.assertTrue(error_message.is_displayed())
        except TimeoutException:
            self.fail("Error message did not appear")

    def test_registration_redirect(self):
        """Test redirection to registration page/modal"""
        # Click Registrar button
        registrar_button = self.driver.find_element(By.XPATH, "//button[text()='Registrar']")
        registrar_button.click()

        # Wait for registration form/modal
        try:
            registration_form = WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CLASS_NAME, "registration-form"))
            )
            self.assertTrue(registration_form.is_displayed())
        except TimeoutException:
            self.fail("Registration form/modal did not appear")

if __name__ == "__main__":
    unittest.main()

"""
Key changes made based on the image:

Updated the locators to match the actual UI elements shown in the image
Used more specific XPath selectors for text elements and form fields
Matched the exact text content from the image
Updated the input field placeholders to match what's shown
Maintained the proper structure of the page elements as shown in the layout
To run the tests:

Install required packages:
BASH

pip install selenium
Install ChromeDriver and ensure it's in your system PATH

Run the tests:

BASH

python -m unittest test_login.py
Note: Some class names (like 'dashboard', 'error-message', 'registration-form') are assumed and might need to be updated based on the actual implementation of the application.

The test suite covers all the requirements while matching the actual UI elements shown in the image. The locators are now more precise and should work with the actual implementation of the page.
"""
