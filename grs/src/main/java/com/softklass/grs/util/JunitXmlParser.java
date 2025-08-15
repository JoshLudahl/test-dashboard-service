package com.softklass.grs.util;

import com.softklass.grs.model.TestCaseResult;
import com.softklass.grs.model.TestRun;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public final class JunitXmlParser {
    private JunitXmlParser() {}

    public static TestRun parse(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setExpandEntityReferences(false);
            Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = doc.getDocumentElement();
            TestRun run = new TestRun();
            run.setFramework("junit");
            if (root.getTagName().equals("testsuite")) {
                fillFromTestSuite(run, root);
            } else if (root.getTagName().equals("testsuites")) {
                // take first testsuite
                NodeList nl = root.getElementsByTagName("testsuite");
                if (nl.getLength() > 0) {
                    fillFromTestSuite(run, (Element) nl.item(0));
                }
            }
            if (run.getTimestamp() == null) {
                run.setTimestamp(Instant.now());
            }
            return run;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JUnit XML", e);
        }
    }

    private static void fillFromTestSuite(TestRun run, Element suite) {
        run.setSuite(suite.getAttribute("name"));
        Integer total = parseIntAttr(suite, "tests");
        Integer failures = parseIntAttr(suite, "failures");
        Integer errors = parseIntAttr(suite, "errors");
        Integer skipped = parseIntAttr(suite, "skipped");
        Double timeSec = parseDoubleAttr(suite, "time");
        String tsAttr = suite.getAttribute("timestamp");
        if (tsAttr != null && !tsAttr.isEmpty()) {
            try { run.setTimestamp(Instant.parse(tsAttr)); } catch (Exception ignored) {}
        }
        int failed = (failures == null ? 0 : failures) + (errors == null ? 0 : errors);
        run.setTotal(total);
        run.setFailed(failed);
        run.setSkipped(skipped);
        if (total != null) {
            run.setPassed(Math.max(0, total - failed - (skipped == null ? 0 : skipped)));
        }
        if (timeSec != null) {
            run.setDurationMs((long) Math.round(timeSec * 1000d));
        }
        NodeList cases = suite.getElementsByTagName("testcase");
        for (int i = 0; i < cases.getLength(); i++) {
            Element c = (Element) cases.item(i);
            String name = c.getAttribute("name");
            Long tMs = null;
            String t = c.getAttribute("time");
            if (t != null && !t.isEmpty()) {
                try { tMs = (long) (Double.parseDouble(t) * 1000); } catch (Exception ignored) {}
            }
            String status = "passed";
            String error = null;
            if (c.getElementsByTagName("skipped").getLength() > 0) status = "skipped";
            if (c.getElementsByTagName("failure").getLength() > 0 || c.getElementsByTagName("error").getLength() > 0) {
                status = "failed";
                Node fail = c.getElementsByTagName("failure").getLength() > 0 ? c.getElementsByTagName("failure").item(0) : c.getElementsByTagName("error").item(0);
                if (fail != null) error = fail.getTextContent();
            }
            run.getTests().add(new TestCaseResult(name, status, tMs, error));
        }
    }

    private static Integer parseIntAttr(Element e, String name) {
        String v = e.getAttribute(name);
        if (v == null || v.isEmpty()) return null;
        try { return (int) Double.parseDouble(v); } catch (Exception ex) { return null; }
    }
    private static Long parseLongAttr(Element e, String name) {
        String v = e.getAttribute(name);
        if (v == null || v.isEmpty()) return null;
        try { return (long) Double.parseDouble(v); } catch (Exception ex) { return null; }
    }
    private static Double parseDoubleAttr(Element e, String name) {
        String v = e.getAttribute(name);
        if (v == null || v.isEmpty()) return null;
        try { return Double.parseDouble(v); } catch (Exception ex) { return null; }
    }
}
