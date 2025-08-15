package com.softklass.grs.model;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TestCaseResult {
    private String name;
    private String status; // passed, failed, skipped
    private Long durationMs;
    private String error;

    public TestCaseResult() {}

    public TestCaseResult(String name, String status, Long durationMs, String error) {
        this.name = name;
        this.status = status;
        this.durationMs = durationMs;
        this.error = error;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
