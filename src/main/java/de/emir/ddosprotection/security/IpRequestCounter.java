package de.emir.ddosprotection.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpRequestCounter {
    private final ConcurrentHashMap<String, Long> requestCounters;
    private final int maxRequestsPerSecond;
    private final long requestWindowMilliseconds;
    private long lastRequestTime;

    public IpRequestCounter(int maxRequestsPerSecond, long requestWindowMilliseconds) {
        this.requestCounters = new ConcurrentHashMap<>();
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.requestWindowMilliseconds = requestWindowMilliseconds;
        this.lastRequestTime = 0;
    }

    public void resetCount(String ipAddress) {
        requestCounters.remove(ipAddress);
    }

    public void resetAllCounts() {
        requestCounters.clear();
        System.out.println("All request counters have been reset");
    }

    public synchronized boolean isAllowed(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime > requestWindowMilliseconds) {
            // Reset request count if the request window has elapsed
            requestCounters.remove(ipAddress);
        }
        lastRequestTime = currentTime;

        Long requestCount = requestCounters.get(ipAddress);
        if (requestCount != null && requestCount >= maxRequestsPerSecond) {
            return false;
        }

        requestCounters.put(ipAddress, requestCount != null ? requestCount + 1 : 1);
        return true;
    }
}