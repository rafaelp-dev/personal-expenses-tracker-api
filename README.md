# Personal Expenses Tracker

API REST em Spring Boot para controlar gastos pessoais.

## Tecnologias

- Java 21
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Security com JWT
- PostgreSQL
- Maven

## Endpoints

### Autenticação

- `POST /auth/register` — cria uma conta de acesso.
- `POST /auth/login` — autentica e devolve um access token JWT.

### Usuários

- `GET /users`
- `GET /users/{id}`
- `POST /users`
- `PATCH /users/{id}`
- `DELETE /users/{id}`
- `GET /users/{id}/expenses`

### Gastos

- `GET /expenses`
- `GET /expenses/{id}`
- `POST /expenses`
- `PATCH /expenses/{id}`
- `DELETE /expenses/{id}`

Ao criar um gasto, informe `source` como `MAIN` para usar o saldo principal ou `SAVINGS_BOX` com `savingsBoxId` para usar uma caixinha.

### Categorias

- `POST /categories` — cria uma categoria de gasto (`EXPENSE`) ou entrada (`INCOME`).
- `GET /categories/user/{userId}` — lista as categorias do usuário.
- `GET /categories/user/{userId}?type=EXPENSE` — filtra as categorias pelo tipo.
- `DELETE /categories/{id}` — exclui uma categoria que ainda não esteja em uso.

```json
{
  "name": "Alimentação",
  "type": "EXPENSE",
  "userId": 1
}
```

### Receitas e saldos

- `POST /incomes` — registra uma receita no saldo principal ou em uma caixinha.
- `GET /users/{id}/incomes` — lista o histórico de receitas do usuário.
- `GET /users/{id}/balance` — retorna saldo principal, total das caixinhas e saldo total.

### Caixinhas

- `POST /savings-boxes` — cria uma caixinha com saldo inicial.
- `GET /users/{id}/savings-boxes` — lista as caixinhas e seus saldos.

## Configuração

Configure o PostgreSQL em `src/main/resources/application.properties` e defina o segredo JWT por variável de ambiente.

No PowerShell:

```powershell
$env:APP_AUTH_JWT_SECRET="um-segredo-com-pelo-menos-32-caracteres"
```

O token dura 3.600 segundos por padrão. Para alterar:

```powershell
$env:APP_AUTH_JWT_EXPIRATION_SECONDS="7200"
```

## Como executar

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

## Autenticação JWT

Crie uma conta de acesso:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Seu Nome","email":"voce@email.com","password":"uma-senha-forte"}'
```

Solicite um token:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"voce@email.com","password":"uma-senha-forte"}'
```

Resposta:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

Use o token nos demais endpoints:

```bash
curl http://localhost:8080/users \
  -H "Authorization: Bearer SEU_TOKEN"
```

Não há refresh token. Quando o JWT expirar, faça login novamente.

## Exemplos

Criar usuário:

```json
{
  "name": "Rafael",
  "email": "rafael@email.com",
  "password": "uma-senha-forte"
}
```

Criar gasto:

```json
{
  "name": "Almoço",
  "categoryId": 1,
  "price": 45.50,
  "userId": 1,
  "source": "MAIN"
}
```

Adicionar salário ao saldo principal:

```json
{
  "description": "Salário",
  "amount": 5000.00,
  "categoryId": 2,
  "userId": 1,
  "destination": "MAIN"
}
```

Criar uma caixinha:

```json
{
  "name": "Viagem",
  "userId": 1,
  "initialBalance": 500.00
}
```

Adicionar receita a uma caixinha:

```json
{
  "description": "Valor extra",
  "amount": 200.00,
  "categoryId": 2,
  "userId": 1,
  "destination": "SAVINGS_BOX",
  "savingsBoxId": 1
}
```

Registrar uma saída da caixinha:

```json
{
  "name": "Passagem",
  "categoryId": 3,
  "price": 250.00,
  "userId": 1,
  "source": "SAVINGS_BOX",
  "savingsBoxId": 1
}
```

## Testes

```powershell
.\mvnw.cmd test
```
