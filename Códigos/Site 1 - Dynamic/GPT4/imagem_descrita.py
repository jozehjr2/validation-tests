from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the WebDriver
driver = webdriver.Chrome()

try:
    # Open the web application
    driver.get("https://the-internet.herokuapp.com/dynamic_controls")

    # Validate Page Structure
    # Check for the presence of the title and subtitle
    assert "The Internet" in driver.title
    header = driver.find_element(By.XPATH, "//h4[contains(text(), 'Dynamic Controls')]")
    assert header.is_displayed()

    # Verify the checkbox and remove button exist when the page loads
    checkbox = driver.find_element(By.XPATH, "//input[@type='checkbox']")
    remove_button = driver.find_element(By.XPATH, "//button[normalize-space()='Remove']")
    assert checkbox.is_displayed()
    assert remove_button.is_displayed()

    # Verify the text field is disabled by default
    text_field = driver.find_element(By.XPATH, "//input[@type='text']")
    assert not text_field.is_enabled()

    # Test Checkbox Removal and Addition
    # Click "Remove" and verify the checkbox disappears
    remove_button.click()
    WebDriverWait(driver, 10).until(EC.invisibility_of_element(checkbox))

    # Check if the button text changes to "Add"
    add_button = driver.find_element(By.XPATH, "//button[normalize-space()='Add']")
    assert add_button.is_displayed()

    # Click "Add" and verify the checkbox reappears
    add_button.click()
    WebDriverWait(driver, 10).until(EC.visibility_of_element_located((By.XPATH, "//input[@type='checkbox']")))
    checkbox = driver.find_element(By.XPATH, "//input[@type='checkbox']")
    assert checkbox.is_displayed()

    # Test Enable/Disable Functionality
    # Verify that the text field is disabled by default
    assert not text_field.is_enabled()

    # Click "Enable" and confirm the appearance of the message "Wait for it..."
    enable_button = driver.find_element(By.XPATH, "//button[normalize-space()='Enable']")
    enable_button.click()
    message = WebDriverWait(driver, 10).until(EC.visibility_of_element_located((By.ID, "message")))
    assert "It's enabled!" in message.text

    # Ensure the loading bar appears before the text field is enabled
    WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "loading")))

    # Verify that the text field is enabled after the process is complete
    WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//input[@type='text']")))
    assert text_field.is_enabled()

finally:
    # Close the WebDriver
    driver.quit()