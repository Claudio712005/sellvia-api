# Sellvia

API REST backend para gerenciamento de catalogos e produtos, desenvolvida com Kotlin e Spring Boot. O sistema contempla autenticacao, administracao de empresas, gerenciamento de produtos e categorias, e armazenamento de ativos digitais via Cloudflare R2.

---

## Sumario

- [Visao Geral](#visao-geral)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Pre-requisitos](#pre-requisitos)
- [Variaveis de Ambiente](#variaveis-de-ambiente)
- [Executando a Aplicacao](#executando-a-aplicacao)
- [Referencia da API](#referencia-da-api)
- [Autenticacao](#autenticacao)
- [Migracoes de Banco de Dados](#migracoes-de-banco-de-dados)
- [Testes](#testes)
- [Licenca](#licenca)

---

## Visao Geral

Sellvia e uma API backend multi-tenant que permite que empresas gerenciem seus catalogos de produtos, incluindo itens fisicos e digitais. Cada empresa opera em um contexto de dados isolado, e os usuarios recebem perfis que definem seu nivel de acesso dentro do sistema.

Principais funcionalidades:

- Cadastro e gerenciamento de perfil de empresas com upload de logotipo
- CRUD de produtos e categorias com gerenciamento de imagens via Cloudflare R2
- Geracao de catalogo em PDF por empresa
- Autenticacao baseada em JWT com rotacao de access e refresh token
- Controle de acesso por perfil com tres niveis de permissao distintos

---

## Arquitetura

O projeto segue uma arquitetura em camadas com separacao clara de responsabilidades:

```
src/main/kotlin/br/com/claus/sellvia/
├── web/
│   └── controller/        # Controladores REST (pontos de entrada)
├── application/
│   ├── usecase/           # Logica de negocio por funcionalidade
│   ├── dto/               # Objetos de transferencia de dados (requisicao e resposta)
│   └── mapper/            # Conversoes entre dominio e DTO
├── domain/
│   ├── model/             # Entidades de dominio
│   ├── enums/             # Enumeracoes de dominio
│   ├── exception/         # Excecoes customizadas de dominio
│   └── pagination/        # Abstracoes de paginacao
└── infrastructure/
    ├── config/            # Beans de configuracao do Spring e bibliotecas externas
    ├── security/          # Filtros JWT e configuracao do Spring Security
    ├── persistence/       # Repositorios JPA e adaptadores de persistencia
    └── adapter/           # Adaptadores para servicos externos (R2, PDF)
```

Os modelos de dominio herdam de `SaleableItem`, uma classe base que centraliza campos comuns como nome, descricao, precificacao, status e timestamps de auditoria.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 1.9.25 |
| Runtime | Java 21 (Eclipse Temurin) |
| Framework | Spring Boot 3.3.4 |
| Build | Gradle 8 com Kotlin DSL |
| Banco de Dados | PostgreSQL 14+ |
| Migracoes | Flyway 11.3.2 |
| ORM | Spring Data JPA / Hibernate |
| Seguranca | Spring Security + Java-JWT 4.5.0 |
| Armazenamento | Cloudflare R2 via AWS S3 SDK 2.27.0 |
| Processamento de Imagens | Scrimage 4.1.3 (suporte a WebP) |
| Geracao de PDF | Flying Saucer 9.1.22 |
| Testes | JUnit 5, MockK 1.14.9, Kotest |
| Estilo de Codigo | ktlint |
| Cobertura | JaCoCo |
| Conteinerizacao | Docker / Docker Compose |

---

## Pre-requisitos

- Docker e Docker Compose
- Java 21 (para desenvolvimento local sem Docker)
- PostgreSQL 14+ (caso execute fora do Docker)
- Um bucket Cloudflare R2 configurado com acesso publico

---

## Variaveis de Ambiente

A aplicacao e configurada exclusivamente por variaveis de ambiente. Nenhum segredo e versionado no repositorio.

### Aplicacao

| Variavel | Descricao | Exemplo |
|---|---|---|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_PORT` | Porta do PostgreSQL | `5432` |
| `DB_NAME` | Nome do banco de dados | `sellvia` |
| `DB_USERNAME` | Usuario da aplicacao no banco | `app_user` |
| `DB_PASSWORD` | Senha do usuario da aplicacao | — |
| `MAIN_APP_SCHEMA` | Schema do PostgreSQL | `sellvia` |
| `FLYWAY_USER` | Usuario do Flyway para migracoes | `flyway_user` |
| `FLYWAY_PASSWORD` | Senha do usuario do Flyway | — |

### Seguranca

| Variavel | Descricao |
|---|---|
| `JWT_SECRET` | Chave secreta utilizada para assinar os tokens JWT |
| `JWT_ACCESS_EXPIRATION` | TTL do access token em milissegundos |
| `JWT_REFRESH_EXPIRATION` | TTL do refresh token em milissegundos |

### Cloudflare R2

| Variavel | Descricao |
|---|---|
| `R2_ACCOUNT_ID` | ID da conta Cloudflare |
| `R2_ACCESS_KEY` | Chave de acesso do R2 |
| `R2_SECRET_KEY` | Chave secreta do R2 |
| `R2_BUCKET_NAME` | Nome do bucket de destino |
| `R2_PUBLIC_URL` | URL publica base do bucket |

### Servico de Imagens

| Variavel | Descricao |
|---|---|
| `IMAGE_SERVICE_URL` | URL base para servir as imagens enviadas |

---

## Executando a Aplicacao

### Com Docker Compose

```bash
# Copie o arquivo de exemplo e preencha as variaveis
cp .env.example .env

# Construa e inicie todos os servicos
docker compose up --build
```

A API estara disponivel em `http://localhost:8080`.

### Desenvolvimento Local

```bash
# Certifique-se de que uma instancia do PostgreSQL esta acessivel e as variaveis de ambiente estao definidas
./gradlew bootRun
```

### Gerando o JAR

```bash
./gradlew build
java -jar build/libs/sellvia-*.jar
```

---

## Referencia da API

Todos os endpoints possuem o prefixo `/api/v1.0.0`.

O Swagger UI esta disponivel em `/swagger-ui.html` (publico, sem necessidade de autenticacao em perfis que nao sejam de producao).

### Autenticacao

| Metodo | Caminho | Descricao | Acesso |
|---|---|---|---|
| POST | `/auth/login` | Autentica o usuario e retorna os tokens | Publico |
| POST | `/auth/registry` | Registra um novo usuario | Publico |
| POST | `/auth/refresh-token` | Rotaciona o access token usando o refresh token | Publico |

### Empresas

| Metodo | Caminho | Descricao | Acesso |
|---|---|---|---|
| GET | `/companies` | Lista todas as empresas | SYSTEM_ADMIN |
| GET | `/companies/{id}` | Busca empresa por ID | SYSTEM_ADMIN, COMPANY_ADMIN |
| POST | `/companies` | Cria uma nova empresa | SYSTEM_ADMIN |
| PUT | `/companies/{id}` | Atualiza dados da empresa | SYSTEM_ADMIN, COMPANY_ADMIN |
| PUT | `/companies/{id}/update-image` | Envia ou substitui o logotipo da empresa | SYSTEM_ADMIN, COMPANY_ADMIN |
| POST | `/companies/{id}/catalog/pdf` | Gera o catalogo em PDF da empresa | COMPANY_ADMIN, COMPANY_USER |

### Produtos

| Metodo | Caminho | Descricao | Acesso |
|---|---|---|---|
| GET | `/products` | Lista produtos (paginado) | Autenticado |
| GET | `/products/{id}` | Busca produto por ID | Autenticado |
| POST | `/products` | Cria um produto | COMPANY_ADMIN |
| PUT | `/products/{id}` | Atualiza dados do produto | COMPANY_ADMIN |
| DELETE | `/products/{id}` | Remove um produto | COMPANY_ADMIN |
| PUT | `/products/{id}/update-image` | Envia ou substitui a imagem do produto | COMPANY_ADMIN |

### Categorias

| Metodo | Caminho | Descricao | Acesso |
|---|---|---|---|
| GET | `/categories` | Lista categorias (paginado) | Autenticado |
| POST | `/categories` | Cria uma categoria | COMPANY_ADMIN |
| PUT | `/categories/{id}` | Atualiza uma categoria | COMPANY_ADMIN |
| DELETE | `/categories/{id}` | Remove uma categoria | COMPANY_ADMIN |

### Usuarios

| Metodo | Caminho | Descricao | Acesso |
|---|---|---|---|
| GET | `/users` | Lista usuarios | SYSTEM_ADMIN, COMPANY_ADMIN |
| GET | `/users/{id}` | Busca usuario por ID | SYSTEM_ADMIN, COMPANY_ADMIN |
| PUT | `/users/{id}` | Atualiza dados do usuario | SYSTEM_ADMIN, COMPANY_ADMIN |

---

## Autenticacao

A API utiliza JWT com estrategia de dois tokens:

- **Access token** - vida util curta, enviado no cabecalho `Authorization: Bearer <token>` em todas as requisicoes protegidas
- **Refresh token** - vida util longa, utilizado exclusivamente em `POST /auth/refresh-token` para obter um novo access token sem exigir nova autenticacao

Os valores de expiracao dos tokens sao configurados pelas variaveis de ambiente `JWT_ACCESS_EXPIRATION` e `JWT_REFRESH_EXPIRATION`.

### Perfis de Usuario

| Perfil | Descricao |
|---|---|
| `SYSTEM_ADMIN` | Acesso total a plataforma, gerencia empresas e recursos globais |
| `COMPANY_ADMIN` | Gerencia todos os recursos dentro de sua propria empresa |
| `COMPANY_USER` | Acesso somente leitura ao catalogo e dados de produtos |

---

## Migracoes de Banco de Dados

O Flyway gerencia todas as alteracoes de schema. As migracoes estao localizadas em:

```
src/main/resources/db/migration/
```

As migracoes sao aplicadas automaticamente na inicializacao, utilizando um usuario dedicado do Flyway (`FLYWAY_USER`) com privilegios elevados, separado do usuario de runtime da aplicacao (`DB_USERNAME`).

Convencao de nomenclatura: `V{versao}__{descricao}.sql`

---

## Testes

```bash
# Executar todos os testes
./gradlew test

# Executar testes com relatorio de cobertura
./gradlew test jacocoTestReport
```

Os relatorios de cobertura sao gerados em `build/reports/jacoco/test/html/index.html`.

Uma colecao Postman cobrindo todos os endpoints esta incluida na raiz do repositorio: `sellvia_api.postman_collection.json`.

---

## Licenca

Este projeto e software proprietario. Consulte o arquivo [LICENSE](LICENSE) para os termos completos.

A visualizacao do codigo-fonte e permitida. Uso comercial, modificacao, redistribuicao e sublicenciamento sao estritamente proibidos.
