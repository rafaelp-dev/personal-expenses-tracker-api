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
  "category": "Alimentação",
  "price": 45.50,
  "userId": 1
}
```

## Testes

```powershell
.\mvnw.cmd test
```
