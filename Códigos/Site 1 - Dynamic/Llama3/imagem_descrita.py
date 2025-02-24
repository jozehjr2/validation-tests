from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Set up the Selenium WebDriver
driver = webdriver.Chrome()  # Replace with your preferred browser

try:
    # Test Scenario 1: Validate Page Structure
    driver.get("https://the-internet.herokuapp.com/dynamic_controls")

    # Verify title and subtitle
    title_element = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//h4[normalize-space()='Dynamic Controls']"))
    )
    assert title_element.text == "Dynamic Controls"

    # Verify checkbox and remove button exist when the page loads
    checkbox_element = driver.find_element_by_name("checkbox")
    remove_button_element = driver.find_element_by_name("remove")

    # Verify text field is disabled by default
    input_field_element = driver.find_element_by_name("input")
    assert not input_field_element.is_enabled()

    # Test Scenario 2: Test Checkbox Removal and Addition
    checkbox_element.click()  # Click the checkbox to select it
    remove_button_element.click()  # Click "Remove" to remove the checkbox

    WebDriverWait(driver, 10).until(
        EC.invisibility_of_element_located((By.NAME, "checkbox"))
    )

    assert not checkbox_element.is_displayed()

    remove_button_text = remove_button_element.text
    assert remove_button_text == "Add"

    remove_button_element.click()  # Click "Add" to add the checkbox back

    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.NAME, "checkbox"))
    )

    assert checkbox_element.is_displayed()

    # Test Scenario 3: Test Enable/Disable Functionality
    input_field_element.click()  # Click the text field to focus it
    enable_button_element = driver.find_element_by_name("enable")
    enable_button_element.click()  # Click "Enable" to activate the text field

    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//p[text()='Wait for it...']"))
    )

    WebDriverWait(driver, 10).until_not(EC.text_to_be_present_in_element_value((By.NAME, "input"), ""))
    assert input_field_element.is_enabled()

finally:
    driver.quit()