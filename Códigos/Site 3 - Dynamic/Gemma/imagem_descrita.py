from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the WebDriver (replace 'chromedriver' with your actual WebDriver executable path)
driver = webdriver.Chrome()

# Open the target URL
driver.get('https://the-internet.herokuapp.com/dynamic_loading')

# **Validate Page Structure**
title = driver.find_element(By.TAG_NAME, 'h3').text
subtitle = driver.find_element(By.CSS_SELECTOR, 'body > div:nth-child(2) > div:nth-child(2) > div:nth-child(1) > p:nth-child(2)').text
print("Title:", title)
print("Subtitle:", subtitle)

links = driver.find_elements(By.LINK_TEXT, 'Example 1: Element on page that is hidden') # Get all elements by link text
link_2 = driver.find_element(By.LINK_TEXT, 'Example 2: Element rendered after the fact') # Get all elements by link text

# **Test Example 1 - Hidden Element Behavior**

print("-------------------- Example 1: Hidden Element ---------------")
# Click the "Example 1" link and verify redirection.
links[0].click()  # Navigate to the hidden element page after click on example 1 link
driver.implicitly_wait(5) # Implicit wait for 5 seconds


# **Test Example 2 - Rendered Element Behavior**

print("-------------------- Example 2: Rendered Element ---------------")
link_2.click()  # Navigate to the rendered element page after click on example 2 link

driver.implicitly_wait(5) # Implicit wait for 5 seconds


# **Additional Notes**
# - Explicit waits are often used for dynamic content that takes time to load, as
#   it's not guaranteed to be available right away (ex. loading a page in JavaScript).
#    - Use WebDriverWait and EC for explicit waits

# Close the WebDriver
driver.quit()