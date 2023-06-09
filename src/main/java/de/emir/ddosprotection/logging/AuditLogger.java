package de.emir.ddosprotection.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AuditLogger {
    private static final String LOG_FILE_PATH = "audit.log";

    public static void logAudit(String message) {
        System.out.println("Audit Log: " + message);
        writeLogToFile(message);
    }

    private static void writeLogToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Failed to write to audit log file: " + e.getMessage());
        }
    }
}
