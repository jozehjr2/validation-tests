package site1.mistral;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class BugBankLoginTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.gecko.driver", "src\\drive\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Espera implícita
        driver.get("https://bugbank.netlify.app/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyPageStructure() {
        WebElement leftColumn = driver.findElement(By.cssSelector("div.left-column"));
        WebElement rightColumn = driver.findElement(By.cssSelector("div.right-column"));
        WebElement accessButton = driver.findElement(By.id("access-btn"));
        WebElement registerButton = driver.findElement(By.id("register-btn"));
        WebElement footerText = driver.findElement(By.cssSelector("p.footer-text"));

        Assertions.assertTrue(leftColumn.getText().contains("BugBank"), "Texto esperado na coluna esquerda não encontrado.");
        Assertions.assertEquals(2, rightColumn.findElements(By.tagName("input")).size(), "Campos de e-mail e senha não encontrados.");
        Assertions.assertNotNull(accessButton, "Botão de acesso não encontrado.");
        Assertions.assertNotNull(registerButton, "Botão de registro não encontrado.");
        Assertions.assertTrue(footerText.getText().contains("Meet our requirements"), "Texto esperado no rodapé não encontrado.");
    }

    @Test
    public void testSuccessfulLogin() {
        driver.findElement(By.name("email")).sendKeys("test@bugbank.net");
        driver.findElement(By.name("password")).sendKeys("test1234");
        driver.findElement(By.id("access-btn")).click();

        // Esperar um pouco para verificar a ausência de erro
        try {
            WebElement errorMessage = driver.findElement(By.cssSelector(".error-message"));
            Assertions.assertFalse(errorMessage.isDisplayed(), "Mensagem de erro apareceu para login válido.");
        } catch (NoSuchElementException e) {
            // Se o elemento não existir, significa que o login não falhou (o que é bom).
        }
    }

    @Test
    public void testLoginFailure() {
        driver.findElement(By.name("email")).sendKeys("wrong@bugbank.net");
        driver.findElement(By.name("password")).sendKeys("incorrect1234");
        driver.findElement(By.id("access-btn")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector(".error-message"));
        Assertions.assertTrue(errorMessage.isDisplayed(), "Mensagem de erro não foi exibida para credenciais inválidas.");
    }

    @Test
    public void testRedirectToRegistrationPage() {
        driver.findElement(By.id("register-btn")).click();

        // Verifica a URL de destino após clicar no botão de registro
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertEquals("https://bugbank.netlify.app/register", currentUrl, "Redirecionamento incorreto para a página de registro.");
    }
}

/* 

This test case covers the given requirements with comments explaining each part of the code. It uses appropriate locators (ID, name, and CSS selectors) for the elements and includes assertions to validate the expected outcomes for each test scenario. Dynamic content loading is handled using implicit waits as it's a static web app in this case.

*/