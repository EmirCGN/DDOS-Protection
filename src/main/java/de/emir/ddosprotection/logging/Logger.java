package de.emir.ddosprotection.logging;

public class Logger {
    public static void logAccess(String ipAddress, boolean isAllowed) {
        String logMessage = "Access from IP: " + ipAddress + " - Allowed: " + isAllowed;
        System.out.println(logMessage);

        if (!isAllowed) {
            triggerAlarm(ipAddress);
        }

        if (isAllowed) {
            sendNotification(ipAddress);
        }

        updateStatistics(ipAddress, isAllowed);
    }

    private static void triggerAlarm(String ipAddress) {
        // Code zum Auslösen eines Alarms oder anderen Aktionen bei nicht erlaubtem Zugriff
        System.out.println("Alarm ausgelöst für IP: " + ipAddress);
    }

    private static void sendNotification(String ipAddress) {
        // Code zum Senden einer Benachrichtigung bei erlaubtem Zugriff
        System.out.println("Benachrichtigung gesendet für IP: " + ipAddress);
    }

    private static void updateStatistics(String ipAddress, boolean isAllowed) {
        // Code zum Aktualisieren von Statistiken oder Metriken
        System.out.println("Statistiken aktualisiert für IP: " + ipAddress);
    }
}
