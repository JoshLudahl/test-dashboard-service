# Project Guidelines

Project: test-dashboard-service
Updated: 2025-08-15 13:39 local

Overview
- This service ingests automated test reports (JUnit, Playwright, Cypress, XCUITest, etc.), converts them into a normalized TestRun model, and writes roll-up metrics to InfluxDB.
- A Grafana dashboard (grafana/test-dashboard.json) can be imported to visualize totals and pass/fail counts per day.
- A docker-compose.yml is provided to spin up InfluxDB, Telegraf, Grafana, and Kafka locally. The Micronaut app runs separately (Gradle run or your IDE).

Project structure
- grs/ — Micronaut 4 project (Java 21)
  - src/main/java/com/softklass/grs
    - Application.java — Micronaut bootstrap and OpenAPI metadata
    - controller/
      - ReportController.java — REST endpoints under /report
    - model/
      - TestRun.java, TestCaseResult.java — report domain model
    - service/
      - InfluxService.java — writes line protocol to InfluxDB
    - util/
      - JunitXmlParser.java — parses JUnit XML into TestRun
  - src/main/resources/application.properties — Micronaut and telemetry config
  - src/test/java/com/softklass/grs — unit and controller tests
- grafana/test-dashboard.json — sample dashboard
- docker-compose.yml — InfluxDB, Telegraf, Grafana, Kafka stack
- telegraf.conf — sends host metrics to InfluxDB (optional for this app)
- README.md — quick start and usage

Endpoints (ReportController)
- GET /report/ — simple health text: "Test Dashboard Service - OK"
- GET /report/health — JSON {"status":"UP"}
- POST /report/json (application/json) — body is TestRun
- POST /report/junit (text/xml or application/xml) — body is a JUnit XML testsuite(s)

Running locally
- Prereq: Java 21, Gradle wrapper in grs/ folder.
- Start data stack: docker compose up -d (from repo root)
- Run app: cd grs && ./gradlew run
- Open Swagger UI (if enabled by Micronaut config) at /swagger-ui/; otherwise use curl to post.

Posting data (examples)
- JSON:
  curl -X POST http://localhost:8080/report/json \
    -H 'Content-Type: application/json' \
    -d '{"framework":"cypress","suite":"login.spec","total":10,"passed":9,"failed":1,"skipped":0,"durationMs":12345}'
- JUnit XML:
  curl -X POST http://localhost:8080/report/junit \
    -H 'Content-Type: application/xml' \
    --data-binary @path/to/junit-report.xml

Grafana
- Visit http://localhost:3000 (admin/admin_password by default per docker-compose).
- Add an InfluxDB datasource pointing to http://influxdb:8086 (inside Docker network) or http://localhost:8086 if running locally.
- Import grafana/test-dashboard.json and select the InfluxDB datasource.

Tests
- Use the run_test tool paths or Gradle JUnit 5 in grs/ module.
- Run all tests: cd grs && ./gradlew test
- Key tests: GrsTest, ReportControllerTest, JunitXmlParserTest.

Build
- Build shadow JAR: cd grs && ./gradlew shadowJar
- Native-image and Docker tasks are wired through Micronaut Gradle plugin; ensure Java 21.

Code style
- Java 21, Micronaut conventions.
- Favor Serdeable DTOs for Micronaut JSON.
- Keep controllers thin; parsing and persistence in util/service layers.

Tooling notes for Junie
- Prefer functions.run_test to execute tests; project is multi-module-like but only grs is the app module.
- When modifying application.properties, avoid breaking Micronaut static resources configuration used for Swagger/UI.
- Minimal changes principle: prefer adding small, focused files or methods to meet acceptance criteria.
