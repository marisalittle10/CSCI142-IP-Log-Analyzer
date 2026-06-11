package project2.tests;

import project2.LogEntry;

public class LogEntryTest {

    public static void main(String[] args) {
        try {
            testParseValid();
            testParseInvalid();
            System.out.println("LogEntryTest: ALL PASS");
        } catch (AssertionError e) {
            System.err.println("LogEntryTest: FAIL - " + e.getMessage());
            throw e;
        }
    }

    private static void testParseValid() {
        String line = "2025-11-07 10:22:01 [AUTH] user=Kara ip=131.44.45.22 status=FAIL";
        var opt = LogEntry.parse(line);
        assertTrue(opt.isPresent(), "expected present");
        var e = opt.get();
        assertEquals("Kara", e.getUser(), "user");
        assertEquals("131.44.45.22", e.getIp(), "ip");
        assertEquals("FAIL", e.getStatus(), "status");
        assertNotNull(e.getTimestamp(), "timestamp");
    }

    private static void testParseInvalid() {
        String line = "this is not a valid log line";
        var opt = LogEntry.parse(line);
        assertFalse(opt.isPresent(), "should be empty");
    }

    // simple assertion helpers
    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new AssertionError(msg);
    }
    private static void assertFalse(boolean cond, String msg) {
        if (cond) throw new AssertionError(msg);
    }
    private static void assertEquals(Object a, Object b, String msg) {
        if (a == null ? b != null : !a.equals(b)) throw new AssertionError(msg + ": expected " + b + " got " + a);
    }
    private static void assertNotNull(Object o, String msg) {
        if (o == null) throw new AssertionError(msg + " is null");
    }
}