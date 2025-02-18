import pytest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class TestBugBankLoginPage:
    @pytest.fixture
    def driver(self):
        """Setup WebDriver before each test"""
        driver = webdriver.Chrome()  # Assumes ChromeDriver is installed
        driver.get("https://bugbank.netlify.app/")
        driver.maximize_window()
        yield driver
        driver.quit()

    def test_page_structure(self, driver):
        """Verify page structure and key elements"""
        # Left column verification
        title = driver.find_element(By.CLASS_NAME, "pages__Title-sc-1ee1f2s-4")
        assert "O banco com bugs e falhas do seu jeito" in title.text

        # Right column elements
        email_input = driver.find_element(By.NAME, "email")
        password_input = driver.find_element(By.NAME, "password")
        login_button = driver.find_element(By.CLASS_NAME, "otUnI")
        register_button = driver.find_element(By.CLASS_NAME, "ihdmxA")

        assert email_input.is_displayed()
        assert password_input.is_displayed()
        assert login_button.is_displayed()
        assert register_button.is_displayed()

    def test_successful_login(self, driver):
        """Test login with valid credentials"""
        # Note: This is a placeholder. Actual credentials would depend on the application's test setup
        email_input = driver.find_element(By.NAME, "email")
        password_input = driver.find_element(By.NAME, "password")
        login_button = driver.find_element(By.CLASS_NAME, "otUnI")

        email_input.send_keys("test@example.com")
        password_input.send_keys("validpassword")
        login_button.click()

        # Add assertion for successful login - this might be a URL change or dashboard element
        # WebDriverWait(driver, 10).until(EC.url_contains("/dashboard"))

    def test_login_failure(self, driver):
        """Test login with invalid credentials"""
        email_input = driver.find_element(By.NAME, "email")
        password_input = driver.find_element(By.NAME, "password")
        login_button = driver.find_element(By.CLASS_NAME, "otUnI")

        email_input.send_keys("invalid@example.com")
        password_input.send_keys("wrongpassword")
        login_button.click()

        # Wait for and check error message
        # Note: Replace with actual error message locator
        # error_message = WebDriverWait(driver, 10).until(
        #     EC.presence_of_element_located((By.CLASS_NAME, "error-message"))
        # )
        # assert error_message.is_displayed()

    def test_registration_redirection(self, driver):
        """Test redirection to registration page"""
        register_button = driver.find_element(By.CLASS_NAME, "ihdmxA")
        register_button.click()

        # Verify registration form is visible
        register_form = driver.find_element(By.CLASS_NAME, "card__register")
        assert register_form.is_displayed()

    def test_requirements_text(self, driver):
        """Verify requirements text is displayed"""
        requirements_text = driver.find_element(By.CLASS_NAME, "kpiCJH")
        expected_text = "A aplicação não conta com um banco de dados, todas as informações são armazenadas em memória local"
        assert expected_text in requirements_text.text

if __name__ == "__main__":
    pytest.main([__file__])


"""
Key notes about the test script:

Uses pytest for test organization
Assumes ChromeDriver is installed
Includes placeholders for some assertions that require application-specific details
Covers the requested test scenarios
Requires additional configuration (WebDriver, credentials) for full functionality

Prerequisites:

Install dependencies: pip install selenium pytest
Download appropriate WebDriver for your browser
Update WebDriver initialization as needed
"""