from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Set up the WebDriver instance
driver = webdriver.Chrome() # or any other browser you prefer

# Navigate to the page
driver.get("https://the-internet.herokuapp.com/dynamic_controls")

remove_button = driver.find_element(By.TAG_NAME, "Remove")

# Validate Page Structure
def validate_page_structure():
    # Verify title and subtitle are present
    assert driver.find_element(By.XPATH, "//h4[normalize-space()='Dynamic Controls']")

    # Verify checkbox and remove button exist
    checkbox = driver.find_element(By.ID, "checkbox")
    remove_button = driver.find_element(By.TAG_NAME, "Remove")
    assert checkbox.is_enabled() is False
    assert remove_button.text == "Remove"

    # Verify text field is disabled by default
    text_field = driver.find_element(By.ID, "text_field")
    assert text_field.is_disabled() is True

# Test Checkbox Removal and Addition
def test_checkbox_removal():
    # Click Remove button
    remove_button.click()

    # Verify checkbox disappears
    checkbox = driver.find_element(By.ID, "checkbox")
    assert checkbox.is_enabled() is False

    # Check if button text changes to "Add"
    button = driver.find_element(By.TAG_NAME, "Remove")
    assert button.text == "Add"

    # Click Add button
    add_button = driver.find_element(By.TAG_NAME, "Add")
    add_button.click()

    # Verify checkbox reappears
    checkbox = driver.find_element(By.ID, "checkbox")
    assert checkbox.is_enabled() is True

# Test Enable/Disable Functionality
def test_enable_disable():
    # Verify text field is disabled by default
    text_field = driver.find_element(By.ID, "text_field")
    assert text_field.is_disabled() is True

    # Click Enable button
    enable_button = driver.find_element(By.TAG_NAME, "Enable")
    enable_button.click()

    # Verify message appears
    message = driver.find_element(By.CSS_SELECTOR, ".message")
    assert message.text == "Wait for it..."

    # Wait for the loading bar to appear
    waiting_bar = driver.find_element(By.CSS_SELECTOR, ".loading_bar")
    assert waiting_bar.is_visible() is True

    # Verify text field is enabled after process is complete
    text_field = driver.find_element(By.ID, "text_field")
    assert text_field.is_enabled() is True

# Implicit Wait for Loading Bar to Appear
def implicit_wait():
    driver.implicitly_wait(10) # wait for 10 seconds

# Test Other Scenarios (optional)
def test_other_scenarios():
    pass # Add your test scenarios here

# Close the browser when finished
driver.quit()