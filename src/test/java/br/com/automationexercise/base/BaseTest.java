package br.com.automationexercise.base;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Classe base dos testes end-to-end do site https://automationexercise.com/.
 *
 * Segue o mesmo padrao usado como referencia (WebDriverManager + JUnit 5
 * {@code @BeforeEach}/{@code @AfterEach}), centralizando a criacao e o
 * encerramento do WebDriver para as classes de teste.
 *
 * Para rodar em modo headless (ex.: pipelines de CI), basta definir a
 * propriedade de sistema {@code -Dheadless=true} na execucao do Maven:
 * {@code mvn test -Dheadless=true}.
 */
public abstract class BaseTest {

	protected static final String BASE_URL = "https://automationexercise.com";

	protected WebDriver driver;

	@BeforeEach
	public void createDriver() {
		ChromeOptions options = new ChromeOptions();
		if (Boolean.getBoolean("headless")) {
			options.addArguments("--headless=new", "--window-size=1920,1080");
		}
		driver = WebDriverManager.chromedriver().capabilities(options).create();
		// Sem implicit wait: as Page Objects usam WebDriverWait (espera explicita) nos
		// pontos de verificacao, que e a pratica recomendada e evita combinar os dois
		// mecanismos (o que gera tempos de espera imprevisiveis).
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		driver.manage().window().maximize();
		driver.get(BASE_URL);
	}

	@AfterEach
	public void quitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}
}
