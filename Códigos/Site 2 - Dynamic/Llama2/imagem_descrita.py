import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Set up the browser and drive
driver = webdriver.Chrome()
driver.implicitly_wait(10) # Wait for 10 seconds for the page to load

# Validate Page Structure
def test_page_structure():
    title = driver.find_element(By.XPATH, "//h3[contains(text(), 'Dynamic Content')]")
    images = driver.find_elements(By.XPATH, "body div[class='row'] div[id='content'] div[id='content'] div:nth-child(1) div:nth-child(1)")
    texts = driver.find_elements(By.CSS_SELECTOR, "body div[class='row'] div[id='content'] div[id='content']")
    assert title.is_displayed
    assert len(images) == 3
    assert len(texts) == 3
    assert driver.find_element(By.XPATH, "//a[contains(href, 'https://the-internet.herokuapp.com/dynamic_content')]").is_displayed

# Test Dynamic Content Behavior
def test_dynamic_content():
    driver.implicitly_wait(10) # Wait for 10 seconds for the page to load
    assert driver.find_elements(By.CSS_SELECTOR, ".image").have_size(3)
    assert driver.find_elements(By.CSS_SELECTOR, ".text").have_size(3)
    for i in range(3):
        new_image = driver.find_element(By.CSS_SELECTOR, f".image:nth-child({i+1})")
        new_text = driver.find_element(By.CSS_SELECTOR, f".text:nth-child({i+1})")
        assert not new_image.equals(driver.find_element(By.CSS_SELECTOR, ".image"))
        assert not new_text.equals(driver.find_element(By.CSS_SELECTOR, ".text"))
    driver.implicitly_wait(10) # Wait for 10 seconds for the page to load after refreshing
    assert driver.find_elements(By.CSS_SELECTOR, ".image").have_size(3)
    assert driver.find_elements(By.CSS_SELECTOR, ".text").have_size(3)

# Test Static Content Behavior
def test_static_content():
    driver.get("https://the-internet.herokuapp.com/dynamic_content?with_content=static") # Navigate to the static content page  
    time.sleep(10) # Wait for 10 seconds for the page to load
    assert driver.find_elements(By.CSS_SELECTOR, ".image").have_size(3)
    assert driver.find_elements(By.CSS_SELECTOR, ".text").have_size(3)
    for i in range(3):
        new_image = driver.find_element(By.CSS_SELECTOR, f".image:nth-child({i+1})")
        new_text = driver.find_element(By.CSS_SELECTOR, f".text:nth-child({i+1})")
        assert new_image.equals(driver.find_element(By.CSS_SELECTOR, ".image"))
        assert new_text.equals(driver.find_element(By.CSS_SELECTOR, ".text"))

# Cleanup
driver.quit()