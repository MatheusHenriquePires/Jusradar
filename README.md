<img width="991" height="1133" alt="image" src="https://github.com/user-attachments/assets/38543343-67fb-4944-a5bd-04caadea6cbd" /># JusRadar

JusRadar e uma API backend em Java com Spring Boot para consulta e monitoramento de processos judiciais. O projeto integra com a API publica do DataJud, permite cadastro/login de usuarios e possui base para registrar monitoramentos por cliente e advogado.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- JWT
- Lombok
- Springdoc OpenAPI/Swagger
- Maven Wrapper

## Funcionalidades

- Cadastro de usuarios.
- Login com retorno de token JWT.
- Consulta de processos por numero/documento e tribunal via DataJud.
- Cadastro de monitoramentos de processos.
- Migracoes de banco com Flyway.
- Documentacao Swagger em `/docs`.

## Requisitos

- Java 21 instalado.
- PostgreSQL em execucao.
- Banco de dados `jusradar` criado.
- Chave de API do DataJud.

## Configuracao

As configuracoes principais estao em `src/main/resources/application.properties`.

Exemplo de configuracao local:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/jusradar}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true

jwt.secret=${JWT_SECRET:change-me-local-development-secret}
jwt.expiration=${JWT_EXPIRATION:86400000}

datajud.base-url=${DATAJUD_BASE_URL:https://api-publica.datajud.cnj.jus.br}
datajud.api-key=${DATAJUD_API_KEY:}

springdoc.swagger-ui.path=/docs
```

Configure os valores sensiveis por variaveis de ambiente:

```bash
export JWT_SECRET="sua-chave-jwt"
export DATAJUD_API_KEY="sua-chave-datajud"
```

> Evite publicar chaves reais no repositorio. Para ambientes compartilhados ou producao, prefira variaveis de ambiente ou secrets da plataforma de deploy.

## Banco de dados

Crie o banco local antes de iniciar a aplicacao:

```sql
CREATE DATABASE jusradar;
```

As tabelas sao criadas/validadas pelas migrations do Flyway em:

```text
src/main/resources/db/migration
```

## Como executar

Na raiz do projeto, rode:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API ficara disponivel por padrao em:

```text
http://localhost:8080
```

## Testes

Para executar os testes:

```bash
./mvnw test
```

## Endpoints principais

### Autenticacao

Cadastro:

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "123456"
}
```

Login:

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "maria@email.com",
  "senha": "123456"
}
```

### Consulta de processos

```http
GET /api/v1/processos?documento=00000000000000000000&tribunal=TJPI
```

Tribunais tratados atualmente:

- `TJPI`
- `TRF1`
- `TRF2`

### Monitoramentos

```http
POST /api/v1/monitoramentos
Content-Type: application/json

{
  "numeroProcesso": "00000000000000000000",
  "tribunal": "TJPI"
}
```

## Swagger

Com a aplicacao rodando, acesse:

```text
http://localhost:8080/docs
```

## Estrutura do projeto

```text
src/main/java/br/com/jusradar
├── config
├── consulta
├── identity
├── monitoramento
└── shared
```

- `consulta`: integracao e consulta de processos no DataJud.
- `identity`: cadastro, login, usuarios e JWT.
- `monitoramento`: cadastro e rotinas de monitoramento.
- `config` e `shared`: configuracoes e utilitarios compartilhados.
<img width="991" height="1133" alt="image" src="https://github.com/user-attachments/assets/8319ef87-b2bd-419b-89ac-6e449e55578a" />

