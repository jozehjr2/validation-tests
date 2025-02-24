from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Initialize the Chrome driver
driver = webdriver.Chrome()

url = "https://the-internet.herokuapp.com/dynamic_loading"
driver.get(url)

# Validate Page Structure
title = driver.find_element(By.XPATH, "//h3[normalize-space()='Dynamically Loaded Page Elements']")

assert title.text == "Dynamically Loaded Page Elements", f"Expected: Dynamic Content but got: {title.text}"

links = driver.find_elements(By.CSS_SELECTOR, "#content > div > div:nth-child(2) > a")
assert len(links) == 2, "Expected 2 links, found: {}".format(len(links))

for link in links:
    if link.text == "Example 1: Element on page that is hidden":
        example_1_link = link
    elif link.text == "Example 2: Element rendered after the fact":
        example_2_link = link

# Test Example 1 - Hidden Element Behavior
example_1_link.click()
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, 'loading_example_text')))
start_button = driver.find_element(By.ID, 'start')
start_button.click()

hello_world_text = driver.find_element(By.CSS_SELECTOR, "#finish > h4")
assert hello_world_text.is_displayed(), "Expected Hello World text to be displayed"

# Check if the text was already present but hidden before clicking "Start"
hidden_elements = driver.find_elements(By.CSS_SELECTOR, "#finish h4")
if len(hidden_elements) > 0:
    assert not hidden_elements[0].is_displayed(), f"Expected Hello World text to be hidden before clicking 'Start', but found it displayed."
else:
    print("Hello World text was initially absent from the HTML.")

# Test Example 2 - Rendered Element Behavior
example_2_link.click()
WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, 'loading_example_text')))
start_button = driver.find_element(By.ID, 'start')
start_button.click()

hello_world_text = driver.find_element(By.CSS_SELECTOR, "#finish > h4")
assert hello_world_text.is_displayed(), "Expected Hello World text to be displayed"

# Check if the text was not initially present in the HTML and was added after loading
initial_html = driver.execute_script("return document.documentElement.outerHTML")
if "Hello World" in initial_html:
    print("Hello World text was initially present in the HTML.")
else:
    print("Hello World text was not initially present in the HTML.")

# Close the browser
driver.quit()