from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the WebDriver
driver = webdriver.Chrome()

# Define the application URL
base_url = "https://wavingtest.github.io/system-healing-test/"

try:
    # Open the login page
    driver.get(base_url)

    # Implicit wait for dynamic elements
    driver.implicitly_wait(5)

    # Test: Verify page structure and content
    assert "Login" in driver.title, "Page title does not match."
    assert driver.find_element(By.CLASS_NAME, "banner"), "Banner not found."
    assert driver.find_element(By.ID, "username-555"), "Username field not found."
    assert driver.find_element(By.ID, "password-88888"), "Password field not found."
    assert driver.find_element(By.ID, "btnLogin65"), "Login button not found."
    assert driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?"), "Forgot Password link not found."
    assert driver.find_element(By.LINK_TEXT, "Criar uma conta"), "Create Account link not found."
    print("Page structure verified successfully.")

    # Test: Login functionality
    username_field = driver.find_element(By.ID, "username-555")
    password_field = driver.find_element(By.ID, "password-88888")
    login_button = driver.find_element(By.ID, "btnLogin65")
    error_message = driver.find_element(By.ID, "errorMessage5")

    # Successful login
    username_field.clear()
    password_field.clear()
    username_field.send_keys("validUser")
    password_field.send_keys("validPassword")
    login_button.click()
    # Wait for success toast to appear
    WebDriverWait(driver, 5).until(
        EC.visibility_of_element_located((By.ID, "toast"))
    )
    print("Successful login test passed.")

    # Failed login
    username_field.clear()
    password_field.clear()
    username_field.send_keys("invalidUser")
    password_field.send_keys("invalidPassword")
    login_button.click()
    # Verify error message
    WebDriverWait(driver, 5).until(
        lambda d: error_message.is_displayed() and "error" in error_message.text.lower()
    )
    print("Failed login test passed.")

    # Test: Forgot Password hyperlink redirection
    forgot_password_link = driver.find_element(By.LINK_TEXT, "Esqueceu a sua senha?")
    forgot_password_link.click()
    WebDriverWait(driver, 5).until(
        EC.url_contains("password.html")
    )
    assert "password.html" in driver.current_url, "Forgot Password redirection failed."
    print("Forgot Password link test passed.")

    # Navigate back
    driver.back()

    # Test: Create Account hyperlink redirection
    create_account_link = driver.find_element(By.LINK_TEXT, "Criar uma conta")
    create_account_link.click()
    WebDriverWait(driver, 5).until(
        EC.url_contains("account.html")
    )
    assert "account.html" in driver.current_url, "Create Account redirection failed."
    print("Create Account link test passed.")

    # Navigate back
    driver.back()

    # Test: Social media icon redirections
    social_icons = driver.find_elements(By.CSS_SELECTOR, ".social img")
    social_links = ["facebook.com", "google.com", "twitter.com", "github.com"]

    for icon, link in zip(social_icons, social_links):
        icon.click()
        WebDriverWait(driver, 5).until(EC.new_window_is_opened)
        driver.switch_to.window(driver.window_handles[-1])  # Switch to the new tab
        assert link in driver.current_url, f"Redirection to {link} failed."
        driver.close()
        driver.switch_to.window(driver.window_handles[0])  # Switch back to main tab
    print("Social media link tests passed.")

finally:
    # Close the browser
    driver.quit()


"""
Key Features
Structure Verification: Validates the presence of key elements on the page.
Login Tests:
Validates successful login through the presence of the success toast.
Validates failed login by checking for the error message.
Hyperlink Redirections:
Ensures the Forgot Password and Create Account links redirect to their respective pages.
Social Media Links:
Verifies each icon redirects to the correct platform.
Explicit Waits: Handles dynamic content loading.
Assertions: Validates expected outcomes at each step.

"""