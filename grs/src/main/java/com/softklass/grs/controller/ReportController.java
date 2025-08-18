package com.softklass.grs.controller;

import com.softklass.grs.model.TestRun;
import com.softklass.grs.service.InfluxService;
import com.softklass.grs.util.JunitXmlParser;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller("/report")
public class ReportController {

    @Inject
    InfluxService influxService;

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Test Dashboard Service - OK";
    }

    @Post(value = "/json", consumes = MediaType.APPLICATION_JSON)
    @ExecuteOn(TaskExecutors.BLOCKING)  // run this method on a separate thread pool
    public HttpResponse<?> postJson(@Body TestRun run) {
        if (run.getTimestamp() == null) run.setTimestamp(Instant.now());
        if (run.getFramework() == null) run.setFramework("generic");
        influxService.writeTestRun(run);
        return HttpResponse.accepted();
    }

    @Post(value = "/junit", consumes = {MediaType.TEXT_XML, MediaType.APPLICATION_XML})
    public HttpResponse<?> postJunit(@Body String xml) {
        TestRun run = JunitXmlParser.parse(xml);
        influxService.writeTestRun(run);
        return HttpResponse.accepted();
    }

    @Get("/health")
    public Map<String, Object> health() {
        Map<String, Object> m = new HashMap<>();
        m.put("status", "UP");
        return m;
    }

    @Serdeable
    public record DailySummary(String day, String suite, String framework, int total, int passed, int failed, int skipped) {}
}
