# Copa do Mundo Microservices

## Integrantes

- João Gabriel Raja Gabaglia Doreste
- (Adicionar demais integrantes)

---

# Descrição do Projeto

Sistema distribuído baseado em microsserviços para gerenciamento de partidas e notícias da Copa do Mundo.

A solução foi desenvolvida utilizando arquitetura de microsserviços com comunicação síncrona e assíncrona, descoberta de serviços, gateway centralizado, persistência distribuída, observabilidade, resiliência e programação reativa.

---

# Arquitetura

## Discovery Server

Responsável pelo registro e descoberta dos microsserviços utilizando Eureka Server.

Porta:

```text
8761
```

---

## API Gateway

Responsável pelo roteamento centralizado das requisições para os microsserviços.

Porta:

```text
8080
```

Rotas principais:

```text
/api/partidas/**
/api/noticias/**
```

---

## Partida Service

Responsável pelo gerenciamento das partidas da Copa do Mundo.

### Tecnologias

- Spring Boot
- Spring Data JPA
- PostgreSQL
- Eureka Client
- Kafka Producer
- Spring Actuator

### Porta

```text
8081
```

### Funcionalidades

- Cadastro de partidas
- Consulta de partidas
- Publicação de eventos Kafka
- Observabilidade via Actuator

---

## Noticia Service

Responsável pelo gerenciamento das notícias.

### Tecnologias

- Spring Boot
- MongoDB
- Kafka Consumer
- OpenFeign
- Resilience4J
- Spring Actuator
- Project Reactor

### Porta

```text
8082
```

### Funcionalidades

- Cadastro de notícias
- Consulta de notícias
- Criação automática de notícias através de eventos Kafka
- Endpoint reativo
- Circuit Breaker

---

# Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Cloud
- Eureka Server
- Spring Cloud Gateway
- OpenFeign
- Apache Kafka
- PostgreSQL
- MongoDB
- Resilience4J
- Spring Boot Actuator
- Project Reactor
- Docker
- Maven

---

# Pré-requisitos

Antes de executar o projeto é necessário possuir instalado:

- Java 21
- Maven 3.9+
- Docker Desktop
- PostgreSQL
- Git

Verificar instalações:

```bash
java -version
```

```bash
mvn -version
```

```bash
docker --version
```

---

# Infraestrutura Docker

O projeto utiliza Docker para executar:

- Kafka
- MongoDB

## Iniciar containers

Na raiz do projeto:

```bash
docker compose up -d
```

Verificar containers:

```bash
docker ps
```

Resultado esperado:

```text
kafka-copa
mongo-copa
```

## Parar containers

```bash
docker compose down
```

## Recriar ambiente Docker

```bash
docker compose down -v
docker compose up -d
```

---

# Configuração do Banco PostgreSQL

Criar banco:

```sql
CREATE DATABASE partidas_db;
```

Configuração utilizada:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/partidas_db
spring.datasource.username=postgres
spring.datasource.password=12345678
```

> Ajustar usuário e senha conforme ambiente local.

---

# Ordem de Inicialização

Os serviços devem ser iniciados na seguinte ordem:

## 1. Discovery Server

Executar:

```bash
cd discovery-server
mvn spring-boot:run
```

Porta:

```text
8761
```

Acessar:

```text
http://localhost:8761
```

---

## 2. API Gateway

Executar:

```bash
cd api-gateway
mvn spring-boot:run
```

Porta:

```text
8080
```

---

## 3. Partida Service

Executar:

```bash
cd partida-service
mvn spring-boot:run
```

Porta:

```text
8081
```

---

## 4. Noticia Service

Executar:

```bash
cd noticia-service
mvn spring-boot:run
```

Porta:

```text
8082
```

---

# Verificando Registro no Eureka

Após iniciar todos os serviços, acessar:

```text
http://localhost:8761
```

Serviços esperados:

- API-GATEWAY
- PARTIDA-SERVICE
- NOTICIA-SERVICE

---

# Comunicação Síncrona

O Noticia Service utiliza OpenFeign para consultar dados do Partida Service.

Endpoint:

```http
GET /api/noticias/partida/{id}
```

---

# Comunicação Assíncrona com Kafka

## Evento de Domínio

Evento:

```text
PartidaCriadaEvent
```

Tópico:

```text
partidas.criadas
```

## Fluxo

1. Usuário cadastra uma partida.
2. Partida Service salva a partida no PostgreSQL.
3. Partida Service publica evento no Kafka.
4. Noticia Service consome o evento.
5. Noticia Service cria uma notícia automaticamente.
6. Notícia é salva no MongoDB.

---

# Correlação de Chamadas

O sistema utiliza Correlation ID para rastrear uma operação entre microsserviços.

Exemplo:

```text
[CORRELATION-ID: xxxxx]
Evento enviado para Kafka

[CORRELATION-ID: xxxxx]
Mensagem recebida do Kafka

