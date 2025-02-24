from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Setup WebDriver (assuming Google Chrome is being used)
driver = webdriver.Chrome()  # specify path if not in PATH

try:
    # 1. Validate Page Structure
    driver.get("https://the-internet.herokuapp.com/dynamic_content")

    # Check for Page Header
    header = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//h3[text()='Dynamic Content']"))
    )
    title = driver.find_element(By.XPATH, "//h3[text()='Dynamic Content']")
    assert title.is_displayed(), "Title is not displayed"

    # Check the presence of three images and their respective text
    images = driver.find_elements(By.XPATH, "//div[@class='row']//img")
    #assert len(images) == 3, "There should be exactly three images"

    texts = driver.find_elements(By.XPATH, "//div[@class='row']//div[@class='large-10 columns']")
    assert len(texts) == 3, "There should be exactly three text sections"

    # Check the "click here" link
    click_here_link = driver.find_element(By.LINK_TEXT, "click here")
    assert "https://the-internet.herokuapp.com/dynamic_content?with_content=static" in click_here_link.get_attribute("href"), \
        "'click here' link does not have the correct href."

    # 2. Test Dynamic Content Behavior
    initial_images_texts = [(img.get_attribute('src'), txt.text) for img, txt in zip(images, texts)]

    # Refresh the page and check for changes
    driver.refresh()
    time.sleep(2)  # sleep for content to load; can be replaced with WebDriverWait for more robust handling
    updated_images = driver.find_elements(By.XPATH, "//div[@class='row']//img")
    updated_texts = driver.find_elements(By.XPATH, "//div[@class='row']//div[@class='large-10 columns']")
    updated_images_texts = [(img.get_attribute('src'), txt.text) for img, txt in zip(updated_images, updated_texts)]

    assert initial_images_texts != updated_images_texts, "Content did not change after refreshing the page."

    # 3. Test Static Content Behavior
    #click_here_link.click()
    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//div[@class='row']//img"))
    )

    # Capture initial static content
    static_images = driver.find_elements(By.XPATH, "//div[@class='row']//img")
    static_texts = driver.find_elements(By.XPATH, "//div[@class='row']//div[@class='large-10 columns']")
    initial_static_images_texts = [(img.get_attribute('src'), txt.text) for img, txt in zip(static_images, static_texts)]

    # Refresh and verify that the content remains the same
    driver.refresh()
    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, "//div[@class='row']//img"))
    )
    refreshed_static_images = driver.find_elements(By.XPATH, "//div[@class='row']//img")
    refreshed_static_texts = driver.find_elements(By.XPATH, "//div[@class='row']//div[@class='large-10 columns']")
    refreshed_static_images_texts = [(img.get_attribute('src'), txt.text) for img, txt in zip(refreshed_static_images, refreshed_static_texts)]

    assert initial_static_images_texts == refreshed_static_images_texts, "Static content did not remain unchanged after refreshing."

finally:
    # Close the WebDriver instance
    driver.quit()