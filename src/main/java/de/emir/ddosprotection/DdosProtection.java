package de.emir.ddosprotection;

import de.emir.ddosprotection.logging.AuditLogger;
import de.emir.ddosprotection.logging.Logger;
import de.emir.ddosprotection.security.IpRequestCounter;
import de.emir.ddosprotection.validation.IpAddressValidator;

import java.util.HashMap;
import java.util.Map;

public class DdosProtection {
    private static final int MAX_REQUESTS_PER_SECOND = 10;
    private static final long REQUEST_WINDOW_MILLISECONDS = 1000;
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;
    private final Map<String, IpRequestCounter> ipRequestCounters;
    private final Map<String, Integer> failedLoginAttempts;
    private final IpAddressValidator ipAddressValidator;

    public DdosProtection() {
        ipRequestCounters = new HashMap<>();
        failedLoginAttempts = new HashMap<>();
        ipAddressValidator = new IpAddressValidator();
    }

    private void blockIpAddress(String ipAddress, String reason) {
        System.out.println("IP address blocked: " + ipAddress + ", Reason: " + reason);

        // Log the blocked IP address and reason using the AuditLogger
        AuditLogger.logAudit("IP address blocked: " + ipAddress + ", Reason: " + reason);
    }



    public boolean isAllowed(String ipAddress, String userInput) {
        if (!ipAddressValidator.isValid(ipAddress)) {
            return false;
        }

        IpRequestCounter requestCounter = ipRequestCounters.get(ipAddress);
        if (requestCounter == null) {
            requestCounter = new IpRequestCounter(MAX_REQUESTS_PER_SECOND, REQUEST_WINDOW_MILLISECONDS);
            ipRequestCounters.put(ipAddress, requestCounter);
        }

        if (!requestCounter.isAllowed(ipAddress)) {
            blockIpAddress(ipAddress, "Blockiert: Anforderungslimit überschritten");
            return false;
        }

        if (ipAddressValidator.isPrivateIpAddress(ipAddress) || ipAddressValidator.isLoopbackIpAddress(ipAddress)) {
            blockIpAddress(ipAddress, "Blockiert: Private oder Loopback-IP");
            return false;
        }

        if (isSuspiciousActivity(userInput)) {
            // IP-Adresse blockieren, wenn es verdächtige Aktivitäten erkennt
            blockIpAddress(ipAddress, "Blockiert: Verdächtige Aktivität");
            return false;
        }

        return true;
    }

    private boolean isSuspiciousActivity(String userInput) {
        // Überprüfen auf verdächtige Schlüsselwörter
        String[] suspiciousKeywords = {"hack", "attack", "password", "admin"};
        for (String keyword : suspiciousKeywords) {
            if (userInput.contains(keyword)) {
                return true;
            }
        }

        // Überprüfen auf häufige Großbuchstaben im Benutzereingabe
        int consecutiveUppercaseCount = 0;
        int consecutiveLowercaseCount = 0;
        int maxConsecutiveUppercaseCount = 3;
        int maxConsecutiveLowercaseCount = 3;
        for (int i = 0; i < userInput.length(); i++) {
            char c = userInput.charAt(i);
            if (Character.isUpperCase(c)) {
                consecutiveUppercaseCount++;
                consecutiveLowercaseCount = 0;
            } else if (Character.isLowerCase(c)) {
                consecutiveLowercaseCount++;
                consecutiveUppercaseCount = 0;
            } else {
                consecutiveUppercaseCount = 0;
                consecutiveLowercaseCount = 0;
            }
            if (consecutiveUppercaseCount > maxConsecutiveUppercaseCount || consecutiveLowercaseCount > maxConsecutiveLowercaseCount) {
                return true;
            }
        }

        return false;
    }

    public void resetAllCounts() {
        for (Map.Entry<String, IpRequestCounter> entry : ipRequestCounters.entrySet()) {
            String ipAddress = entry.getKey();
            IpRequestCounter requestCounter = entry.getValue();
            requestCounter.resetCount(ipAddress);
        }
        System.out.println("Alle Anfragezähler zurückgesetzt");

        failedLoginAttempts.clear();
        System.out.println("Alle fehlgeschlagenen Anmeldeversuche zurückgesetzt");
    }
}
