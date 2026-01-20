# Personal Expenses Tracker API

API REST para controlar gastos pessoais.

## Sumário
- Arquitetura
- Tecnologias
- Modelo de dados
- Funcionalidades / Endpoints
- Validações e regras de negócio
- Configuração e execução
- Exemplos de requisições
- Possíveis melhorias

## Arquitetura
Aplicação estruturada em camadas:

- Controller - Endpoints: mapeamento REST e validação inicial.
- Service - Regras de negócio: lógica principal da aplicação.
- Repository - Persistência: interface JPA para acesso ao banco.
- Entity - Entidades: modelos de dados mapeados para tabelas.
- DTOs - Objetos de transferência de dados: para requests e responses.

A aplicação usa Spring Boot como framework principal e JPA/Hibernate para persistência em PostgreSQL.

## Tecnologias
- Java 21
- Spring Boot 3.5.4
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-validation
  - spring-boot-starter-data-jpa
- PostgreSQL
- Maven
- Lombok

## Modelo de dados
- User
  - userId: Long (PK)
  - name: String
  - email: String (único)

- Expense
  - expenseId: Long (PK)
  - name: String
  - category: String
  - price: BigDecimal
  - date: LocalDateTime (inserido automaticamente)
  - user (ManyToOne -> UserEntity)

## Funcionalidades / Endpoints
Base path para usuários: `/users`
- GET /users
  - Retorna lista de usuários.
- GET /users/{id}
  - Retorna usuário por ID.
- POST /users
  - Cria um novo usuário.
- DELETE /users/{id}
  - Remove usuário por ID.
- PATCH /users/{id}
  - Atualiza campos opcionais.
- GET /users/{id}/expenses
  - Lista todos os gastos associados a um usuário.

Base path para gastos: `/expenses`
- GET /expenses
  - Retorna lista de gastos.
- GET /expenses/{id}
  - Retorna gasto por ID.
- POST /expenses
  - Cria um gasto.
- DELETE /expenses/{id}
  - Remove gasto por ID.
- PATCH /expenses/{id}
  - Atualiza campos opcionais do gasto.

## Validações e regras de negócio importantes
- Validações de request via Jakarta Validation (anotações como `@NotBlank`, `@NotNull`, `@Positive`).
- Ao criar usuário, a aplicação verifica se o email já existe e retorna HTTP 409 CONFLICT em caso afirmativo.
- Ao criar/atualizar gasto, o `userId` informado deve existir; caso contrário, retorna HTTP 404.
- Formato de data é `LocalDateTime` (campo `date` preenchido automaticamente no servidor na criação).

## Configuração
As propriedades do projeto estão em `src/main/resources/application.properties`.

spring.datasource.url=SUA_URL_AQUI
spring.datasource.username=SEU_USUARIO_AQUI
spring.datasource.password=SUA_SENHA_AQUI

spring.jpa.hibernate.ddl-auto=update

server.error.include-stacktrace=never

Atenção: troque `spring.datasource.password` e outros dados conforme seu ambiente local. Para produção, prefira variáveis de ambiente.

## Como executar
Pré-requisitos:
- JDK 21 instalado
- PostgreSQL disponível e um banco criado (ex.: `personal-expenses-tracker`)

Executar com Maven wrapper:

```bash
./mvnw spring-boot:run
```

## Exemplos de requisições
Criar usuário (POST /users)

Request JSON:
{
  "name": "nome",
  "email": "nome@example.com"
}

Resposta (201 Created): exemplo de `UserResponseDto`:
{
  "id": 1,
  "name": "nome",
  "email": "nome@example.com"
}

Criar gasto (POST /expenses)

Request JSON:
{
  "name": "Almoço",
  "category": "Alimentação",
  "price": 25.50,
  "userId": 1
}

Resposta (201 Created): exemplo de `ExpenseResponseDto`:
{
  "expenseId": 1,
  "name": "Almoço",
  "category": "Alimentação",
  "price": 25.50,
  "date": "2026-01-20T12:34:56",
  "userName": "Rafael"
}

## Observações e melhorias futuras
- Adicionar autenticação/autorização JWT para proteger endpoints.
- Implementar paginação e filtros nos endpoints de listagem (ex.: por data, categoria, intervalo de preços).
- Adicionar migrações de banco (Flyway ou Liquibase).
- Adicionar métricas/monitoramento e documentação automática da API (Swagger/OpenAPI).

## Licença e contato
Projeto pessoal — Para dúvidas ou contribuições, abra uma issue ou entre em contato com o autor do repositório.