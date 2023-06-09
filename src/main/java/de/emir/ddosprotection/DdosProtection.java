package de.emir.ddosprotection;

import de.emir.ddosprotection.logging.AuditLogger;
import de.emir.ddosprotection.logging.Logger;
import de.emir.ddosprotection.security.IpRequestCounter;
import de.emir.ddosprotection.validation.IpAddressValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DdosProtection {
    private static final int MAX_REQUESTS_PER_SECOND = 10;
    private static final long REQUEST_WINDOW_MILLISECONDS = 1000;
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private static final int MAX_CONSECUTIVE_UPPERCASE_COUNT = 3;
    private static final int MAX_CONSECUTIVE_LOWERCASE_COUNT = 3;
    private final Map<String, IpRequestCounter> ipRequestCounters;
    private final Map<String, Integer> failedLoginAttempts;
    private final IpAddressValidator ipAddressValidator;

    public DdosProtection() {
        ipRequestCounters = new ConcurrentHashMap<>();
        failedLoginAttempts = new ConcurrentHashMap<>();
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

        IpRequestCounter requestCounter = ipRequestCounters.computeIfAbsent(ipAddress, key ->
                new IpRequestCounter(MAX_REQUESTS_PER_SECOND, REQUEST_WINDOW_MILLISECONDS));

        if (!requestCounter.isAllowed(ipAddress)) {
            blockIpAddress(ipAddress, "Blocked: Request limit exceeded");
            return false;
        }

        if (ipAddressValidator.isPrivateIpAddress(ipAddress) || ipAddressValidator.isLoopbackIpAddress(ipAddress)) {
            blockIpAddress(ipAddress, "Blocked: Private or loopback IP");
            return false;
        }

        if (containsConsecutiveUppercase(userInput, MAX_CONSECUTIVE_UPPERCASE_COUNT)) {
            blockIpAddress(ipAddress, "Blocked: Suspicious activity");
            return false;
        }

        if (containsConsecutiveLowercase(userInput, MAX_CONSECUTIVE_LOWERCASE_COUNT)) {
            blockIpAddress(ipAddress, "Blocked: Suspicious activity");
            return false;
        }

        int failedAttempts = failedLoginAttempts.getOrDefault(ipAddress, 0);
        if (failedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            blockIpAddress(ipAddress, "Blocked: Exceeded maximum failed login attempts");
            return false;
        }

        if (userInput.equals("admin")) {
            // Increment the failed login attempts counter for the IP address
            failedAttempts++;
            failedLoginAttempts.put(ipAddress, failedAttempts);
            Logger.logFailedLogin(ipAddress, failedAttempts);
            return false;
        }

        // Reset the failed login attempts counter for the IP address
        failedLoginAttempts.remove(ipAddress);

        // Process the request normally
        return true;
    }

    private boolean containsConsecutiveUppercase(String input, int consecutiveCount) {
        int count = 0;
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                count++;
                if (count >= consecutiveCount) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    private boolean containsConsecutiveLowercase(String input, int consecutiveCount) {
        int count = 0;
        for (char c : input.toCharArray()) {
            if (Character.isLowerCase(c)) {
                count++;
                if (count >= consecutiveCount) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    public void resetAllCounts() {
        ipRequestCounters.clear();
        failedLoginAttempts.clear();
        System.out.println("All request counters reset");
        System.out.println("Reset all failed login attempts");
        AuditLogger.logAudit("All counts reset");
    }
}