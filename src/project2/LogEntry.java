package project2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class LogEntry {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String user;
    private final String ip;
    private final String status;

    public LogEntry(LocalDateTime timestamp, String user, String ip, String status) {
        this.timestamp = timestamp;
        this.user = user == null ? "" : user;
        this.ip = ip;
        this.status = status == null ? "" : status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getIp() {
        return ip;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Parse two kinds of log lines:
     *  1. Full format:
     *     2025-11-07 10:22:01 [AUTH] user=Kara ip=131.44.45.22 status=FAIL
     *
     *  2. Simple format:
     *     Failed login from 192.168.1.5
     */
    public static Optional<LogEntry> parse(String line) {

        if (line == null) return Optional.empty();
        line = line.trim();
        if (line.isEmpty()) return Optional.empty();

        // ===== Type 1: Full format with timestamp =====
        if (line.length() >= 19) {
            String tsPart = line.substring(0, 19);
            try {
                LocalDateTime ts = LocalDateTime.parse(tsPart, FORMATTER);

                String user = "";
                String ip = null;
                String status = null;

                String rest = line.substring(19);
                String[] tokens = rest.split("\\s+");

                for (String token : tokens) {
                    if (token.startsWith("user=")) {
                        user = token.substring(5);
                    } else if (token.startsWith("ip=")) {
                        ip = token.substring(3);
                    } else if (token.startsWith("status=")) {
                        status = token.substring(7);
                    }
                }

                if (ip != null && status != null) {
                    return Optional.of(new LogEntry(ts, user, ip, status));
                }

            } catch (Exception ignored) {
            }
        }

        // ===== Type 2: Simple format =====
        // "Failed login from 192.168.1.5"
        if (line.toLowerCase().startsWith("failed login from")) {

            String[] parts = line.split(" ");
            String ip = parts[parts.length - 1];

            LocalDateTime ts = LocalDateTime.now();

            return Optional.of(new LogEntry(ts, "unknown", ip, "FAIL"));
        }

        return Optional.empty();
    }
}
