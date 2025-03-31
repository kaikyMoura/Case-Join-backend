<h2 align="center">Case join - Backend</h2>
<p align="center"><i>Repositório  para o backend/api do desafio proposto pela Join tecnologia.</i></p>

<br/>

### 1. Sobre o projeto
Este repositório contem o back do [Case Join - FrontEnd](https://github.com/kaikyMoura/Case-join-frontend), responsável pelas operações de CRUD. Foi desenvolvido utilizando o Spring Boot 3.3.10 e o Java JDK 21, e, H2 como banco de dados.

Este aplicação segue o padrão REST.

<br/>

### 2. Principais recursos 🔑
- CRUD completo (Create, Read, Update, Delete).
- Banco em memória utilizando o H2.
- Testes unitários com Junit e Spring test.

<br/>

### 3. Technologias & Dependências
<div display="inline-block"> <img alt="java-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" /> <img alt="spring-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" /> </div>

#### Principais Dependências:

- **[springdoc-openapi-starter-webmvc-ui](https://springdoc.org/)**:  
  Facilita a integração do Spring Boot com o Swagger UI para documentação de APIs REST, permitindo a geração automática de documentação OpenAPI para endpoints.

- **[lombok](https://projectlombok.org/)**:  
  Biblioteca para reduzir o código boilerplate em Java, oferecendo anotações que geram automaticamente getters, setters, construtores, equals, hashCode e toString.

- **[junit-jupiter](https://junit.org/junit5/)**:  
  Framework de testes unitários para Java, parte do JUnit 5, que oferece uma plataforma para rodar testes, além de suporte para testes dinâmicos, anotações mais flexíveis e funcionalidades modernas.

- **[jakarta.servlet-api](https://jakarta.ee/specifications/servlet/)**:  
  API que fornece suporte para a criação de servlets e interações com servidores web em Java, com a responsabilidade de gerenciar as requisições HTTP.

- **[jakarta.validation-api](https://jakarta.ee/specifications/bean-validation/)**:  
  API para validação de beans em Java, proporcionando uma maneira de aplicar restrições de validação declarativas (como tamanho, formato, entre outras) nas entidades.

- **[spring-web](https://spring.io/projects/spring-framework)**:  
  Parte do Spring Framework, fornece suporte para criar aplicativos web, incluindo suporte a APIs RESTful, integração com protocolos HTTP e recursos para simplificar a configuração de controladores e filtragem de requisições.

- **[spring-boot-starter-data-jpa](https://spring.io/projects/spring-data-jpa/)**:  
  Simplifica o uso da Java Persistence API (JPA) no Spring Boot, fornecendo configurações, entidades JPA e suporte a repositórios para operações CRUD fáceis.

- **[h2-database](https://www.h2database.com/html/main.html)**:  
  Um banco de dados relacional em memória usado para desenvolvimento e testes, com um console web para gerenciamento de banco de dados.

- **[lombok](https://projectlombok.org)**:  
  Reduz o código boilerplate usando anotações para gerar getters, setters, construtores e mais durante o tempo de compilação.
<br/>

### 4. Arquitetura

O projeto segue uma arquitetura REST, com uma clara separação de modelos, serviços e controladores.

⚙️ **Fluxo de Criação de Conta & Autenticação:**
- Para criar um produto, envie uma requisição POST para **/product** com o produto no corpo [consulte a documentação do Swagger para entender melhor].
```json
{
  "name": "string",
  "description": "string",
  "brand": "string",
  "category": "TECHNOLOGY",
  "quantity": 0,
  "price": 0,
}
```
  
- Por padrão, novos produtos têm IDs atribuídos automaticamente usando UUID.
- O classe Product tem os seguintes campos:
```js
    private UUID id;
    private String name;
    private String description;
    private String brand;
    private Category category; (Enum: [ TECHNOLOGY, CLOTHING, FOOD, FURNITURE, TOYS, BOOKS, GAMES, ELECTRONICS, JEWELERY ])
    private Integer quantity;
    private Double price;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

<br/>

### 5. Instalação e Configuração

#### Pré-requisitos:
Antes de rodar o projeto, certifique-se de que o Java está instalado na sua máquina. Se não estiver, você pode baixá-lo no [site oficial da Oracle](https://www.oracle.com/java/technologies/downloads) (versão 17 ou superior é recomendada).
<br/>Para verificar a instalação do Java, execute:

```console
java --version
```

#### Clone o repositório para sua máquina local:

```console
git clone https://github.com/kaikyMoura/Case-Join-backend.git
```

Navegue até o diretório raiz do projeto:

```console
cd Case-Join-backend
```

#### Construindo o Projeto
Use o Maven para limpar e empacotar a aplicação:

```console
mvn clean package
```

#### Rodando a Aplicação
Após a construção ser concluída, você pode iniciar a aplicação com:

```console
java -jar target/product-inventory-0.0.1-SNAPSHOT
```

Ou você pode rodar diretamente na sua IDE.

#### A API estará disponível em:

```console
http://localhost:8080/api/v1/product
```

<br/>

### 6. Executando os testes

Para executar todos os testes do projeto, utilize o seguinte comando:

```console
mvn test
```


### 7. Documentação 
Os endpoints disponiveis são:

| Método   | Endpoint        | Descrição                                              | Parâmetros                           |
| --- | --- | --- | --- |
| **POST** | `/product`      | Cria um novo produto                                   | `ProductDto` (no corpo da requisição)|
| **GET**  | `/product`      | Retorna uma lista de produtos (com paginação opcional) | `name`, `category`, `brand`, `minPrice`, `maxPrice`, `page`, `pageSize` |
| **GET**  | `/product/{id}` | Retorna um produto pelo ID                              | `id` (path variável)                 |

<br/>


### Author 👨‍💻 
[Kaiky](https://github.com/kaikyMoura) - Desenvolvedor
