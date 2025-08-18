package com.softklass.grs.service;

import com.softklass.grs.model.TestRun;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;

@Singleton
public class InfluxService {

    private final HttpClient httpClient;
    private final String influxUrl;
    private final String org;
    private final String bucket;
    private final String token;

    public InfluxService(
            @Client("${influx.url:`http://localhost:8086`}") HttpClient httpClient,
            @Value("${influx.url:`http://localhost:8086`}") String influxUrl,
            @Value("${influx.org:${INFLUXDB_ORG:grs}}") String org,
            @Value("${influx.bucket:${INFLUXDB_BUCKET:telegraf}}") String bucket,
            @Value("${influx.token:${INFLUXDB_TOKEN}}") String token) {
        this.httpClient = httpClient;
        this.influxUrl = influxUrl;
        this.org = org;
        this.bucket = bucket;
        this.token = token;
    }

    public void writeTestRun(TestRun run) {
        String measurement = "playwright_tests";
        String tags = String.format("framework=%s,suite=%s",
                escapeTag(run.getFramework()), escapeTag(Objects.toString(run.getSuite(), "unknown")));
        int total = nonNull(run.getTotal());
        int failed = nonNull(run.getFailed());
        int skipped = nonNull(run.getSkipped());
        int passed = run.getPassed() != null ? run.getPassed() : Math.max(0, total - failed - skipped);
        float duration = run.getDurationMs() != null ? run.getDurationMs() : 0L;
        String fields = String.format("total=%di,passed=%di,failed=%di,skipped=%di,duration_ms=%f",
                total, passed, failed, skipped, duration);
        Instant ts = run.getTimestamp() != null ? run.getTimestamp() : Instant.now();
        long tsMs = ts.toEpochMilli();

        String line = String.format("%s,%s %s %d", measurement, tags, fields, tsMs * 1_000_000); // ns precision

        // InfluxDB v2 write endpoint
        String writeUrl = influxUrl + "/api/v2/write?org=" + urlencode("grs") + "&bucket=" + urlencode("telegraf") + "&precision=ns";
        HttpRequest<String> request = HttpRequest.POST(writeUrl, line)
                .contentType(MediaType.TEXT_PLAIN_TYPE)
                .header(HttpHeaders.AUTHORIZATION, "Token " + "Xpp109RyUZemfbqmI49Uvf3N5YVCizqbmCXEcj8ABbJajon9IJah2-g2haubzO3WjQRrPMN4OepsiZdsMsUyuQ==");

        HttpResponse<String> resp = httpClient.toBlocking().exchange(request, String.class);
        int code = resp.getStatus().getCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("Failed to write to InfluxDB v2: status=" + resp.getStatus() + " body=" + resp.body());
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
