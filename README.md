# Selenium QETS — automationexercise.com

Testes end-to-end com **Selenium WebDriver** + **JUnit 5** para o site [automationexercise.com](https://automationexercise.com/), cobrindo:

- **Test Case 3** — Login User with incorrect email and password
- **Test Case 1** — Register User

A atividade pede o uso de **particionamento em classes de equivalência** e **análise de valor limite** para pensar a massa de dados do teste de login (mínimo de 5 entradas). O racional completo está documentado em [`docs/casos-de-teste.md`](docs/casos-de-teste.md).

## Stack

- Java 11
- Selenium WebDriver 4.26.0
- WebDriverManager 5.9.2 (baixa/gerencia o driver do Chrome automaticamente)
- JUnit 5 (Jupiter), incluindo `@ParameterizedTest`
- Maven

## Estrutura do projeto

```
selenium_qets/
├── pom.xml
├── descricao.txt                  # enunciado original da atividade
├── docs/
│   └── casos-de-teste.md          # classes de equivalência / valor limite
└── src/test/java/br/com/automationexercise/
    ├── base/
    │   └── BaseTest.java          # setup/teardown do WebDriver (@BeforeEach/@AfterEach)
    ├── pages/                     # Page Objects
    │   ├── HomePage.java
    │   ├── LoginPage.java
    │   └── SignupPage.java
    └── tests/
        ├── LoginIncorrectCredentialsTest.java   # Test Case 3 (parametrizado)
        └── RegisterUserTest.java                # Test Case 1
```

## Como executar

Pré-requisitos: JDK 11+, Maven e Google Chrome instalados.

```bash
mvn test
```

Para rodar em modo headless (ex.: CI):

```bash
mvn test -Dheadless=true
```

O `WebDriverManager` baixa automaticamente a versão correta do ChromeDriver compatível com o Chrome instalado na máquina — não é necessário configurar nada manualmente.

## Observações sobre o design dos testes

- O campo de e-mail do formulário de login usa `<input type="email" required>`. Isso significa que, para e-mails vazios ou com formato inválido, o **próprio navegador** bloqueia o envio do formulário via validação HTML5, sem round-trip ao servidor. O teste parametrizado diferencia esses dois comportamentos esperados (ver `docs/casos-de-teste.md`).
- `RegisterUserTest` gera um e-mail único por execução (timestamp) para evitar o erro "Email Address already exist!", e exclui a conta criada ao final (passos 17 e 18 do roteiro original), não deixando dados residuais no site.
- Os localizadores usam preferencialmente os atributos `data-qa` expostos pelo próprio site (pensados para automação) e `id`s estáveis dos campos do formulário de cadastro.
