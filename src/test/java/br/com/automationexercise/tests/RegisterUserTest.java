package br.com.automationexercise.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.automationexercise.base.BaseTest;
import br.com.automationexercise.pages.HomePage;
import br.com.automationexercise.pages.SignupPage;
import br.com.automationexercise.pages.SignupPage.AccountInfo;
import br.com.automationexercise.pages.SignupPage.Title;

/**
 * Test Case 1: Register User.
 *
 * <p>Executa o fluxo completo de cadastro de um novo usuario e, ao final, exclui
 * a conta criada (passos 17 e 18 do roteiro), de forma que a execucao do teste
 * nao deixe dados residuais no site.
 */
public class RegisterUserTest extends BaseTest {

	@Test
	void deveRegistrarEDepoisExcluirUmNovoUsuario() {
		String nome = "QA Automation";
		// E-mail unico por execucao para evitar o erro "Email Address already exist!"
		String email = "qa.automation+" + System.currentTimeMillis() + "@teste.com";

		// 1/2/3. Launch browser / Navigate to automationexercise.com / Verify home page visible
		HomePage homePage = new HomePage(driver);
		assertTrue(homePage.isHomePageVisible(), "A home page deveria estar visivel");

		// 4. Click on 'Signup / Login' button
		homePage.clickSignupLogin();

		// 5. Verify 'New User Signup!' is visible
		SignupPage signupPage = new SignupPage(driver);
		assertTrue(signupPage.isSignupFormVisible(), "O formulario 'New User Signup!' deveria estar visivel");

		// 6/7. Enter name and email address / Click 'Signup' button
		signupPage.signup(nome, email);

		// 8. Verify that 'ENTER ACCOUNT INFORMATION' is visible
		assertTrue(signupPage.isAccountInfoFormVisible(),
				"O formulario 'Enter Account Information' deveria estar visivel");

		// 9/10/11/12/13. Fill details, marcar checkboxes de newsletter/ofertas e criar a conta
		AccountInfo accountInfo = AccountInfo.builder()
				.title(Title.MR)
				.password("SenhaForte123!")
				.dateOfBirth("15", "5", "1995")
				.firstName("QA")
				.lastName("Automation")
				.company("QA Corp")
				.address("Rua de Teste, 123")
				.address2("Apto 1")
				.country("Canada")
				.state("SP")
				.city("Sao Paulo")
				.zipcode("01000000")
				.mobileNumber("11999999999")
				.build();
		signupPage.fillAccountInformationAndCreate(accountInfo);

		// 14. Verify that 'ACCOUNT CREATED!' is visible
		assertTrue(signupPage.isAccountCreatedVisible(), "'ACCOUNT CREATED!' deveria estar visivel");

		// 15. Click 'Continue' button
		signupPage.clickContinue();

		// 16. Verify that 'Logged in as username' is visible
		assertTrue(signupPage.isLoggedInAsVisible(nome), "'Logged in as " + nome + "' deveria estar visivel");

		// 17. Click 'Delete Account' button
		signupPage.clickDeleteAccount();

		// 18. Verify that 'ACCOUNT DELETED!' is visible and click 'Continue' button
		assertTrue(signupPage.isAccountDeletedVisible(), "'ACCOUNT DELETED!' deveria estar visivel");
		signupPage.clickContinue();

		// O botao 'Continue' da tela 'ACCOUNT DELETED!' retorna para a home page.
		assertTrue(homePage.isHomePageVisible(), "A home page deveria estar visivel apos excluir a conta");
	}
}
