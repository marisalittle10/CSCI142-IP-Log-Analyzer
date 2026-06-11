package project2;

import java.io.File;
import java.util.ArrayList;

public class LogScanner {

    private ArrayList<File> logFiles = new ArrayList<>();
    private int filesScanned = 0;

    public void scanForLogs(File folder) {
        if (!folder.exists()) {
            System.out.println("Folder not found: " + folder.getAbsolutePath());
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanForLogs(f); // recursive scan
            } else {
                // case-insensitive .log check
                String name = f.getName();
                if (name.toLowerCase().endsWith(".log")) {
                    logFiles.add(f);
                }
                filesScanned++;
            }
        }
    }

    public ArrayList<File> getLogFiles() {
        return logFiles;
    }

    public int getFilesScanned() {
        return filesScanned;
    }
}