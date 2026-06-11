package project2;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void printReport(Map<String, LogAnalyzer.Summary> data, int filesScanned, int totalFailed) {
        System.out.println("=== Failed Login Attempts Report ===");
        System.out.println();

        // header
        System.out.printf("%-16s  %-8s  %-25s  %-19s  %-19s%n", "IP Address", "Attempts", "Usernames", "First", "Last");
        System.out.println("------------------------------------------------------------------------------------------");

        for (Map.Entry<String, LogAnalyzer.Summary> e : data.entrySet()) {
            String ip = e.getKey();
            LogAnalyzer.Summary s = e.getValue();
            String users = s.getUsers().stream().collect(Collectors.joining(","));
            String first = s.getFirst() == null ? "" : s.getFirst().format(FMT);
            String last = s.getLast() == null ? "" : s.getLast().format(FMT);
            System.out.printf("%-16s  %-8d  %-25s  %-19s  %-19s%n", ip, s.getCount(), users, first, last);
        }

        System.out.println();
        System.out.println("Total files scanned: " + filesScanned);
        System.out.println("Total failed attempts: " + totalFailed);
        System.out.println();
        System.out.println("=== End of Report ===");
    }
}