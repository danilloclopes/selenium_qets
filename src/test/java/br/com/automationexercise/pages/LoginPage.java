package br.com.automationexercise.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Representa o formulario "Login to your account" da pagina /login
 * (a mesma pagina que exibe tambem o formulario de cadastro - ver {@link SignupPage}).
 */
public class LoginPage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	private final By loginFormTitle = By.xpath("//h2[text()='Login to your account']");
	private final By emailInput = By.cssSelector("input[data-qa='login-email']");
	private final By passwordInput = By.cssSelector("input[data-qa='login-password']");
	private final By loginButton = By.cssSelector("button[data-qa='login-button']");
	// Mensagem de erro exibida pelo backend: <p style="color: red;">Your email or password is incorrect!</p>
	private final By loginErrorMessage = By.cssSelector(".login-form p");

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	/**
	 * Passo "Verify 'Login to your account' is visible".
	 */
	public boolean isLoginFormVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(loginFormTitle)).isDisplayed();
	}

	/**
	 * Passos "Enter incorrect email address and password" + "Click 'login' button".
	 */
	public void login(String email, String password) {
		WebElement emailField = driver.findElement(emailInput);
		emailField.clear();
		emailField.sendKeys(email);

		WebElement passwordField = driver.findElement(passwordInput);
		passwordField.clear();
		passwordField.sendKeys(password);

		JsActions.click(driver, driver.findElement(loginButton));
	}

	/**
	 * Passo "Verify error 'Your email or password is incorrect!' is visible".
	 * So e exibida quando o navegador aceitou submeter o formulario (ou seja,
	 * quando o campo e-mail tinha um formato valido do ponto de vista do HTML5).
	 */
	public boolean isErrorMessageVisible() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage)).isDisplayed();
		} catch (TimeoutException e) {
			return false;
		}
	}

	public String getErrorMessageText() {
		return driver.findElement(loginErrorMessage).getText();
	}

	/**
	 * O campo de e-mail usa {@code <input type="email" required>}, ou seja, para as
	 * classes de equivalencia invalidas (e-mail vazio ou com formato malformado) o
	 * proprio navegador bloqueia o submit via validacao HTML5, sem round-trip ao
	 * servidor. Este metodo expoe o resultado de {@code checkValidity()} do campo
	 * para que o teste consiga diferenciar os dois comportamentos esperados.
	 */
	public boolean isEmailFieldFlaggedInvalidByBrowser() {
		WebElement emailField = driver.findElement(emailInput);
		Object isValid = ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();",
				emailField);
		return Boolean.FALSE.equals(isValid);
	}
}
