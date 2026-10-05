package br.com.automationexercise.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Representa as telas do fluxo de cadastro de usuario em automationexercise.com:
 * "New User Signup!" -&gt; "ENTER ACCOUNT INFORMATION" -&gt; "ACCOUNT CREATED!" -&gt;
 * conta logada -&gt; "ACCOUNT DELETED!" (Test Case 1 - Register User).
 */
public class SignupPage {

	public enum Title {
		MR, MRS
	}

	private final WebDriver driver;
	// Timeout generoso: os passos de criacao/exclusao de conta envolvem escrita no
	// backend do site, que por vezes demora mais que o esperado para redirecionar.
	private final WebDriverWait wait;

	// Etapa 1: "New User Signup!"
	private final By signupFormTitle = By.xpath("//h2[text()='New User Signup!']");
	private final By signupNameInput = By.cssSelector("input[data-qa='signup-name']");
	private final By signupEmailInput = By.cssSelector("input[data-qa='signup-email']");
	private final By signupButton = By.cssSelector("button[data-qa='signup-button']");
	// Mensagem de erro quando o e-mail ja existe: <p style="color: red;">Email Address already exist!</p>
	private final By signupErrorMessage = By.cssSelector(".signup-form p");

	// Etapa 2: "ENTER ACCOUNT INFORMATION"
	private final By accountInfoTitle = By.xpath("//b[text()='Enter Account Information']");
	private final By titleMr = By.id("id_gender1");
	private final By titleMrs = By.id("id_gender2");
	private final By passwordInput = By.id("password");
	private final By daysSelect = By.id("days");
	private final By monthsSelect = By.id("months");
	private final By yearsSelect = By.id("years");
	private final By newsletterCheckbox = By.id("newsletter");
	private final By optinCheckbox = By.id("optin");
	private final By firstNameInput = By.id("first_name");
	private final By lastNameInput = By.id("last_name");
	private final By companyInput = By.id("company");
	private final By addressInput = By.id("address1");
	private final By address2Input = By.id("address2");
	private final By countrySelect = By.id("country");
	private final By stateInput = By.id("state");
	private final By cityInput = By.id("city");
	private final By zipcodeInput = By.id("zipcode");
	private final By mobileNumberInput = By.id("mobile_number");
	private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

	// Etapa 3: "ACCOUNT CREATED!"
	private final By accountCreatedTitle = By.cssSelector("[data-qa='account-created']");
	private final By continueButton = By.cssSelector("[data-qa='continue-button']");

	// Etapa 4: usuario logado
	private final By loggedInAsText = By.xpath("//a[contains(text(),'Logged in as')]");
	private final By deleteAccountLink = By.cssSelector("a[href='/delete_account']");

	// Etapa 5: "ACCOUNT DELETED!"
	private final By accountDeletedTitle = By.cssSelector("[data-qa='account-deleted']");

