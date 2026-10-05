# Casos de teste

Site sob teste: https://automationexercise.com/
Casos de referência: [Test Cases](https://automationexercise.com/test_cases) — **Test Case 1** (Register User) e **Test Case 3** (Login User with incorrect email and password).

## Test Case 1 — Register User

Automatizado em `src/test/java/br/com/automationexercise/tests/RegisterUserTest.java`, seguindo exatamente os 18 passos do roteiro original (cadastro completo de um novo usuário e, ao final, exclusão da conta criada, para não deixar dados residuais no site). O e-mail usado é gerado com timestamp a cada execução, evitando colisão com o erro "Email Address already exist!".

## Test Case 3 — Login User with incorrect email and password

Automatizado em `src/test/java/br/com/automationexercise/tests/LoginIncorrectCredentialsTest.java`, como teste parametrizado (`@ParameterizedTest`). A massa de dados foi definida com **particionamento em classes de equivalência (EP)** e **análise de valor limite (BVA)**, considerando as duas restrições reais dos campos do formulário (inspecionadas diretamente no HTML da página `/login`):

```html
<input type="email" data-qa="login-email" name="email" required />
<input type="password" data-qa="login-password" name="password" required />
```

Ou seja, o campo de e-mail usa validação nativa do navegador (`type="email"`) e ambos os campos são `required`. Isso gera **duas classes de resultado diferentes** para um login "incorreto":

- **Classe A — chega ao servidor**: e-mail com formato sintaticamente válido e ambos os campos preenchidos → o navegador permite o submit, o backend responde e a mensagem **"Your email or password is incorrect!"** é exibida.
- **Classe B — bloqueado no cliente**: e-mail vazio, senha vazia, ou e-mail com formato inválido → a validação HTML5 (`required` / `type=email`) impede o envio do formulário; a página permanece em `/login` e a mensagem do servidor **não** chega a aparecer.

### Classes de equivalência

| Classe | Campo | Partição | Exemplo |
| --- | --- | --- | --- |
| EC1 (válida) | e-mail | formato sintaticamente correto, não cadastrado | `usuario_nao_cadastrado@teste.com` |
| EC2 (inválida) | e-mail | formato malformado (sem `@`) | `usuarioinvalido.com` |
| EC3 (inválida) | e-mail | vazio (obrigatoriedade) | `""` |
| EC4 (válida) | senha | qualquer string não vazia | `SenhaErrada123` |
| EC5 (inválida) | senha | vazia (obrigatoriedade) | `""` |

### Valores limite

| Limite | Campo | Exemplo |
| --- | --- | --- |
| Inferior (menor e-mail válido) | e-mail | `a@b.co` |
| Inferior (menor senha não vazia) | senha | `1` (1 caractere) |
| Superior (string longa) | e-mail / senha | 100+ caracteres cada |

### Massa de dados utilizada (6 entradas, ≥ 5 exigidas)

| ID | Descrição | E-mail | Senha | Classificação | Resultado esperado |
| --- | --- | --- | --- | --- | --- |
| CT01 | Caso base do Test Case 3 | `usuario_nao_cadastrado@teste.com` | `SenhaErrada123` | EC1 + EC4 | Mensagem "Your email or password is incorrect!" visível |
| CT02 | Senha vazia | `usuario2@teste.com` | `""` | EC1 + EC5 | Bloqueado pelo navegador (`required`); permanece em `/login` |
| CT03 | E-mail vazio | `""` | `SenhaQualquer123` | EC3 + EC4 | Bloqueado pelo navegador (`required`); permanece em `/login` |
| CT04 | E-mail sem `@` | `usuarioinvalido.com` | `SenhaQualquer123` | EC2 + EC4 | Bloqueado pelo navegador (`type=email`); permanece em `/login` |
| CT05 | Valor limite inferior | `a@b.co` | `1` | EC1 + EC4 (limite) | Mensagem "Your email or password is incorrect!" visível |
| CT06 | Valor limite superior | 100 chars + `@teste.com` | 100 chars | EC1 + EC4 (limite) | Mensagem "Your email or password is incorrect!" visível |

Essa massa cobre: a classe válida (base do Test Case 3 original), as classes inválidas de formato e de obrigatoriedade, e os limites inferior/superior de tamanho — sem usar nenhuma credencial real cadastrada no site.
