package project2;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // Change this path to the folder you want to scan
        File startFolder = new File("logs");

        LogScanner scanner = new LogScanner();
        scanner.scanForLogs(startFolder);

        LogAnalyzer analyzer = new LogAnalyzer();
        analyzer.processFiles(scanner.getLogFiles());

        ReportGenerator report = new ReportGenerator();
        report.printReport(analyzer.getSummaries(), scanner.getFilesScanned(), analyzer.getTotalFailedAttempts());
    }
}