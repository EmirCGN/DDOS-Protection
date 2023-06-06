package de.emir.ddosprotection.security;

import de.emir.ddosprotection.DdosProtection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpRequestCounter {
    private ConcurrentHashMap<String, Long> requestCount;
    private final int MAX_REQUESTS_PER_SECOND;
    private final long REQUEST_WINDOW_MILLISECONDS;
    private final int MAX_FAILED_LOGIN_ATTEMPTS = 5;
    private Map<String, Integer> failedLoginAttempts; // Hinzufügen einer Map für fehlgeschlagene Anmeldeversuche
    private DdosProtection ddosProtection;

    public IpRequestCounter(int maxRequestsPerSecond, long requestWindowMilliseconds) {
        requestCount = new ConcurrentHashMap<>();
        failedLoginAttempts = new ConcurrentHashMap<>(); // Initialisierung der Map für fehlgeschlagene Anmeldeversuche
        MAX_REQUESTS_PER_SECOND = maxRequestsPerSecond;
        REQUEST_WINDOW_MILLISECONDS = requestWindowMilliseconds;
    }

    public boolean isAllowed(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        long startTime = currentTime - REQUEST_WINDOW_MILLISECONDS;

        requestCount.entrySet().removeIf(entry -> entry.getValue() < startTime);

        long currentCount = requestCount.getOrDefault(ipAddress, 0L);

        if (currentCount >= MAX_REQUESTS_PER_SECOND) {
            return false;
        }

        requestCount.put(ipAddress, currentCount + 1L);
        return true;
    }

    public void resetCount(String ipAddress) {
        requestCount.remove(ipAddress);
    }

    public void resetAllCounts() {
        requestCount.clear();
        failedLoginAttempts.clear();
        System.out.println("Alle Anfragezähler und fehlgeschlagenen Anmeldeversuche zurückgesetzt");
    }

    public void registerFailedLoginAttempt(String ipAddress) {
        // Überprüfen, ob der IP-Zähler für fehlgeschlagene Anmeldeversuche bereits vorhanden ist
        if (failedLoginAttempts.containsKey(ipAddress)) {
            int currentCount = failedLoginAttempts.get(ipAddress);
            // Inkrementieren des Zählers um 1
            failedLoginAttempts.put(ipAddress, currentCount + 1);
        } else {
            // IP-Zähler initialisieren und auf 1 setzen
            failedLoginAttempts.put(ipAddress, 1);
        }

        // Weitere Aktionen für fehlgeschlagene Anmeldeversuche ausführen
        System.out.println("Fehlgeschlagener Anmeldeversuch registriert für IP: " + ipAddress);

        // Überprüfen, ob die maximale Anzahl von fehlgeschlagenen Anmeldeversuchen erreicht wurde
        if (failedLoginAttempts.get(ipAddress) >= MAX_FAILED_LOGIN_ATTEMPTS) {
            // Hier können Sie weitere Aktionen ausführen, wie das Blockieren der IP-Adresse oder das Auslösen eines Alarms
            System.out.println("Maximale Anzahl von fehlgeschlagenen Anmeldeversuchen erreicht für IP: " + ipAddress);
        }
    }
}
