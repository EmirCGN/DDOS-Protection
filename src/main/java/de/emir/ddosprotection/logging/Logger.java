package de.emir.ddosprotection.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logAccess(String ipAddress, boolean isAllowed) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println(timestamp + " - IP Address: " + ipAddress + ", Access Allowed: " + isAllowed);
        // Log the access event using the AuditLogger
        AuditLogger.logAudit(timestamp + " - IP Address: " + ipAddress + ", Access Allowed: " + isAllowed);
    }

    public static void logFailedLogin(String ipAddress, int failedAttempts) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println(timestamp + " - IP Address: " + ipAddress + ", Failed Login Attempts: " + failedAttempts);
        // Log the failed login event using the AuditLogger
        AuditLogger.logAudit(timestamp + " - IP Address: " + ipAddress + ", Failed Login Attempts: " + failedAttempts);
    }
}