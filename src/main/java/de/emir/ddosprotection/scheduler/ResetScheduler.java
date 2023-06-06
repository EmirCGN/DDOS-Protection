package de.emir.ddosprotection.scheduler;

import de.emir.ddosprotection.DdosProtection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResetScheduler {
    private final long RESET_INTERVAL_MINUTES = 60;
    private ScheduledExecutorService executorService;
    private DdosProtection ddosProtection;

    public ResetScheduler(DdosProtection ddosProtection) {
        this.ddosProtection = ddosProtection;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::resetAllCounts, 0, RESET_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void resetAllCounts() {
        ddosProtection.resetAllCounts();
        System.out.println("All request counts reset");
    }
}
