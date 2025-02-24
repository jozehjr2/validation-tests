from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the WebDriver (replace with your preferred browser)
driver = webdriver.Chrome()  # Example: using Chrome WebDriver

# Load the website
driver.get("https://the-internet.herokuapp.com/dynamic_controls")

# Implicit wait for the page to load before continuing actions
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.TAG_NAME, 'body')))

# 1. Validate Page Structure
# Get the Title and Subtitle from the webpage
title = driver.find_element(By.TAG_NAME, "h4").text  # Extract title text
subtitle = driver.find_element(By.TAG_NAME, "p").text  # Extract subtitle text

print("Title:", title)
print("Subtitle:", subtitle)

# Check for the checkbox and remove button presence on page load
checkbox_exists = driver.find_elements(By.XPATH, "//input[@type='checkbox']") # Find all checkbox elements by XPath
remove_button_exists = driver.find_elements(By.TAG_NAME, "a")

print("Checkbox present:", checkbox_exists)
print("Remove button present:", remove_button_exists)

# 2. Test Checkbox Removal and Addition
try:
    # Click the "Remove" button
    remove_button = driver.find_element(By.XPATH, "//button[normalize-space()='Remove']") # Find the "Remove" button by its text.
    # Ensure the checkbox is enabled before clicking Remove
    checkbox_is_selected = driver.find_elements(By.XPATH, "//input[@type='checkbox']")[0].is_selected()
    if checkbox_is_selected:
        remove_button.click()
        time.sleep(1) # Introduce delay to ensure the button is clicked properly
    # Verify that the button's text changes to "Add"
    else:
        print("Checkbox is not selected")

        # Click the "Add" button to bring back the checkbox
        add_button = driver.find_element(By.XPATH, "//a[text()='Add']")
        add_button.click()

except Exception as e:
    print('Error in test:', str(e))

# 3. Test Enable/Disable Functionality
try:
    # Verify the text field is disabled by default
    disabled_text_field = driver.find_element(By.ID, "dynamicControl")
    assert disabled_text_field.get_attribute("disabled") == "disabled"

    # Click the 'Enable' button and confirm that a message appears
    enable_button = driver.find_element(By.XPATH, "//button[text()='Enable']")
    enable_button.click()

    # Verify the loading bar before text field enablement
    time.sleep(1)
    loading_bar = WebDriverWait(driver, 10).until(EC.visibility_of_element_located((By.XPATH, "//div[@id='dynamicControl']")))

    assert loading_bar.is_displayed()  # Check if the loading bar appears


    # Verify text field is enabled after process completion
    enabled_text_field = driver.find_element(By.ID, "dynamicControl")
    assert enabled_text_field.get_attribute("disabled") == "disabled"

except Exception as e:
    print('Error in test:', str(e))



# Close the WebDriver session after tests are complete (recommended)
driver.quit()
