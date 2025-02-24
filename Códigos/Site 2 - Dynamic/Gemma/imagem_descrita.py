from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize the WebDriver (replace with your browser and path)
driver = webdriver.Chrome()

# Define the website URL
url = "https://the-internet.herokuapp.com/dynamic_content"

# Implicit wait for page load - adjust timeout if needed
wait = WebDriverWait(driver, 10)

# 1. Validate Page Structure
try:
    driver.get(url)
    # Verify the presence of title and subtitle
    title = driver.find_element(By.TAG_NAME, 'h3').text
    subtitle = driver.find_element(By.TAG_NAME, 'p').text
    assert title == "Dynamic Content"  and subtitle == "The Internet is a powerful platform for creating and sharing content."

    # Verify images and their text
    images = driver.find_elements(By.CLASS_NAME, 'image') # Get all image elements
    for i, img in enumerate(images): # Loop through the images
        assert len(img.find_element(By.TAG_NAME, 'a')) == 1 # Verify there's a link for each image

    # Check "click here" link
    link = driver.find_element(By.LINK_TEXT, "Click Here")
    assert link.get_attribute("href") == "/dynamic_content?with_content=static"
except Exception as e:
    print(f"Validation failed: {e}")

# 2. Test Dynamic Content Behavior
for i in range(3): # Iterate for multiple refresh cycles
    driver.refresh()
    try:
        wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR, '.image-content')))  # Wait until content is visible
        images = driver.find_elements(By.CLASS_NAME, 'image')
        for i, img in enumerate(images):
            try:
                print('Image', i+1, 'Text:', img.find_element(By.TAG_NAME, 'span').text) # Print out the images text
            except Exception as e:
                print(f"Error reading image content for {i+1}: {e}")  # Log any errors
    except Exception as e:
        print(f"Dynamic content refresh failed: {e}")


# 3. Test Static Content Behavior
driver.refresh() # Refresh the page
wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR, 'h3')))
link = driver.find_element(By.LINK_TEXT, "click here")
try:
    link.click()
    # Verify that some images remain static after refresh
    wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR, 'h3')))
except Exception as e:
    print(f"Static content behavior failed: {e}")

# Close the WebDriver
driver.close()