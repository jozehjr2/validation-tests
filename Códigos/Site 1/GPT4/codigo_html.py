from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
import time

# Setup WebDriver (Make sure the WebDriver executable is in your PATH)
driver = webdriver.Chrome()

# Base URL
base_url = "https://bugbank.netlify.app/"

# Define a timeout for explicit waits
timeout = 10

try:
    # Navigate to the application
    driver.get(base_url)

    # Test 1: Verify page structure
    print("Test 1: Verifying page structure...")
    left_title = driver.find_element(By.CSS_SELECTOR, ".pages__Title-sc-1ee1f2s-4")
    assert left_title.text == "O banco com bugs e falhas do seu jeito"
    
    left_text = driver.find_element(By.CSS_SELECTOR, ".pages__Text-sc-1ee1f2s-5")
    assert "Faça transferências e pagamentos com bugs" in left_text.text

    login_form = driver.find_element(By.CLASS_NAME, "card__login")
    assert login_form is not None

    # Test 2: Successful login
    print("Test 2: Testing successful login...")
    email_input = driver.find_element(By.NAME, "email")
    email_input.send_keys("valid_user@example.com")

    password_input = driver.find_element(By.NAME, "password")
    password_input.send_keys("validpassword")

    login_button = driver.find_element(By.CSS_SELECTOR, ".style__ContainerButton-sc-1wsixal-0.otUnI")
    login_button.click()

    # Wait for a successful login indicator or message
    # Assuming the application shows a success message or redirects
    WebDriverWait(driver, timeout).until(EC.url_contains("dashboard"))
    print("Login successful test passed.")

    # Test 3: Login failure
    print("Test 3: Testing login failure...")
    driver.get(base_url)  # Reset to login page
    email_input = driver.find_element(By.NAME, "email")
    email_input.send_keys("invalid_user@example.com")

    password_input = driver.find_element(By.NAME, "password")
    password_input.send_keys("wrongpassword")

    login_button = driver.find_element(By.CSS_SELECTOR, ".style__ContainerButton-sc-1wsixal-0.otUnI")
    login_button.click()

    error_message = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.CLASS_NAME, "input__warging"))
    )
    assert error_message.text != ""
    print("Login failure test passed.")

    # Test 4: Redirect to registration
    print("Test 4: Testing redirection to registration...")
    register_button = driver.find_element(By.CSS_SELECTOR, ".style__ContainerButton-sc-1wsixal-0.ihdmxA")
    register_button.click()

    # Wait for the registration form to appear
    registration_form = WebDriverWait(driver, timeout).until(
        EC.visibility_of_element_located((By.CLASS_NAME, "card__register"))
    )
    assert registration_form is not None
    print("Redirection to registration test passed.")

    # Test 5: Verify additional information text
    print("Test 5: Verifying information text...")
    info_text = driver.find_element(By.CLASS_NAME, "styles__WarnigWrapper-sc-1ota0lw-0")
    assert "A aplicação não conta com um banco de dados" in info_text.text
    print("Information text verification passed.")

except AssertionError as e:
    print(f"AssertionError: {e}")
except TimeoutException as e:
    print(f"TimeoutException: {e}")
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    # Close the browser
    driver.quit()


"""
Key Notes:
Preconditions: Update the test data (valid_user@example.com, validpassword, etc.) based on the application's expectations.
Dynamic Elements: Explicit waits are used to handle dynamic loading.
Assertions: Validate page structure, error messages, redirects, and text presence.
Environment: Ensure the Chrome WebDriver matches the installed browser version. Install Selenium via pip install selenium.
Customization: Adjust locators (e.g., By.CSS_SELECTOR, By.CLASS_NAME) if the page structure changes.

"""