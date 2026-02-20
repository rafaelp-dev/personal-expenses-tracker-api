# Personal Expenses Tracker
API REST em Spring Boot para controlar e analisar seus gastos pessoais com filtros e relatórios inteligentes.

## Arquitetura
A aplicação segue uma arquitetura em camadas clássica do Spring Boot:
- **Controller** - Endpoints: mapeamento REST e validação inicial dos dados (`@Valid`).
- **Service** - Regras de negócio: lógica principal da aplicação, processamento de dados e chamadas ao repositório.
- **Repository** - Acesso a dados: interface com o banco de dados utilizando Spring Data JPA.
- **DTOs** - Transferência de dados: objetos para entrada (Request) e saída (Response) de dados da API.
- **Model/Entity** - Mapeamento Objeto-Relacional (ORM): representação das tabelas do banco de dados.

## Tecnologias
- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Funcionalidades / Endpoints

### Usuários (`/users`)
- `GET /users` - Lista todos os usuários cadastrados.
- `GET /users/{id}` - Busca um usuário específico pelo ID.
- `POST /users` - Cadastra um novo usuário.
- `DELETE /users/{id}` - Remove um usuário pelo ID.
- `PATCH /users/{id}` - Atualiza os dados de um usuário pelo ID.
- `GET /users/{id}/expenses` - Lista todos os gastos de um usuário específico.

### Gastos (`/expenses`)
- `GET /expenses` - Lista todos os gastos registrados.
- `GET /expenses/{id}` - Busca um gasto específico pelo ID.
- `POST /expenses` - Cadastra um novo gasto (requer um usuário existente).
- `DELETE /expenses/{id}` - Remove um gasto pelo ID.
- `PATCH /expenses/{id}` - Atualiza os dados de um gasto pelo ID.

## Configuração
Antes de executar o projeto, é necessário configurar o banco de dados PostgreSQL.
As configurações estão no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/personal-expenses-tracker
spring.datasource.username=postgres
spring.datasource.password=senhapost
spring.jpa.hibernate.ddl-auto=update
```

## Como executar
1. Clone o repositório.
2. Navegue até a pasta do projeto.
3. Execute o comando Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou, se tiver o Maven instalado:
   ```bash
   mvn spring-boot:run
   ```
4. A aplicação estará disponível em `http://localhost:8080`.

## Exemplos de requisições

### Criar usuário
**POST** `/users`
```json
{
  "name": "Rafael",
  "email": "rafael@email.com"
}
```

### Criar gasto
**POST** `/expenses`
```json
{
  "name": "Almoço",
  "category": "Alimentação",
  "price": 45.50,
  "userId": 1
}
```

### Response (Exemplo)
```json
{
  "id": 1,
  "name": "Almoço",
  "category": "Alimentação",
  "price": 45.50,
  "user": {
      "id": 1,
      "name": "Rafael"
  }
}
```

## Observações e melhorias futuras
- Implementar paginação nas listagens de usuários e gastos.
- Adicionar autenticação e segurança (Spring Security).
- Criar relatórios de gastos por período (mensal/semanal).
- Adicionar testes unitários e de integração.
- Documentar a API com Swagger/OpenAPI.

## Licença e contato
Este projeto é para fins educacionais.
Desenvolvido por Rafael.