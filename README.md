# Time Sheet

## ✨ Visão Geral
O projeto **Time Sheet** ⏰ é uma aplicação web desenvolvida em **Java com Spring Boot** para o gerenciamento de folhas de ponto. O sistema permite que funcionários registrem suas horas trabalhadas, gerentes aprovem os registros e administradores gerenciem os acessos.

## 💻 Tecnologias Utilizadas
- **Java 21** ☕
- **Spring Boot** ✨
- **Spring Security** 🔒
- **JPA/Hibernate** 🏢
- **PostgreSQL** 📂 (banco principal)
- **H2 Database** 📓 (banco de testes)
- **Thymeleaf** 🎨
- **Docker** 🛠️
- **OpenAPI (Swagger)** 📄

## ⚙️ Funcionalidades
- ✅ Cadastro de usuários e autenticação com JWT.
- 🕒 Registro de horas trabalhadas pelos funcionários.
- 📍 Aprovação de registros pelos gerentes.
- 🛡️ Controle de permissões por papel (Role-based access control).
- 📊 Integração com banco de dados PostgreSQL.
- ⏳ Atualização automática de timesheets às **23:00h**, baseada na entrada e na falta de marcação de saída.

## 📁 Estrutura do Projeto
O sistema está organizado da seguinte maneira:
```
time-sheet/
│── src/
│   ├── main/
│   │   ├── java/io/github/samuelsantos20/time_sheet/
│   │   │   ├── controller/  # Controladores da API
│   │   │   ├── model/        # Modelos de dados (Entidades)
│   │   │   ├── repository/   # Repositórios para acesso ao banco
│   │   │   ├── service/      # Regras de negócio
│   │   │   ├── config/       # Configurações do sistema
│   ├── resources/
│   │   ├── application.properties  # Configurações da aplicação
│── docker-compose.yml  # Configuração para executar com Docker
│── README.md
```

## 🗃️ Diagrama de Classes
Abaixo está o diagrama de classes representando as principais entidades do sistema:
![diagram-4617229342373617992](https://github.com/user-attachments/assets/4ae1dcd6-b218-4cae-ba95-fcab1cb8afc0)



## 💡 Como Executar
1. **💾 Clonar o repositório:**
   ```sh
   git clone https://github.com/seu-usuario/time-sheet.git
   cd time-sheet
   ```
2. **🔧 Configurar o banco de dados PostgreSQL**
   - Criar um banco de dados chamado `time_sheet`
   - Atualizar `application.properties` com as credenciais corretas
3. **💡 Executar a aplicação**
   ```sh
   mvn spring-boot:run
   ```
   Ou via Docker:
   ```sh
   docker-compose up
   ```
4. **🔗 Acessar a API**
   - Documentação OpenAPI: `http://localhost:8080/swagger-ui.html`
   - Login na aplicação e registrar horas.

## 🛠️ Contribuição
Se você deseja contribuir com o projeto, fique à vontade para abrir um PR ou relatar issues.

## 👨‍💻 Autor
Desenvolvido por **Samuel Santos**.

---
💛 Feito com amor e Java! ☕

