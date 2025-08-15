package com.softklass.grs.model;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Serdeable
public class TestRun {
    private String framework; // junit, cypress, playwright, xcuitest, etc.
    private String suite;
    private Instant timestamp; // when tests ran
    private Integer total;
    private Integer passed;
    private Integer failed;
    private Integer skipped;
    private Long durationMs;
    private List<TestCaseResult> tests = new ArrayList<>();

    public TestRun() {}

    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }

    public String getSuite() { return suite; }
    public void setSuite(String suite) { this.suite = suite; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Integer getPassed() { return passed; }
    public void setPassed(Integer passed) { this.passed = passed; }

    public Integer getFailed() { return failed; }
    public void setFailed(Integer failed) { this.failed = failed; }

    public Integer getSkipped() { return skipped; }
    public void setSkipped(Integer skipped) { this.skipped = skipped; }

    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }

    public List<TestCaseResult> getTests() { return tests; }
    public void setTests(List<TestCaseResult> tests) { this.tests = tests; }
}