	public SignupPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
	}

	public boolean isSignupFormVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(signupFormTitle)).isDisplayed();
	}

	/**
	 * Passos "Enter name and email address" + "Click 'Signup' button".
	 */
	public void signup(String name, String email) {
		driver.findElement(signupNameInput).sendKeys(name);
		driver.findElement(signupEmailInput).sendKeys(email);
		JsActions.click(driver, driver.findElement(signupButton));
	}

	public boolean isSignupErrorMessageVisible() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(signupErrorMessage)).isDisplayed();
		} catch (TimeoutException e) {
			return false;
		}
	}

	public String getSignupErrorMessageText() {
		return driver.findElement(signupErrorMessage).getText();
	}

	/**
	 * Passo "Verify that 'ENTER ACCOUNT INFORMATION' is visible".
	 */
	public boolean isAccountInfoFormVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(accountInfoTitle)).isDisplayed();
	}

	/**
	 * Passos 9 a 13 do Test Case 1: preenche titulo, senha, data de nascimento,
	 * marca os checkboxes de newsletter/ofertas, preenche o endereco e confirma.
	 */
	public void fillAccountInformationAndCreate(AccountInfo info) {
		// Cliques via JS: a pagina injeta banners de anuncio (iframes) que podem
		// sobrepor fisicamente campos do formulario (ex.: os checkboxes), causando
		// ElementClickInterceptedException em cliques nativos do WebDriver.
		JsActions.click(driver, driver.findElement(info.getTitle() == Title.MR ? titleMr : titleMrs));
		driver.findElement(passwordInput).sendKeys(info.getPassword());

		new Select(driver.findElement(daysSelect)).selectByValue(info.getDay());
		new Select(driver.findElement(monthsSelect)).selectByValue(info.getMonth());
		new Select(driver.findElement(yearsSelect)).selectByValue(info.getYear());

		JsActions.click(driver, driver.findElement(newsletterCheckbox));
		JsActions.click(driver, driver.findElement(optinCheckbox));

		driver.findElement(firstNameInput).sendKeys(info.getFirstName());
		driver.findElement(lastNameInput).sendKeys(info.getLastName());
		driver.findElement(companyInput).sendKeys(info.getCompany());
		driver.findElement(addressInput).sendKeys(info.getAddress());
		driver.findElement(address2Input).sendKeys(info.getAddress2());
		new Select(driver.findElement(countrySelect)).selectByValue(info.getCountry());
		driver.findElement(stateInput).sendKeys(info.getState());
		driver.findElement(cityInput).sendKeys(info.getCity());
		driver.findElement(zipcodeInput).sendKeys(info.getZipcode());
		driver.findElement(mobileNumberInput).sendKeys(info.getMobileNumber());

		JsActions.click(driver, driver.findElement(createAccountButton));
	}

	/**
	 * Passo "Verify that 'ACCOUNT CREATED!' is visible".
	 */
	public boolean isAccountCreatedVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(accountCreatedTitle)).isDisplayed();
	}

	/**
	 * Passo "Click 'Continue' button".
	 */
	public void clickContinue() {
		JsActions.click(driver, wait.until(ExpectedConditions.elementToBeClickable(continueButton)));
	}

	/**
	 * Passo "Verify that 'Logged in as username' is visible".
	 */
	public boolean isLoggedInAsVisible(String username) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAsText));
		return element.isDisplayed() && element.getText().contains(username);
	}

	/**
	 * Passo "Click 'Delete Account' button".
	 */
	public void clickDeleteAccount() {
		JsActions.click(driver, wait.until(ExpectedConditions.elementToBeClickable(deleteAccountLink)));
	}

	/**
	 * Passo "Verify that 'ACCOUNT DELETED!' is visible".
	 */
	public boolean isAccountDeletedVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(accountDeletedTitle)).isDisplayed();
	}

	/** Dados de endereco/conta usados na etapa "ENTER ACCOUNT INFORMATION". */
	public static class AccountInfo {
		private final Title title;
		private final String password;
		private final String day;
		private final String month;
		private final String year;
		private final String firstName;
		private final String lastName;
		private final String company;
		private final String address;
		private final String address2;
		private final String country;
		private final String state;
		private final String city;
		private final String zipcode;
		private final String mobileNumber;

		private AccountInfo(Builder builder) {
			this.title = builder.title;
			this.password = builder.password;
			this.day = builder.day;
			this.month = builder.month;
			this.year = builder.year;
			this.firstName = builder.firstName;
			this.lastName = builder.lastName;
			this.company = builder.company;
			this.address = builder.address;
			this.address2 = builder.address2;
			this.country = builder.country;
			this.state = builder.state;
			this.city = builder.city;
			this.zipcode = builder.zipcode;
			this.mobileNumber = builder.mobileNumber;
		}

		public Title getTitle() {
			return title;
		}

		public String getPassword() {
			return password;
		}

		public String getDay() {
			return day;
		}

		public String getMonth() {
			return month;
		}

		public String getYear() {
			return year;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getCompany() {
			return company;
		}

		public String getAddress() {
			return address;
		}

		public String getAddress2() {
			return address2;
		}

		public String getCountry() {
			return country;
		}

		public String getState() {
			return state;
		}

		public String getCity() {
			return city;
		}

		public String getZipcode() {
			return zipcode;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {
			private Title title = Title.MR;
			private String password;
			private String day = "15";
			private String month = "5";
			private String year = "1995";
			private String firstName;
			private String lastName;
			private String company = "QA Corp";
			private String address = "Rua de Teste, 123";
			private String address2 = "Apto 1";
			private String country = "Canada";
			private String state = "SP";
			private String city = "Sao Paulo";
			private String zipcode = "01000000";
			private String mobileNumber = "11999999999";

			public Builder title(Title title) {
				this.title = title;
				return this;
			}

			public Builder password(String password) {
				this.password = password;
				return this;
			}

			public Builder dateOfBirth(String day, String month, String year) {
				this.day = day;
				this.month = month;
				this.year = year;
				return this;
			}

			public Builder firstName(String firstName) {
				this.firstName = firstName;
				return this;
			}

			public Builder lastName(String lastName) {
				this.lastName = lastName;
				return this;
			}

			public Builder company(String company) {
				this.company = company;
				return this;
			}

			public Builder address(String address) {
				this.address = address;
				return this;
			}

			public Builder address2(String address2) {
				this.address2 = address2;
				return this;
			}

			public Builder country(String country) {
				this.country = country;
				return this;
			}

			public Builder state(String state) {
				this.state = state;
				return this;
			}

			public Builder city(String city) {
				this.city = city;
				return this;
			}

			public Builder zipcode(String zipcode) {
				this.zipcode = zipcode;
				return this;
			}

			public Builder mobileNumber(String mobileNumber) {
				this.mobileNumber = mobileNumber;
				return this;
			}

			public AccountInfo build() {
				return new AccountInfo(this);
			}
		}
	}
}
