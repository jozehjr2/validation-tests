from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the Chrome driver
driver = webdriver.Chrome()

url = "https://the-internet.herokuapp.com/dynamic_controls"
driver.get(url)

# Wait for the page to load completely
WebDriverWait(driver, 10).until(EC.visibility_of_all_elements_located((By.XPATH, "//div[@id='flash-messages']")))

# Validate Page Structure
title = driver.find_element(By.CSS_SELECTOR, "h4:nth-child(1)")
assert title.text == 'Dynamic Controls', "Title validation failed"

# Verify the checkbox and remove button exist when the page loads
checkbox = driver.find_element(By.ID, "check-box")
remove_button = driver.find_element(By.CSS_SELECTOR, "#check-box ~ #button > button")
assert checkbox and remove_button, "Checkbox or Remove Button not found"

# Text Field is disabled by default
textfield = driver.find_element(By.ID, "input-example")
assert textfield.get_attribute("disabled") == "true", "Text field validation failed (default state)"

# Test Checkbox Removal and Addition
remove_button.click()
if checkbox:
    remove_button = driver.find_element(By.CSS_SELECTOR, "#check-box ~ #button > button")
    assert remove_button.text == "Add", "Button text validation failed (after Remove)"
else:
    assert False, "Checkbox not found after clicking Remove"

remove_button.click()
if checkbox:
    assert False, "Checkbox should not be present after clicking Add"
else:
    assert True, "Checkbox was found after clicking Add (expected to not find)"

# Test Enable/Disable Functionality
enable_button = driver.find_element(By.ID, "enable-button")
textfield = driver.find_element(By.ID, "input-example")

enable_button.click()

# Wait for the message and loading bar to appear
WebDriverWait(driver, 10).until(EC.visibility_of_element_located((By.CSS_SELECTOR, "#message")))
WebDriverWait(driver, 10).until(EC.visibility_of_element_located((By.ID, "progressbar")))

# Verify that the text field is enabled after the process is complete
assert not textfield.get_attribute("disabled"), "Text field validation failed (after Enable)"

driver.quit()