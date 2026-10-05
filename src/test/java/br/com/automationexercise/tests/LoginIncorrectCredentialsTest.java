package br.com.automationexercise.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.automationexercise.base.BaseTest;
import br.com.automationexercise.pages.HomePage;
import br.com.automationexercise.pages.LoginPage;

/**
 * Test Case 3: Login User with incorrect email and password.
 *
 * <p>Os dados de entrada foram definidos com particionamento em classes de
 * equivalencia e analise de valor limite (detalhado em {@code docs/casos-de-teste.md}):
 *
 * <ul>
 *   <li>EC valida: e-mail com formato sintaticamente correto, porem nao cadastrado
 *       -&gt; o formulario e enviado ao servidor, que responde com a mensagem
 *       "Your email or password is incorrect!".</li>
 *   <li>EC invalida (formato): e-mail malformado (sem "@") -&gt; o proprio campo
 *       {@code input[type=email]} bloqueia o envio via validacao HTML5, sem
 *       round-trip ao servidor.</li>
 *   <li>EC invalida (obrigatoriedade): e-mail ou senha vazios -&gt; bloqueado pelo
 *       atributo HTML5 {@code required}.</li>
 *   <li>Valor limite inferior: menor e-mail sintaticamente valido e senha de 1
 *       caractere.</li>
 *   <li>Valor limite superior: e-mail e senha com 100+ caracteres.</li>
 * </ul>
 */
public class LoginIncorrectCredentialsTest extends BaseTest {

	private static final String EXPECTED_ERROR_MESSAGE = "Your email or password is incorrect!";

	/**
	 * Massa de dados: id | descricao | email | senha | round-trip esperado ao servidor.
	 * "round-trip esperado" = true quando o e-mail tem formato valido e tanto e-mail
	 * quanto senha estao preenchidos (o navegador deixa o formulario ser submetido).
	 */
	static Stream<Arguments> loginData() {
		return Stream.of(
				Arguments.of("CT01 - Email valido nao cadastrado + senha incorreta (classe valida)",
						"usuario_nao_cadastrado@teste.com", "SenhaErrada123", true),
				Arguments.of("CT02 - Email valido + senha vazia (classe invalida: campo obrigatorio)",
						"usuario2@teste.com", "", false),
				Arguments.of("CT03 - Email vazio + senha valida (classe invalida: campo obrigatorio)", "",
						"SenhaQualquer123", false),
				Arguments.of("CT04 - Email com formato invalido, sem '@' (classe invalida: formato)",
						"usuarioinvalido.com", "SenhaQualquer123", false),
				Arguments.of("CT05 - Valor limite inferior: email minimo valido + senha de 1 caractere", "a@b.co",
						"1", true),
				Arguments.of("CT06 - Valor limite superior: email e senha com 100+ caracteres",
						"u".repeat(100) + "@teste.com", "s".repeat(100), true));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("loginData")
	void deveTratarLoginComEmailESenhaIncorretos(String descricao, String email, String password,
			boolean expectServerRoundTrip) {
		// 1/2/3. Launch browser / Navigate to automationexercise.com / Verify home page visible
		HomePage homePage = new HomePage(driver);
		assertTrue(homePage.isHomePageVisible(), "A home page deveria estar visivel");

		// 4. Click on 'Signup / Login' button
		homePage.clickSignupLogin();

		// 5. Verify 'Login to your account' is visible
		LoginPage loginPage = new LoginPage(driver);
		assertTrue(loginPage.isLoginFormVisible(), "O formulario 'Login to your account' deveria estar visivel");

		// 6/7. Enter incorrect email address and password / Click 'login' button
		loginPage.login(email, password);

		if (expectServerRoundTrip) {
			// 8. Verify error 'Your email or password is incorrect!' is visible
			assertTrue(loginPage.isErrorMessageVisible(),
					"A mensagem de erro do servidor deveria estar visivel para: " + descricao);
			assertEquals(EXPECTED_ERROR_MESSAGE, loginPage.getErrorMessageText());
		} else {
			// O navegador bloqueia o submit (HTML5: required / type=email), portanto a
			// pagina permanece em /login e a mensagem de erro do servidor nao aparece.
			assertTrue(driver.getCurrentUrl().contains("/login"),
					"A pagina deveria permanecer em /login para: " + descricao);
			assertFalse(loginPage.isErrorMessageVisible(),
					"A mensagem de erro do servidor NAO deveria aparecer (validacao client-side) para: " + descricao);
		}
	}
}