[CORRELATION-ID: xxxxx]
Notícia criada automaticamente no MongoDB
```

Essa abordagem permite rastrear uma requisição ponta a ponta.

---

# Resiliência

Foi utilizado Resilience4J integrado ao OpenFeign.

Objetivos:

- Evitar falhas em cascata
- Implementar Circuit Breaker
- Disponibilizar fallback para chamadas indisponíveis

Endpoint de teste:

```http
GET /api/noticias/resiliencia/{id}
```

---

# Observabilidade

Foi utilizado Spring Boot Actuator.

Endpoints disponíveis:

```text
/actuator
/actuator/health
/actuator/info
/actuator/metrics
```

Exemplos de métricas monitoradas:

- http.server.requests
- spring.kafka.listener
- spring.kafka.template
- mongodb.driver.commands
- resilience4j.circuitbreaker.calls
- resilience4j.circuitbreaker.state
- hikaricp.connections
- jvm.memory.used
- process.cpu.usage

Exemplo:

```http
GET http://localhost:8081/actuator/metrics/http.server.requests
```

```http
GET http://localhost:8082/actuator/metrics/spring.kafka.listener
```

---

# Logs Centralizados com Papertrail

O projeto tambem esta configurado para enviar logs de todos os microsservicos para o Papertrail usando Logback via HTTPS.

Cada servico possui um arquivo:

```text
src/main/resources/logback-spring.xml
```

A configuracao usa variaveis de ambiente. Se `PAPERTRAIL_URL` e `PAPERTRAIL_TOKEN` estiverem configuradas, os logs sao enviados ao Papertrail. Se nao estiverem, os servicos continuam rodando apenas com logs no console.

## Configurar variaveis de ambiente

No painel do Papertrail, crie ou acesse um log destination e abra a aba `Usage Instructions`. Selecione `HTTPS` e copie:

- `Endpoint`
- `Token`

Exemplo de endpoint:

```text
https://logs.collector.na-01.cloud.solarwinds.com/v1/logs
```

No PowerShell, configure:

```powershell
$env:PAPERTRAIL_URL="https://logs.collector.na-01.cloud.solarwinds.com/v1/logs"
$env:PAPERTRAIL_TOKEN="SEU_TOKEN_DO_PAPERTRAIL"
```

Substitua o token pelo valor real da sua conta. Nao coloque o token fixo no codigo.

## Executar com Papertrail

Configure as variaveis acima no terminal, no IntelliJ ou nas variaveis de ambiente do Windows. Depois execute os servicos normalmente.

Exemplo pelo PowerShell:

```powershell
cd discovery-server
.\mvnw.cmd spring-boot:run
```

Use o mesmo padrao para os outros servicos:

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

```powershell
cd partida-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd noticia-service
.\mvnw.cmd spring-boot:run
```

No IntelliJ, tambem e possivel rodar diretamente as classes `DiscoveryServerApplication`, `ApiGatewayApplication`, `PartidaServiceApplication` e `NoticiaServiceApplication`, desde que as variaveis de ambiente estejam configuradas nas Run Configurations ou no Windows.

## Testar o envio de logs

1. Inicie pelo menos o `discovery-server`.
2. Acesse:

```text
http://localhost:8761
```

3. No Papertrail, pesquise pelo nome do servico:

```text
discovery-server
```

Ao iniciar todos os servicos e testar as rotas do projeto, os logs podem ser filtrados por:

```text
api-gateway
partida-service
noticia-service
discovery-server
```

---

# Programação Reativa

Foi implementado endpoint reativo utilizando Project Reactor.

Endpoint:

```http
GET /api/noticias/reactive
```

Retorno:

```java
Flux<Noticia>
```

Objetivo:

Demonstrar processamento reativo e streaming de dados utilizando Reactor.

---

# Bancos de Dados

## PostgreSQL

Utilizado pelo Partida Service.

Tabela principal:

```text
partidas
```

---

## MongoDB

Utilizado pelo Noticia Service.

Collection:

```text
noticias
```

---

# Testando o Sistema

## 1. Cadastrar Partida

Requisição:

```http
POST http://localhost:8080/api/partidas
```

Body:

```json
{
  "selecaoMandante": "Brasil",
  "selecaoVisitante": "Argentina",
  "golsMandante": 0,
  "golsVisitante": 0,
  "fase": "Grupo",
  "status": "AGENDADA"
}
```

Resposta esperada:

```json
{
  "id": 1,
  "selecaoMandante": "Brasil",
  "selecaoVisitante": "Argentina",
  "golsMandante": 0,
  "golsVisitante": 0,
  "fase": "Grupo",
  "status": "AGENDADA"
}
```

---

## 2. Verificar Notícia Criada Automaticamente

Requisição:

```http
GET http://localhost:8080/api/noticias
```

Resposta esperada:

```json
[
  {
    "titulo": "Nova partida cadastrada",
    "conteudo": "Brasil x Argentina foi cadastrada na fase Grupo"
  }
]
```

---

## 3. Testar Endpoint Reativo

Requisição:

```http
GET http://localhost:8080/api/noticias/reactive
```

Retorna fluxo reativo de notícias utilizando Project Reactor.

---

# Conclusão

O projeto demonstra a aplicação dos principais conceitos de arquitetura de microsserviços:

- Comunicação síncrona com OpenFeign
- Comunicação assíncrona com Kafka
- Descoberta de serviços com Eureka
- Gateway centralizado
- Persistência distribuída
- Resiliência com Resilience4J
- Observabilidade com Spring Actuator
- Correlação de requisições
- Programação Reativa com Project Reactor

Todos os requisitos da segunda etapa do projeto foram implementados e validados através de testes funcionais.
