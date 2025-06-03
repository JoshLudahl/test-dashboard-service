## Micronaut 4.7.4 Documentation

- [User Guide](https://docs.micronaut.io/4.7.4/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.7.4/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.7.4/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

## Feature tracing-opentelemetry-exporter-zipkin documentation

- [Micronaut OpenTelemetry Exporter Zipkin documentation](http://localhost/micronaut-tracing/guide/index.html#opentelemetry)

- [https://opentelemetry.io](https://opentelemetry.io)

## Feature swagger-ui documentation

- [Micronaut Swagger UI documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)

## Feature tracing-opentelemetry-http documentation

- [Micronaut OpenTelemetry HTTP documentation](http://localhost/micronaut-tracing/guide/index.html#opentelemetry)

## Feature tracing-opentelemetry-zipkin documentation

- [Micronaut OpenTelemetry Zipkin documentation](https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry)

- [https://opentelemetry.io](https://opentelemetry.io)

## Feature http-poja documentation

- [Micronaut Plain Old Java HTTP Application documentation](https://micronaut-projects.github.io/micronaut-servlet/latest/guide/index.html#httpPoja)

## Feature management documentation

- [Micronaut Management documentation](https://docs.micronaut.io/latest/guide/index.html#management)

## Feature junit-platform-suite-engine documentation

- [https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine-setup](https://junit.org/junit5/docs/current/user-guide/#junit-platform-suite-engine-setup)

## Feature gitlab-workflow-ci documentation

- [https://docs.gitlab.com/ee/ci/](https://docs.gitlab.com/ee/ci/)

## Feature tracing-opentelemetry-annotations documentation

- [Micronaut OpenTelemetry Annotations documentation](https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry)

- [https://opentelemetry.io](https://opentelemetry.io)

## Feature tracing-opentelemetry-exporter-jaeger documentation

- [Micronaut OpenTelemetry Exporter Jaeger documentation](http://localhost/micronaut-tracing/guide/index.html#opentelemetry)

- [https://opentelemetry.io](https://opentelemetry.io)

## Feature json-schema documentation

- [Micronaut JSON Schema documentation](https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/)

- [https://json-schema.org/learn/getting-started-step-by-step](https://json-schema.org/learn/getting-started-step-by-step)

## Feature junit-params documentation

- [https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests)

## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#nettyHttpClient)

## Feature tracing-opentelemetry-jaeger documentation

- [Micronaut OpenTelemetry Jaeger documentation](https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry)

- [https://opentelemetry.io](https://opentelemetry.io)

## Feature github-workflow-ci documentation

- [https://docs.github.com/en/actions](https://docs.github.com/en/actions)

## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)

## Feature json-schema-validation documentation

- [Micronaut JSON Schema Validation documentation](https://micronaut-projects.github.io/micronaut-json-schema/latest/guide/index.html#validation)

## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)

# Updates

This project is a test dashboard service built with Micronaut framework. It provides a monitoring and metrics visualization solution using a stack of:

- **Telegraf**: For collecting metrics from various sources
- **InfluxDB**: A time-series database for storing the collected metrics
- **Grafana**: For creating dashboards and visualizing the metrics
- **Kafka**: For processing real-time data streams

The service exposes a simple REST API endpoint at "/report/" and is designed for collecting, storing, and visualizing metrics in real-time.

## Example Data

Below is an example of the data that can be sent to the service:

```json
{
  "timestamp": "2023-06-15T14:30:00Z",
  "host": "web-server-01",
  "metrics": {
    "cpu": {
      "usage_percent": 45.2,
      "temperature": 62.5,
      "load_avg": [0.78, 0.59, 0.63]
    },
    "memory": {
      "total_mb": 16384,
      "used_mb": 8192,
      "free_mb": 8192,
      "usage_percent": 50.0
    },
    "disk": {
      "total_gb": 500,
      "used_gb": 350,
      "free_gb": 150,
      "usage_percent": 70.0
    },
    "network": {
      "rx_bytes": 1024000,
      "tx_bytes": 512000,
      "connections": 120
    }
  },
  "application": {
    "name": "web-app",
    "version": "1.2.3",
    "response_time_ms": 250,
    "error_rate": 0.05,
    "requests_per_second": 150
  },
  "tags": ["production", "web-tier", "us-west"]
}
```

This JSON payload includes system metrics (CPU, memory, disk, network) and application-specific metrics that can be used for monitoring and visualization in the dashboard.

## Test Results

The service can report test results from various testing frameworks. Below is an example of Espresso test results from Android that could be reported:

```json
{
  "testSuite": "com.example.app.LoginActivityTest",
  "timestamp": "2023-11-15T09:45:32Z",
  "device": {
    "model": "Pixel 6",
    "os": "Android 13",
    "api_level": 33,
    "screen_density": "420dpi",
    "screen_size": "1080x2400"
  },
  "results": {
    "total": 15,
    "passed": 13,
    "failed": 1,
    "skipped": 1,
    "duration_ms": 12450
  },
  "tests": [
    {
      "name": "testLoginWithValidCredentials",
      "status": "passed",
      "duration_ms": 1250,
      "assertions": 3
    },
    {
      "name": "testLoginWithInvalidPassword",
      "status": "passed",
      "duration_ms": 980,
      "assertions": 2
    },
    {
      "name": "testLoginWithEmptyFields",
      "status": "passed",
      "duration_ms": 850,
      "assertions": 4
    },
    {
      "name": "testForgotPasswordFlow",
      "status": "failed",
      "duration_ms": 1560,
      "error": "Element not found: forgot_password_confirmation",
      "stacktrace": "androidx.test.espresso.NoMatchingViewException: No views in hierarchy found matching: with id: com.example.app:id/forgot_password_confirmation\n  at androidx.test.espresso.ViewInteraction$SingleExecutionViewFinder.checkViewExists(ViewInteraction.java:457)\n  at androidx.test.espresso.ViewInteraction$SingleExecutionViewFinder.findView(ViewInteraction.java:409)\n  at androidx.test.espresso.ViewInteraction.doPerform(ViewInteraction.java:256)\n  at androidx.test.espresso.ViewInteraction.perform(ViewInteraction.java:200)\n  at com.example.app.LoginActivityTest.testForgotPasswordFlow(LoginActivityTest.java:112)"
    },
    {
      "name": "testRememberMeOption",
      "status": "skipped",
      "reason": "Feature not implemented yet"
    }
  ],
  "screenshots": [
    {
      "test": "testForgotPasswordFlow",
      "timestamp": "2023-11-15T09:45:30Z",
      "path": "/artifacts/screenshots/testForgotPasswordFlow_failure.png"
    }
  ],
  "performance": {
    "app_start_time_ms": 450,
    "average_frame_time_ms": 16.7,
    "jank_count": 3,
    "memory_usage_mb": 128.5
  }
}
```

This example shows how the service can collect detailed test results including test status, duration, error details, device information, and performance metrics. The dashboard can visualize this data to help teams identify test failures, performance issues, and trends over time.
