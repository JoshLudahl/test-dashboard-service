package com.softklass.grs.service;

import com.softklass.grs.model.TestRun;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;

@Singleton
public class InfluxService {

    private final HttpClient httpClient;
    private final String influxUrl;
    private final String database;
    private final String username;
    private final String password;

    public InfluxService(@Client("${influx.url:`http://localhost:8086`}") HttpClient httpClient,
                         @Value("${influx.url:`http://localhost:8086`}") String influxUrl,
                         @Value("${influx.database:`telegraf`}") String database,
                         @Value("${influx.username:`telegraf`}") String username,
                         @Value("${influx.password:`telegraf_password`}") String password) {
        this.httpClient = httpClient;
        this.influxUrl = influxUrl;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void writeTestRun(TestRun run) {
        // Build line protocol: measurement,tag=value field=value timestamp
        String measurement = "test_results";
        String tags = String.format("framework=%s,suite=%s",
                escapeTag(run.getFramework()), escapeTag(Objects.toString(run.getSuite(), "unknown")));
        int total = nonNull(run.getTotal());
        int failed = nonNull(run.getFailed());
        int skipped = nonNull(run.getSkipped());
        int passed = run.getPassed() != null ? run.getPassed() : Math.max(0, total - failed - skipped);
        long duration = run.getDurationMs() != null ? run.getDurationMs() : 0L;
        String fields = String.format("total=%di,passed=%di,failed=%di,skipped=%di,duration_ms=%di",
                total, passed, failed, skipped, duration);
        Instant ts = run.getTimestamp() != null ? run.getTimestamp() : Instant.now();
        long tsMs = ts.toEpochMilli();
        String line = String.format("%s,%s %s %d", measurement, tags, fields, tsMs * 1_000_000); // ns

        String writeUrl = influxUrl + "/write?db=" + urlencode(database) + "&precision=ms";
        HttpRequest<String> request = HttpRequest.POST(writeUrl, line)
                .contentType(MediaType.TEXT_PLAIN_TYPE)
                .basicAuth(username, password);
        HttpResponse<String> resp = httpClient.toBlocking().exchange(request, String.class);
        int code = resp.getStatus().getCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("Failed to write to Influx: status=" + resp.getStatus());
        }
    }

    private static String urlencode(String v) {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }

    private static String escapeTag(String v) {
        if (v == null) return "unknown";
        return v.replace(" ", "\\ ").replace(",", "\\,");
    }

    private static int nonNull(Integer v) { return v == null ? 0 : v; }
}
