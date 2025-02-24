import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Set up the web driver and navigate to the page
driver = webdriver.Chrome()
driver.get("https://the-internet.herokuapp.com/dynamic_loading")

# Validate page structure
assert driver.title == "Dynamic Loading Example"
assert driver.find_element(By.LINK, text="Example 1: Element on page that is hidden").is_present()
assert driver.find_element(By.LINK, text="Example 2: Element rendered after the fact").is_present()

# Test Example 1 - Hidden Element Behavior
driver.find_element(By.Link, text="Example 1: Element on page that is hidden").click()
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.XPATH, "//div[contains(@class, 'loading')]")))
time.sleep(5) # Wait for the loading bar to appear
assert driver.find_element(By.XPATH, "//p[contains(@class, 'hello-world')]").is_present()
assert driver.find_element(By.XPATH, "//div[contains(@class, 'loading')]").is_not_visible()

# Test Example 2 - Rendered Element Behavior
driver.find_element(By.Link, text="Example 2: Element rendered after the fact").click()
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.XPATH, "//div[contains(@class, 'loading')]")))
time.sleep(5) # Wait for the loading bar to appear
assert driver.find_element(By.XPATH, "//p[contains(@class, 'hello-world')]").is_present()
assert driver.find_element(By.XPATH, "//div[contains(@class, 'loading')]").is_not_visible()

# Close the web driver and end the test
driver.quit()