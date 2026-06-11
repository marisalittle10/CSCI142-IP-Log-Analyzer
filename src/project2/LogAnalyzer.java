package project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class LogAnalyzer {

    public static class Summary {
        private int count = 0;
        private Set<String> users = new HashSet<>();
        private LocalDateTime first = null;
        private LocalDateTime last = null;

        public void addAttempt(String user, LocalDateTime ts) {
            count++;
            if (user != null && !user.isEmpty()) users.add(user);
            if (ts != null) {
                if (first == null || ts.isBefore(first)) first = ts;
                if (last == null || ts.isAfter(last)) last = ts;
            }
        }

        public int getCount() { return count; }
        public Set<String> getUsers() { return users; }
        public LocalDateTime getFirst() { return first; }
        public LocalDateTime getLast() { return last; }
    }

    private Map<String, Summary> summaries = new HashMap<>();
    private int totalFailedAttempts = 0;

    public void processFiles(Iterable<File> files) {
        for (File file : files) {
            readFile(file);
        }
    }

    private void readFile(File file) {
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                Optional<LogEntry> opt = LogEntry.parse(line);
                if (!opt.isPresent()) continue;
                LogEntry entry = opt.get();
                if (entry.getStatus() != null && entry.getStatus().equalsIgnoreCase("FAIL")) {
                    String ip = entry.getIp();
                    if (ip == null || ip.isEmpty()) continue;
                    Summary s = summaries.get(ip);
                    if (s == null) {
                        s = new Summary();
                        summaries.put(ip, s);
                    }
                    s.addAttempt(entry.getUser(), entry.getTimestamp());
                    totalFailedAttempts++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file: " + file.getName());
        }
    }

    public Map<String, Summary> getSummaries() {
        return summaries;
    }

    public int getTotalFailedAttempts() {
        return totalFailedAttempts;
    }

}