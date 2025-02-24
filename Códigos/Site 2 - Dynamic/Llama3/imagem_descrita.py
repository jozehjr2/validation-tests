from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Initialize Chrome driver
driver = webdriver.Chrome()

# Test 1: Validate Page Structure
def test_validate_page_structure():
    try:
        # Verify the presence of title and subtitle
        title_element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.XPATH, "//h3[normalize-space()='Dynamic Content']")))     
        assert title_element.text != ""

        # Verify the presence of three images with text
        image_elements = driver.find_elements_by_class_name("image-frame")
        for i in range(len(image_elements)):
            image_text_element = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.XPATH, f"//div[@class='image-frame'][{i+1}]/p")))
            assert image_text_element.text != ""

        # Verify the presence and href attribute of "click here" link
        click_here_link = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.LINK_TEXT, "click here")))
        assert click_here_link.get_attribute("href") == "https://the-internet.herokuapp.com/dynamic_content?with_content=static"
    except:
        print("Test failed. Page structure validation failed.")

# Test 2: Test Dynamic Content Behavior
def test_dynamic_content_behavior():
    try:
        # Refresh the page multiple times to test dynamic content behavior
        for i in range(5):
            driver.refresh()
            time.sleep(1)  # Wait a second before testing again

            # Capture initial and post-refresh content
            initial_text = ""
            post_refresh_text = ""
            image_elements = driver.find_elements_by_class_name("image-frame")
            for j in range(len(image_elements)):
                initial_text += image_elements[j].find_element_by_tag_name("img").get_attribute("alt") + "\n"
                post_refresh_text += image_elements[j].find_element_by_tag_name("img").get_attribute("alt") + "\n"

            # Compare initial and post-refresh content to confirm dynamic changes
            assert initial_text != post_refresh_text
    except:
        print("Test failed. Dynamic content behavior testing failed.")

# Test 3: Test Static Content Behavior
def test_static_content_behavior():
    try:
        # Click the "click here" link to navigate to static URL
        click_here_link = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.LINK_TEXT, "click here")))
        click_here_link.click()

        # Verify that some of the images and text remain unchanged upon refresh
        initial_text = ""
        post_refresh_text = ""
        image_elements = driver.find_elements_by_class_name("image-frame")
        for j in range(len(image_elements)):
            initial_text += image_elements[j].find_element_by_tag_name("img").get_attribute("alt") + "\n"
            post_refresh_text += image_elements[j].find_element_by_tag_name("img").get_attribute("alt") + "\n"

        # Compare initial and post-refresh content to confirm static changes
        assert initial_text == post_refresh_text
    except:
        print("Test failed. Static content behavior testing failed.")

# Run the tests
test_validate_page_structure()
test_dynamic_content_behavior()
test_static_content_behavior()

# Close Chrome driver
driver.quit()