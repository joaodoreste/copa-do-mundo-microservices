# ⚽ Copa do Mundo Microservices

Projeto desenvolvido para a disciplina de Arquitetura de Microservices utilizando Spring Boot e Spring Cloud.

O sistema simula um ambiente de gerenciamento de partidas e notícias relacionadas à Copa do Mundo, utilizando uma arquitetura baseada em microsserviços com descoberta de serviços, API Gateway, comunicação entre serviços e mecanismo de resiliência.

---

# 👨‍💻 Integrantes

- João Gabriel Raja
- Henrique Goldstein

---

# 🛠 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Cloud
- Eureka Server
- Spring Cloud Gateway
- OpenFeign
- Resilience4j
- PostgreSQL
- MongoDB
- Docker
- Maven

---

# 🏗 Arquitetura do Projeto

O projeto é composto pelos seguintes microsserviços:

| Serviço | Porta | Responsabilidade |
|----------|--------|------------------|
| Discovery Server | 8761 | Registro e descoberta dos serviços |
| API Gateway | 8080 | Ponto único de entrada da aplicação |
| Partida Service | 8081 | Cadastro e consulta de partidas |
| Notícia Service | 8082 | Cadastro e consulta de notícias |

---

# 📋 Pré-requisitos

Antes de executar o projeto é necessário possuir instalado:

- Java 21
- Maven
- PostgreSQL
- Docker Desktop
- IntelliJ IDEA

---

# 🐘 Configuração do PostgreSQL

O PostgreSQL é utilizado pelo serviço de partidas.

Criar o banco:

```sql
CREATE DATABASE partidas_db;
```

Configurar o arquivo:

`partida-service/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/partidas_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# 🍃 Configuração do MongoDB

O MongoDB é utilizado pelo serviço de notícias.

Subir o container Docker:

```bash
docker run --name mongodb-copa -p 27017:27017 -d mongo:latest
```

Caso o container já exista:

```bash
docker start mongodb-copa
```

Configurar o arquivo:

`noticia-service/src/main/resources/application.properties`

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/noticias_db
```

---

# 🚀 Como Executar o Projeto

Inicie os serviços na seguinte ordem:

### 1. Discovery Server

Executar:

```text
DiscoveryServerApplication
```

Acessar:

```text
http://localhost:8761
```

---

### 2. Partida Service

Executar:

```text
PartidaServiceApplication
```

Porta:

```text
8081
```

---

### 3. Notícia Service

Executar:

```text
NoticiaServiceApplication
```

Porta:

```text
8082
```

---

### 4. API Gateway

Executar:

```text
ApiGatewayApplication
```

Porta:

```text
8080
```

---

# 🔍 Verificando o Eureka

Após iniciar todos os serviços, acessar:

```text
http://localhost:8761
```

Os seguintes serviços devem aparecer registrados:

```text
API-GATEWAY
PARTIDA-SERVICE
NOTICIA-SERVICE
```

---

# 🌐 API Gateway

O Gateway é a única porta de entrada para os microsserviços.

URL base:

```text
http://localhost:8080
```

Rotas configuradas:

| Rota | Serviço |
|--------|----------|
| /api/partidas/** | partida-service |
| /api/noticias/** | noticia-service |

---

# ⚽ Endpoints de Partidas

## Cadastrar Partida

```http
POST http://localhost:8080/api/partidas
```

Body:

```json
{
  "selecaoMandante": "México",
  "selecaoVisitante": "Coréia do Sul",
  "golsMandante": 2,
  "golsVisitante": 0,
  "fase": "Grupo",
  "status": "FINALIZADA"
}
```

---

## Listar Partidas

```http
GET http://localhost:8080/api/partidas
```

---

## Buscar Partida por ID

```http
GET http://localhost:8080/api/partidas/1
```

---

# 📰 Endpoints de Notícias

## Cadastrar Notícia

```http
POST http://localhost:8080/api/noticias
```

Body:

```json
{
  "titulo": "México vence a África do Sul",
  "conteudo": "A seleção mexicana venceu por 2x0 a África do Sul em um jogo com 3 expulsões.",
  "autor": "UOL",
  "dataPublicacao": "2026-06-12"
}
```

---

## Listar Notícias

```http
GET http://localhost:8080/api/noticias
```

---

# 🔗 Comunicação Entre Microsserviços

O serviço de notícias consulta informações do serviço de partidas utilizando OpenFeign.

Endpoint:

```http
GET http://localhost:8080/api/noticias/partida/1
```

---

# 🛡 Resiliência com Resilience4j

Foi implementado um mecanismo de fallback utilizando Resilience4j.

Quando o serviço de partidas estiver indisponível, o serviço de notícias continua respondendo normalmente através de uma resposta alternativa.

---

## Teste de Resiliência

### Cenário 1 - Serviço disponível

Com todos os microsserviços ligados:

```http
GET http://localhost:8080/api/noticias/partida/1
```

Retorno esperado:

```json
{
  "id": 1,
  "selecaoMandante": "México",
  "selecaoVisitante": "África do Sul",
  "golsMandante": 2,
  "golsVisitante": 0,
  "fase": "Grupo",
  "status": "FINALIZADA"
}
```

---

### Cenário 2 - Serviço indisponível

Parar o microsserviço:

```text
PartidaServiceApplication
```

Executar novamente:

```http
GET http://localhost:8080/api/noticias/partida/1
```

Retorno esperado:

```json
{
  "id": 1,
  "selecaoMandante": "Indisponível",
  "selecaoVisitante": "Indisponível",
  "golsMandante": 0,
  "golsVisitante": 0,
  "fase": "Indisponível",
  "status": "PARTIDA-SERVICE INDISPONÍVEL"
}
```

---

# 📂 Repositório

GitHub:

```text
https://github.com/joaodoreste/copa-do-mundo-microservices
```

---

# ✅ Funcionalidades Implementadas

- Cadastro de partidas
- Consulta de partidas
- Cadastro de notícias
- Consulta de notícias
- Service Discovery com Eureka
- API Gateway
- Comunicação entre microsserviços utilizando OpenFeign
- Persistência em PostgreSQL
- Persistência em MongoDB
- Resiliência utilizando Resilience4j
- Fallback automático em caso de indisponibilidade do serviço de partidas