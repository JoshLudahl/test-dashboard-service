package com.softklass.grs;

import com.softklass.grs.model.TestRun;
import com.softklass.grs.service.InfluxService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ReportControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    InfluxService influxService; // mocked by @MockBean below

    @BeforeEach
    void resetMock() {
        Mockito.reset(influxService);
    }

    @io.micronaut.context.annotation.Factory
    static class MockFactory {
        @io.micronaut.context.annotation.Replaces(InfluxService.class)
        @jakarta.inject.Singleton
        InfluxService influxService() {
            return Mockito.mock(InfluxService.class);
        }
    }

    @Test
    void postJsonAccepted() {
        String body = "{\n" +
                "  \"framework\": \"cypress\",\n" +
                "  \"suite\": \"login.spec\",\n" +
                "  \"total\": 10,\n" +
                "  \"passed\": 9,\n" +
                "  \"failed\": 1,\n" +
                "  \"skipped\": 0,\n" +
                "  \"durationMs\": 12345\n" +
                "}";
        HttpRequest<String> req = HttpRequest.POST("/report/json", body)
                .contentType(MediaType.APPLICATION_JSON_TYPE);
        HttpResponse<String> resp = client.toBlocking().exchange(req, String.class);
        assertEquals(202, resp.getStatus().getCode());
        Mockito.verify(influxService, Mockito.times(1)).writeTestRun(Mockito.any(TestRun.class));
    }

    @Test
    void postJunitAccepted() {
        String xml = """
                <testsuite name=\"MySuite\" tests=\"2\" failures=\"1\" skipped=\"0\" time=\"0.4\">
                  <testcase name=\"ok\" time=\"0.2\"/>
                  <testcase name=\"bad\"><failure>boom</failure></testcase>
                </testsuite>
                """;
        HttpRequest<String> req = HttpRequest.POST("/report/junit", xml)
                .contentType(MediaType.TEXT_XML_TYPE);
        HttpResponse<String> resp = client.toBlocking().exchange(req, String.class);
        assertEquals(202, resp.getStatus().getCode());
        Mockito.verify(influxService, Mockito.atLeastOnce()).writeTestRun(Mockito.any(TestRun.class));
    }
}
