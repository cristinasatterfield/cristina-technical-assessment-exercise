# Gaggle Technical Assessment Exercise

This is a CRUD REST API for contacts written in Java with Spring Boot.

## Running the Application Natively

---

### 1. Install Dependencies

This has been designed and tested on Linux-based systems. It may work on Windows, but it is currently untested.

-   Install Java 16
-   Install [Postgres](https://www.postgresql.org/download/)

### 2. Start a Postgres server

Start up an instance of the Postgres server. This is what the application connects to to store contact data.

### 3. Set the Database Configuration

Database configurations are stored in the `flyway.properties` and `src/main/resources/applicaion.properties` files. Change these values to reflect your database configuration.

`src/main/resources/application.properties`

```
spring.datasource.url=jdbc:postgresql://postgres:5432/testdb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

`flyway.properties`

```
flyway.schemas=app-db
flyway.url=jdbc:postgresql://postgres:5432/testdb
flyway.locations=filesystem:db/migration
flyway.username=postgres
flyway.password=postgres
```

### 4. Start the Application Server

Run the following command to start the application server. You can connect to it on `http://localhost:8080`

```bash
./mvnw spring-boot:run
```

## Running the Application in Docker

---

### 1. Install Dependencies

-   Install [Docker](https://docs.docker.com/get-docker/)

### 2. Start the Server

You can run this directly with Docker or with provided Docker Compose file. It is recommended to use Docker Compose as it also starts a Postgres container that is correctly linked to the application server.

You can start the server using Docker compose with the following command.

```bash
docker-compose up --build
```

## Testing

---

You can run the tests with the following command.

The integration tests use TestContainers to standup a temporary Postgres container, so you will need to have Docker installed on your machine to run them.

```bash
./mvnw test
```

## Documentation

---

API documentation is hosted in the application server. After starting the server, you can read the API documentation [here](http://localhost:8080/index.html).

This API documentation is generated using OpenAPI and rendered using [ReDoc](https://github.com/Redocly/redoc).

## Using the Service

---

Running locally or with the Docker Compose configuration, the API server will be hosted at http://localhost:8080/api/v1/contact.

You can use any HTTP client to connect to the API, but it is recommended to use Postman.
