package br.com.automationexercise.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * O site injeta anuncios "Google Ad Manager Vignette" (intersticiais de pagina
 * inteira) que sao disparados apos alguns cliques "confiaveis" (reais, gerados
 * pelo SO/WebDriver) que resultam em navegacao. Um clique disparado via
 * JavaScript ({@code element.click()}) nao conta como gesto de usuario
 * confiavel para a politica de anuncios, entao evita esse intersticial sem
 * alterar o comportamento funcional do clique (envio de formulario/navegacao
 * de link continuam funcionando normalmente).
 */
final class JsActions {

	private JsActions() {
	}

	static void click(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
}
