# Area

Project of 7 weeks whose goal is to create an app that allows actions from services to trigger reactions.

The chosen stack is **Java Spring Boot** (REST API + PostgreSQL) for the backend, with a **React** web client and a **React Native** mobile client.

## Usage

To run the project, go into the `SpringBoot` folder and run the following command, which launches the database, the server, the web and the mobile app:

```
docker compose up --build
```

To stop the project:

```
docker compose down
```

The server runs on port 8080, the web client on port 8081.

## Structure

- `SpringBoot/` — the project: Spring Boot backend, React web client and React Native app, with its [technical documentation](./SpringBoot).
- `PoC/` — the proofs of concept written while choosing the stack.
