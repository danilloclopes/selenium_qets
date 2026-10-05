package br.com.automationexercise.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Representa a Home Page de https://automationexercise.com/.
 */
public class HomePage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	private final By sliderCarousel = By.id("slider-carousel");
	private final By signupLoginLink = By.linkText("Signup / Login");

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	/**
	 * Passo "Verify that home page is visible successfully" dos Test Cases 1 e 3.
	 */
	public boolean isHomePageVisible() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(sliderCarousel));
			return driver.getCurrentUrl().startsWith("https://automationexercise.com");
		} catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * Passo "Click on 'Signup / Login' button".
	 */
	public void clickSignupLogin() {
		JsActions.click(driver, wait.until(ExpectedConditions.elementToBeClickable(signupLoginLink)));
	}
}
