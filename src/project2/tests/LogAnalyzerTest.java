package project2.tests;

import project2.LogAnalyzer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class LogAnalyzerTest {

    public static void main(String[] args) throws Exception {
        try {
            testSingleIPMultipleFails();
            testMultipleIPs();
            testIgnoresMalformedAndOk();
            testMainWithSampleLogs();
            System.out.println("LogAnalyzerTest: ALL PASS");
        } catch (AssertionError e) {
            System.err.println("LogAnalyzerTest: FAIL - " + e.getMessage());
            throw e;
        }
    }

    private static void testSingleIPMultipleFails() throws Exception {
        Path tmp = Files.createTempFile("logtest", ".log");
        List<String> lines = List.of(
                "2025-11-07 10:22:01 [AUTH] user=Marisa ip=131.44.45.22 status=FAIL",
                "2025-11-07 10:23:00 [AUTH] user=Marisa ip=131.44.45.22 status=FAIL",
                "2025-11-07 10:24:00 [AUTH] user=Marisa ip=131.44.45.22 status=OK"
        );
        Files.write(tmp, lines);

        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.processFiles(List.of(tmp.toFile()));

        Map<String, LogAnalyzer.Summary> sums = analyzer.getSummaries();
        assertTrue(sums.containsKey("131.44.45.22"), "missing ip");
        LogAnalyzer.Summary s = sums.get("131.44.45.22");
        assertEquals(2, s.getCount(), "count");
        assertTrue(s.getUsers().contains("Marisa"), "user missing");
        assertNotNull(s.getFirst(), "first ");
        assertNotNull(s.getLast(), "last");

        Files.deleteIfExists(tmp);
    }

    private static void testMultipleIPs() throws Exception {
        Path tmp = Files.createTempFile("logtest2", ".log");
        List<String> lines = List.of(
                "2025-11-07 10:22:01 [AUTH] user=A ip=1.1.1.1 status=FAIL",
                "2025-11-07 10:22:05 [AUTH] user=B ip=2.2.2.2 status=FAIL",
                "2025-11-07 10:23:00 [AUTH] user=A ip=1.1.1.1 status=FAIL"
        );
        Files.write(tmp, lines);

        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.processFiles(List.of(tmp.toFile()));

        Map<String, LogAnalyzer.Summary> sums = analyzer.getSummaries();
        assertEquals(2, sums.size(), "size");
        assertEquals(2, sums.get("1.1.1.1").getCount(), "count 1.1.1.1");
        assertEquals(1, sums.get("2.2.2.2").getCount(), "count 2.2.2.2");

        Files.deleteIfExists(tmp);
    }

    private static void testIgnoresMalformedAndOk() throws Exception {
        Path tmp = Files.createTempFile("logtest3", ".log");
        List<String> lines = List.of(
                "not a log line",
                "2025-11-07 10:22:01 [AUTH] user=C ip=3.3.3.3 status=OK",
                "2025-11-07 10:22:05 [AUTH] user=C ip=3.3.3.3 status=FAIL"
        );
        Files.write(tmp, lines);

        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.processFiles(List.of(tmp.toFile()));

        Map<String, LogAnalyzer.Summary> sums = analyzer.getSummaries();
        assertEquals(1, sums.size(), "size");
        assertTrue(sums.containsKey("3.3.3.3"), "missing 3.3.3.3");
        assertEquals(1, sums.get("3.3.3.3").getCount(), "count");

        Files.deleteIfExists(tmp);
    }

    private static void testMainWithSampleLogs() throws Exception {
        // Create a temporary directory for logs
        Path tempDir = Files.createTempDirectory("logtestdir");

        // Create sample log files
        Path log1 = tempDir.resolve("log1.log");
        Path log2 = tempDir.resolve("log2.log");
        Files.write(log1, List.of(
                "2025-11-07 10:22:01 [AUTH] user=Alice ip=192.168.1.1 status=FAIL",
                "2025-11-07 10:23:01 [AUTH] user=Bob ip=192.168.1.2 status=FAIL"
        ));
        Files.write(log2, List.of(
                "2025-11-07 10:24:01 [AUTH] user=Charlie ip=192.168.1.3 status=FAIL",
                "2025-11-07 10:25:01 [AUTH] user=Alice ip=192.168.1.1 status=FAIL"
        ));

        // Run the main method with the temporary directory
        System.setProperty("user.dir", tempDir.toString());
        project2.Main.main(new String[]{});

        // Clean up temporary files
        Files.deleteIfExists(log1);
        Files.deleteIfExists(log2);
        Files.deleteIfExists(tempDir);
    }

    // simple assertion helpers
    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }
    private static void assertFalse(boolean cond, String msg) {
        if (cond) throw new AssertionError(msg);
    }
    private static void assertEquals(int a, int b, String msg) {
        if (a != b) throw new AssertionError(msg + ": expected " + b + " got " + a);
    }
    private static void assertEquals(Object a, Object b, String msg) {
        if (a == null ? b != null : !a.equals(b)) throw new AssertionError(msg + ": expected " + b + " got " + a);
    }
    private static void assertNotNull(Object o, String msg) {
        if (o == null) throw new AssertionError(msg + " is null");
    }
}