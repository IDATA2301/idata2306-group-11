# RoamRoute — Backend

Spring Boot 4 REST API for the RoamRoute trip-booking app. Companion to the React frontend at https://github.com/IDATA2301/idata2301-group11.

## Stack

- Java 21, Spring Boot 4.0.2 (Maven)
- Spring Web, Spring Data JPA, Spring Security
- MySQL 8 (via mysql-connector-j)
- JWT auth (jsonwebtoken)

## Prerequisites

- JDK 21
- MySQL 8 running locally (or reachable) with a database named `roamroute`
- The bundled Maven wrapper (`./mvnw`) — no global Maven install required

## Database setup

1. Start MySQL.
2. Create the database and seed it:

   ```bash
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS roamroute;"
   mysql -u root -p roamroute < database.sql
   ```

   `database.sql` resets and reseeds destinations, flights, trips, etc. It's safe to re-run.

## Environment variables

Spring loads `backend/.env` via `spring.config.import` in `application.properties`.

```bash
cd backend
cp .env.example .env
```

Then edit `.env`:

| Variable | Required? | Default |
|---|---|---|
| `DB_PASSWORD` | yes | — |
| `APP_JWT_SECRET` | yes | — (use a long random string, 32+ chars) |
| `DB_URL` | no | `jdbc:mysql://localhost:3306/roamroute` |
| `DB_USERNAME` | no | `root` |
| `APP_CORS_ALLOWED_ORIGINS` | no | `http://localhost:5173,http://127.0.0.1:5173` |
| `APP_JWT_EXPIRATION_MS` | no | `86400000` (24h) |

## Running

```bash
cd backend
./mvnw spring-boot:run
```

The server listens on `http://localhost:8080`. All controllers are mounted under `/api/*` — e.g. `GET http://localhost:8080/api/destinations`.

JPA is configured with `spring.jpa.hibernate.ddl-auto=update`, so the schema in `database.sql` will be reconciled at startup. SQL statements are logged (`spring.jpa.show-sql=true`).

### Other useful commands

```bash
./mvnw test           # run tests
./mvnw clean package  # build a runnable jar in target/
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## How the frontend connects

The Vite dev server proxies `/api/*` to whatever `VITE_API_TARGET` points at — by default the NTNU box, or `http://localhost:8080` when running locally. CORS is set up to allow `http://localhost:5173` out of the box.

## Project layout

```
backend/
  pom.xml
  .env.example
  src/main/
    java/com/roamroute/backend/
      controller/   REST endpoints
      service/      business logic
      repository/   Spring Data JPA repos
      entity/       JPA entities
      config/       security, CORS, JWT
    resources/
      application.properties
database.sql        schema + seed data (run against the roamroute DB)
```

## Image storage

Trip and destination images are served by nginx on the NTNU server at `/images/trip/<filename>` and `/images/destination/<filename>` — they're not part of this backend. The `image_url` columns store bare filenames (e.g. `barcelona.webp`); the frontend builds the full URL via its image helpers. When running fully locally, put matching files into the frontend's `public/images/destination/` and `public/images/trip/` folders so the dev server can serve them.
