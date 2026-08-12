# Personal Expenses Tracker API

API REST para gerenciamento de finanças pessoais, desenvolvida com Java e Spring Boot. A aplicação permite organizar receitas, despesas, categorias e reservas financeiras, oferecendo uma visão consolidada do saldo de cada usuário.

## Funcionalidades

- Cadastro e autenticação de usuários
- Autenticação stateless com JSON Web Token (JWT)
- Registro, consulta, atualização e exclusão de despesas
- Registro e consulta de receitas
- Criação de categorias para receitas e despesas
- Controle de saldo principal
- Criação de caixinhas com saldo independente
- Movimentações financeiras no saldo principal ou em caixinhas
- Consulta do histórico financeiro por usuário
- Cálculo do saldo principal, saldo das caixinhas e saldo total
- Validação dos dados recebidos pela API
- Controle de acesso aos endpoints protegidos

## Tecnologias

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- Spring Security
- OAuth2 Resource Server
- JWT com algoritmo HS256
- PostgreSQL
- H2 Database para testes
- Maven
- Lombok
- Docker

## Arquitetura

O projeto segue uma arquitetura em camadas para separar responsabilidades e facilitar a manutenção:

```text
controllers  → exposição dos endpoints REST
services     → regras de negócio
repositories → acesso e persistência dos dados
entities     → mapeamento das entidades do banco
dtos         → objetos de entrada e saída da API
config       → segurança, autenticação e CORS
```

## Principais regras de negócio

- As categorias são classificadas como `INCOME` ou `EXPENSE`.
- Receitas podem ser destinadas ao saldo principal ou a uma caixinha.
- Despesas podem utilizar o saldo principal ou o saldo de uma caixinha.
- Movimentações em caixinhas devem informar o identificador da caixinha.
- Categorias que já estão em uso não podem ser excluídas.
- As senhas são armazenadas de forma segura por meio de um `PasswordEncoder`.
- Apenas cadastro e login são públicos; os demais recursos exigem um token válido.

## Endpoints

### Autenticação

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/auth/register` | Cadastra uma conta |
| `POST` | `/auth/login` | Autentica o usuário e retorna um JWT |

### Usuários

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/users` | Lista os usuários |
| `GET` | `/users/{id}` | Consulta um usuário |
| `POST` | `/users` | Cria um usuário |
| `PATCH` | `/users/{id}` | Atualiza um usuário |
| `DELETE` | `/users/{id}` | Exclui um usuário |
| `GET` | `/users/{id}/expenses` | Lista as despesas do usuário |
| `GET` | `/users/{id}/incomes` | Lista as receitas do usuário |
| `GET` | `/users/{id}/savings-boxes` | Lista as caixinhas do usuário |
| `GET` | `/users/{id}/balance` | Retorna os saldos do usuário |

### Despesas

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/expenses` | Lista as despesas |
| `GET` | `/expenses/{id}` | Consulta uma despesa |
| `POST` | `/expenses` | Registra uma despesa |
| `PATCH` | `/expenses/{id}` | Atualiza uma despesa |
| `DELETE` | `/expenses/{id}` | Exclui uma despesa |

### Receitas, categorias e caixinhas

| Método | Rota | Descrição |
| --- | --- | --- |
| `POST` | `/incomes` | Registra uma receita |
| `POST` | `/categories` | Cria uma categoria |
| `GET` | `/categories/user/{userId}` | Lista as categorias do usuário |
| `GET` | `/categories/user/{userId}?type=EXPENSE` | Filtra as categorias por tipo |
| `DELETE` | `/categories/{id}` | Exclui uma categoria que não esteja em uso |
| `POST` | `/savings-boxes` | Cria uma caixinha |

## Autenticação

Após o login, a API retorna um access token que deve ser enviado nos endpoints protegidos:

```http
Authorization: Bearer SEU_TOKEN
```

Exemplo de login:

```json
{
  "email": "rafael@email.com",
  "password": "uma-senha-forte"
}
```

Exemplo de resposta:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Exemplos

### Registrar uma despesa

```json
{
  "name": "Almoço",
  "categoryId": 1,
  "price": 45.50,
  "userId": 1,
  "source": "MAIN"
}
```

### Registrar uma receita

```json
{
  "description": "Salário",
  "amount": 5000.00,
  "categoryId": 2,
  "userId": 1,
  "destination": "MAIN"
}
```

### Criar uma caixinha

```json
{
  "name": "Viagem",
  "userId": 1,
  "initialBalance": 500.00
}
```

Para movimentar uma caixinha, utilize `SAVINGS_BOX` como origem ou destino e informe também `savingsBoxId`.

## Como executar

Configure um banco PostgreSQL e defina as variáveis de ambiente:

```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/personal-expenses-tracker"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="sua-senha"
$env:DATABASE_MAX_POOL_SIZE="5"
$env:DATABASE_MIN_IDLE="0"
$env:DATABASE_CONNECTION_TIMEOUT="30000"
$env:DATABASE_KEEPALIVE_TIME="120000"
$env:JPA_DDL_AUTO="update"
$env:APP_AUTH_JWT_SECRET="um-segredo-com-pelo-menos-32-caracteres"
$env:APP_AUTH_JWT_EXPIRATION_SECONDS="3600"
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173,http://127.0.0.1:5173"
$env:PORT="8080"
```

Todas as variáveis acima são obrigatórias. A aplicação não utiliza valores padrão quando alguma configuração está ausente.

Inicie a aplicação no Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.
