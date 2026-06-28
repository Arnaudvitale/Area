# Spring Boot stack documentation
The Spring Boot stack uses Java, Spring Boot, Spring Data JPA and PostgreSQL.

Below are the advantages and disadvantages of this stack.

## Advantages

- Strongly typed language (Java), easier to maintain on a 7 weeks project with several developers
- Spring Boot ships with everything needed for a REST API (web, validation, security, data access)
- Spring Data JPA removes most of the SQL boilerplate while keeping a real relational database
- PostgreSQL is robust, free and well supported
- Large ecosystem and documentation, easy to dockerize

## Disadvantages

- Heavier to start than Node (JVM, build time)
- Java is more verbose than JavaScript
- A second technology is still required for the mobile app and the web client (React / React Native)

## How to use

Before starting, make sure the following are installed:
- **Java 17+**
- **Maven** (or use the Docker image which bundles it)
- **PostgreSQL** (or the docker-compose `db` service)

Build and run the API:

```
mvn spring-boot:run
```

Or with Docker (from the stack folder):

```
docker compose up --build
```

The server starts on port **8080**. A quick check:

```
curl http://localhost:8080/about.json
```

The full implementation lives in the [SpringBoot](../../SpringBoot) folder.
