# 👤 User Management System - JavaFX CRUD

Sistema de gerenciamento de usuários desenvolvido para aplicar e consolidar os conhecimentos adquiridos durante o **2º semestre de Análise e Desenvolvimento de Sistemas (ADS) na Fatec**, especificamente nas disciplinas de **Linguagem de Programação** e **Sistemas da Informação**, também reaplicando desafios práticos do desenovlvimento do projeto [API do semestre](https://github.com/DanielDPereira/API-2).

O projeto foca na implementação de uma arquitetura robusta, segurança de dados e uma interface de usuário intuitiva.

## 🛠️ Tecnologias e Aplicações

O sistema utiliza tecnologias modernas do ecossistema Java para garantir escalabilidade e segurança:

* **Java 21 & JavaFX:** Utilização de Programação Orientada a Objetos (POO) avançada, controle de interfaces com arquivos FXML e estilização via CSS.
* **MySQL:** Implementação de banco de dados relacional com integridade referencial.
* **JDBC:** Gerenciamento de persistência de dados através do padrão **DAO (Data Access Object)**.
* **BCrypt:** Aplicação de criptografia *hashing* para armazenamento seguro de senhas.
* **Dotenv:** Gerenciamento de variáveis de ambiente para proteção de credenciais sensíveis (DB_URL, USER, PASS).
* **Maven:** Gestão de dependências e automação do build do projeto.

## 📊 Aplicação de Conhecimentos (Fatec)

### Sistemas da Informação

Aplicação da modelagem de dados relacional (DER) para garantir a consistência das informações. O sistema conta com:

* **Controle de Acesso (RBAC):** Diferenciação de permissões entre usuários `ADMIN` e `COMUM`.
* **Trilha de Auditoria:** Implementação de uma tabela de logs para registrar cada ação realizada no sistema (quem, o quê e quando), um conceito fundamental em sistemas de informação empresariais.

### Linguagem de Programação

* **Interface Dinâmica:** Uso de `TableView` com filtragem em tempo real e paginação de dados para otimização de performance.
* **Tratamento de Exceções:** Robustez no gerenciamento de conexões e erros de SQL.
* **Modularidade:** Organização do código seguindo o padrão **MVC (Model-View-Controller)** para separação de responsabilidades.

## 📋 Funcionalidades CRUD

O sistema gerencia o ciclo de vida completo do usuário:

1. **Create:** Cadastro de novos usuários com validação de campos e criptografia automática de senha.
2. **Read:** Listagem dinâmica com busca por nome/email e visualização de logs de atividade.
3. **Update:** Edição de dados existentes, permitindo a atualização seletiva da senha.
4. **Delete:** Exclusão lógica ou física de registros diretamente pela interface.

## 🚀 Como Executar o Projeto

### Pré-requisitos

* JDK 21 ou superior.
* Maven instalado.
* MySQL Server ativo.

### Passo a Passo

1. **Configurar Banco de Dados:** Execute o script presente em `BD.sql` no seu servidor MySQL para criar as tabelas necessárias.
2. **Variáveis de Ambiente:** Renomeie o arquivo `.env.exemple` para `.env` e preencha com suas credenciais locais:
```env
DB_URL=jdbc:mysql://localhost:3306/user_management_db
DB_USER=seu_usuario
DB_PASSWORD=sua_senha

```


3. **Build e Execução:**
Abra o terminal na raiz do projeto e execute:
```bash
mvn clean compile javafx:run

```



---

<div align="center">
<p>Desenvolvido por <b><a href="https://danieldpereira.github.io/danieldias.py/">Daniel Dias Pereira</a></b></p>
</div>
