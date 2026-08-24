# 👤 Microsserviço de Usuários

Microsserviço responsável pelo **gerenciamento de usuários e autenticação** da aplicação de agendamento.

Este serviço faz parte de uma arquitetura baseada em **microsserviços**, sendo consumido principalmente pelo **BFF Agendador**, que centraliza a comunicação entre o cliente e os serviços da aplicação.

## 🛠️ Tecnologias

* ☕ Java
* 🌱 Spring Boot
* 🔐 Spring Security
* 🔑 JWT
* 🗄️ PostgreSQL
* 📦 Gradle
* 🧩 Lombok
* 📄 DTOs
* 📚 Swagger / OpenAPI
* 🐳 Docker
* 📊 SonarQube

## 📌 Responsabilidades

* Cadastro de usuários
* Consulta de usuários
* Atualização de usuários
* Exclusão de usuários
* Autenticação
* Geração e validação de JWT
* Gerenciamento de dados relacionados aos usuários

## 🏗️ Arquitetura

O microsserviço faz parte do seguinte fluxo:

```text
                    ┌─────────────────┐
                    │     Cliente     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  BFF Agendador  │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Microsserviço   │
                    │    Usuários     │
                    └─────────────────┘
```

O cliente não precisa acessar diretamente este microsserviço. O **BFF Agendador** funciona como ponto de entrada da aplicação.

## 🐳 Docker

O serviço pode ser executado individualmente ou em conjunto com os demais microsserviços através do Docker Compose.

## 📖 Documentação

A documentação completa da arquitetura, integração entre os serviços e instruções para execução pode ser encontrada no projeto principal:

👉 **[BFF Agendador](https://github.com/cardosogoc/bff-agendador)**

## 🔗 Projeto

* **BFF Agendador:** https://github.com/cardosogoc/bff-agendador
* **Este microsserviço:** https://github.com/cardosogoc/usuario_recap

## 👨‍💻 Autor

**Gabriel Cardoso**

Projeto desenvolvido para estudos e evolução prática em **desenvolvimento Backend Java e Spring Boot**.
