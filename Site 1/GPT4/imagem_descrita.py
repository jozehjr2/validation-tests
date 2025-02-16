from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import unittest

class BugBankTests(unittest.TestCase):

    def setUp(self):
        # Initialize the WebDriver
        self.driver = webdriver.Chrome()  # Ensure you have the ChromeDriver installed and in PATH
        self.driver.get("https://bugbank.netlify.app/")
        self.driver.implicitly_wait(10)  # Implicit wait for dynamic content

    def test_page_structure(self):
        driver = self.driver

        # Verify left column text
        left_column_text = driver.find_element(By.XPATH, "//div[@class='left-column']").text
        expected_left_text = ("BugBank. The bank with bugs and flaws your way. "
                              "Make transfers and payments with bugs and practice testing successfully in a near-real scenario!")
        self.assertEqual(left_column_text, expected_left_text, "Left column text does not match.")

        # Verify right column elements
        email_field = driver.find_element(By.ID, "email")
        password_field = driver.find_element(By.ID, "password")
        access_button = driver.find_element(By.ID, "access")
        registrar_button = driver.find_element(By.ID, "registrar")

        self.assertIsNotNone(email_field, "Email field is not present.")
        self.assertIsNotNone(password_field, "Password field is not present.")
        self.assertIsNotNone(access_button, "Access button is not present.")
        self.assertIsNotNone(registrar_button, "Registrar button is not present.")

        # Verify text below buttons
        below_buttons_text = driver.find_element(By.XPATH, "//div[@class='below-buttons']").text
        expected_below_text = ("Meet our requirements. The application does not have a database, "
                               "all information is stored in local memory.")
        self.assertEqual(below_buttons_text, expected_below_text, "Text below buttons does not match.")

    def test_successful_login(self):
        driver = self.driver

        # Enter valid credentials
        driver.find_element(By.ID, "email").send_keys("valid@example.com")
        driver.find_element(By.ID, "password").send_keys("validpassword")
        driver.find_element(By.ID, "access").click()

        # Verify successful login (assuming a successful login redirects or changes the page)
        try:
            WebDriverWait(driver, 10).until(EC.url_changes("https://bugbank.netlify.app/"))
            print("Login successful.")
        except TimeoutException:
            self.fail("Login was not successful.")

    def test_login_failure(self):
        driver = self.driver

        # Enter invalid credentials
        driver.find_element(By.ID, "email").send_keys("invalid@example.com")
        driver.find_element(By.ID, "password").send_keys("invalidpassword")
        driver.find_element(By.ID, "access").click()

        # Verify error message appears
        try:
            error_message = WebDriverWait(driver, 10).until(
                EC.visibility_of_element_located((By.XPATH, "//div[@class='error-message']"))
            )
            self.assertIsNotNone(error_message, "Error message did not appear.")
        except TimeoutException:
            self.fail("Error message did not appear.")

    def test_registrar_redirection(self):
        driver = self.driver

        # Click the "Registrar" button
        driver.find_element(By.ID, "registrar").click()

        # Verify redirection to registration page or modal
        try:
            WebDriverWait(driver, 10).until(
                EC.visibility_of_element_located((By.XPATH, "//div[@class='registration-modal']"))
            )
            print("Redirection to registration page/modal successful.")
        except TimeoutException:
            self.fail("Redirection to registration page/modal failed.")

    def tearDown(self):
        # Close the WebDriver
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()


"""
Explanation:
Setup and Teardown: The setUp method initializes the WebDriver and opens the application URL. The tearDown method closes the browser after each test.
Test Page Structure: This test verifies the presence and correctness of the text and elements on the login page.
Test Successful Login: This test inputs valid credentials and checks for a successful login by waiting for a URL change or another indicator of success.
Test Login Failure: This test inputs invalid credentials and verifies that an error message appears.
Test Registrar Redirection: This test clicks the "Registrar" button and checks for the appearance of a registration modal or page.
Notes:
Ensure that the locators (IDs, XPaths) match the actual HTML structure of the web application.
Adjust the WebDriver initialization if using a different browser (e.g., Firefox).
The test assumes certain behaviors (e.g., URL change on successful login) which should be adjusted based on the actual application behavior.
"""