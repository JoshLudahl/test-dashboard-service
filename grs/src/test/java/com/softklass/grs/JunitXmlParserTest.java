package com.softklass.grs;

import com.softklass.grs.model.TestRun;
import com.softklass.grs.util.JunitXmlParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JunitXmlParserTest {

    @Test
    void parsesSimpleSuite() {
        String xml = """
                <testsuite name=\"ExampleSuite\" tests=\"3\" failures=\"1\" errors=\"0\" skipped=\"1\" time=\"1.5\" timestamp=\"2025-08-15T10:00:00Z\">
                  <testcase name=\"ok\" time=\"0.5\"/>
                  <testcase name=\"skip\"><skipped/></testcase>
                  <testcase name=\"fail\"><failure>boom</failure></testcase>
                </testsuite>
                """;
        TestRun run = JunitXmlParser.parse(xml);
        assertEquals("junit", run.getFramework());
        assertEquals("ExampleSuite", run.getSuite());
        assertEquals(3, run.getTotal());
        assertEquals(1, run.getFailed());
        assertEquals(1, run.getSkipped());
        assertEquals(1, run.getPassed());
        assertEquals(1500L, run.getDurationMs());
        assertEquals(3, run.getTests().size());
        assertEquals("passed", run.getTests().get(0).getStatus());
        assertEquals("skipped", run.getTests().get(1).getStatus());
        assertEquals("failed", run.getTests().get(2).getStatus());
    }
}
