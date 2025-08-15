# Test Dashboard Service

This service accepts automated test results (JUnit XML or simple JSON), writes roll-up metrics to InfluxDB, and lets you see them in Grafana.

Below is a super-basic, step-by-step guide to get everything running locally and send data to it.

---

## What you need
- Docker and Docker Compose
- Java 21
- curl (for sending example requests)

## 1) Start the data stack (InfluxDB + Grafana)
From the project root (this folder):

```
docker compose up -d
```

This will start:
- InfluxDB on http://localhost:8086
- Grafana on http://localhost:3000 (user: admin, password: admin_password)

You can stop everything later with `docker compose down`.

## 2) Run the app
In a separate terminal:

```
cd grs
./gradlew run
```

The first run downloads dependencies and can take a minute. The app listens on http://localhost:8080.

## 3) Check it’s up

- Health text:
  ```
  curl http://localhost:8080/report/
  ```
  Expected: `Test Dashboard Service - OK`

- Health JSON:
  ```
  curl http://localhost:8080/report/health
  ```
  Expected: `{ "status": "UP" }`

## 4) Send test data (two options)

Option A: JSON (simple totals)
```
curl -X POST http://localhost:8080/report/json \
  -H 'Content-Type: application/json' \
  -d '{
    "framework":"cypress",
    "suite":"login.spec",
    "total":10,
    "passed":9,
    "failed":1,
    "skipped":0,
    "durationMs":12345
  }'
```

Option B: JUnit XML (one testsuite or multiple testsuites)
```
curl -X POST http://localhost:8080/report/junit \
  -H 'Content-Type: application/xml' \
  --data-binary @path/to/junit-report.xml
```

Tip: To try quickly, create a small file called `sample-junit.xml` and paste this content:
```
<testsuite name="ExampleSuite" tests="3" failures="1" errors="0" skipped="1" time="1.5" timestamp="2025-08-15T10:00:00Z">
  <testcase name="ok" time="0.5"/>
  <testcase name="skip"><skipped/></testcase>
  <testcase name="fail"><failure>boom</failure></testcase>
</testsuite>
```
Then run:
```
curl -X POST http://localhost:8080/report/junit \
  -H 'Content-Type: application/xml' \
  --data-binary @sample-junit.xml
```

If these requests return 200 OK, the app has also written a data point to InfluxDB.

## 5) See the data in Grafana (very basic)

1. Open http://localhost:3000 in your browser.
2. Log in with:
   - Username: `admin`
   - Password: `admin_password`
3. Add a data source:
   - Click “Connections” (or the gear icon -> Data sources) -> “Add new data source”.
   - Choose “InfluxDB”.
   - Query Language: InfluxQL
   - URL: `http://localhost:8086`
   - Database: `telegraf`
   - User: `telegraf`
   - Password: `telegraf_password`
   - Click “Save & Test” (you should see a success message).
4. Import the sample dashboard:
   - In Grafana, go to Dashboards -> New -> Import.
   - Click “Upload JSON file” and choose `grafana/test-dashboard.json` from this repository.
   - When prompted, select the InfluxDB data source you just created.
   - Click “Import”.
5. You should now see tiles with totals and pass/fail counts. If it looks empty, send the JSON or JUnit example again and refresh the dashboard.

## Troubleshooting
- If `./gradlew run` fails, ensure you have Java 21 installed and on your PATH.
- If Grafana login fails, make sure the `docker compose up -d` step completed and you’re visiting http://localhost:3000.
- If “Save & Test” for the Grafana data source fails, confirm InfluxDB is running (http://localhost:8086) and that you used:
  - Database: `telegraf`
  - User: `telegraf`
  - Password: `telegraf_password`
- To reset the stack, run:
  - `docker compose down -v` (removes volumes), then
  - `docker compose up -d` again.

## Endpoints summary
- GET /report/ -> text health: "Test Dashboard Service - OK"
- GET /report/health -> JSON health: {"status":"UP"}
- POST /report/json (application/json) -> accepts TestRun JSON
- POST /report/junit (application/xml) -> accepts JUnit XML

That’s it! You can now run the service, send test results, and view them in Grafana.
