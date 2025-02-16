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
        # Set base URL
        self.base_url = "https://bugbank.netlify.app/"
        # Set implicit wait
        self.driver.implicitly_wait(10)
        # Navigate to the page
        self.driver.get(self.base_url)

    def tearDown(self):
        # Close the browser
        self.driver.quit()

    def test_page_structure(self):
        """Test to verify the correct page structure and elements"""
        # Verify left column text
        left_column_text = self.driver.find_element(
            By.CSS_SELECTOR, ".left-column"
        ).text
        expected_text = """BugBank.
The bank with bugs and flaws your way.
Make transfers and payments with bugs and practice testing successfully in a near-real scenario!"""
        self.assertEqual(left_column_text.strip(), expected_text.strip())

        # Verify right column elements
        right_column = self.driver.find_element(By.CSS_SELECTOR, ".right-column")
        
        # Verify email field exists
        email_field = right_column.find_element(By.CSS_SELECTOR, "input[type='email']")
        self.assertTrue(email_field.is_displayed())

        # Verify password field exists
        password_field = right_column.find_element(By.CSS_SELECTOR, "input[type='password']")
        self.assertTrue(password_field.is_displayed())

        # Verify Access button exists
        access_button = right_column.find_element(By.CSS_SELECTOR, "button[type='submit']")
        self.assertTrue(access_button.is_displayed())

        # Verify Registrar button exists
        registrar_button = right_column.find_element(By.CSS_SELECTOR, "button.register-button")
        self.assertTrue(registrar_button.is_displayed())

        # Verify requirements text
        requirements_text = self.driver.find_element(
            By.CSS_SELECTOR, ".requirements-text"
        ).text
        expected_req_text = "Meet our requirements. The application does not have a database, all information is stored in local memory."
        self.assertEqual(requirements_text.strip(), expected_req_text)

    def test_successful_login(self):
        """Test successful login with valid credentials"""
        # Enter valid email and password
        email_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='email']")
        password_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='password']")
        
        email_field.send_keys("valid@email.com")
        password_field.send_keys("validpassword")

        # Click login button
        login_button = self.driver.find_element(By.CSS_SELECTOR, "button[type='submit']")
        login_button.click()

        # Wait for successful login (dashboard element presence)
        try:
            WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, ".dashboard"))
            )
            login_successful = True
        except TimeoutException:
            login_successful = False

        self.assertTrue(login_successful, "Login was not successful")

    def test_failed_login(self):
        """Test login failure with invalid credentials"""
        # Enter invalid email and password
        email_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='email']")
        password_field = self.driver.find_element(By.CSS_SELECTOR, "input[type='password']")
        
        email_field.send_keys("invalid@email.com")
        password_field.send_keys("wrongpassword")

        # Click login button
        login_button = self.driver.find_element(By.CSS_SELECTOR, "button[type='submit']")
        login_button.click()

        # Wait for error message
        try:
            error_message = WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, ".error-message"))
            )
            self.assertTrue(error_message.is_displayed())
        except TimeoutException:
            self.fail("Error message did not appear")

    def test_registration_redirect(self):
        """Test redirection to registration page/modal"""
        # Click on Registrar button
        registrar_button = self.driver.find_element(By.CSS_SELECTOR, "button.register-button")
        registrar_button.click()

        # Wait for registration form/modal
        try:
            registration_form = WebDriverWait(self.driver, 10).until(
                EC.presence_of_element_located((By.CSS_SELECTOR, ".registration-form"))
            )
            self.assertTrue(registration_form.is_displayed())
        except TimeoutException:
            self.fail("Registration form/modal did not appear")

if __name__ == "__main__":
    unittest.main()


"""
This test suite includes:

A setUp method that initializes the WebDriver and navigates to the page before each test.
A tearDown method that closes the browser after each test.
Four test methods:
test_page_structure: Verifies all page elements and text content
test_successful_login: Tests login with valid credentials
test_failed_login: Tests login with invalid credentials
test_registration_redirect: Tests the registration button functionality
Important notes:

The CSS selectors used in this example are hypothetical and should be replaced with the actual selectors from your application.

You might need to modify the waits and timeouts based on your application's performance.

The valid credentials used in test_successful_login should be replaced with actual valid test credentials.

Error handling could be enhanced based on your specific requirements.

You might need to add additional assertions based on your application's specific behavior.

To run the tests:

Install the required packages:
BASH

pip install selenium
Download and set up the appropriate WebDriver (ChromeDriver in this example)

Run the tests:

BASH

python -m unittest test_login.py
This code follows best practices for Selenium testing:

Uses explicit waits for dynamic elements
Includes proper setup and teardown
Has clear test cases with descriptive names
Includes comments explaining the code
Uses appropriate assertions to verify expected behavior
Handles potential exceptions
"""