package de.emir.ddosprotection.scheduler;

import de.emir.ddosprotection.DdosProtection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResetScheduler {
    private final long RESET_INTERVAL_MINUTES = 60;
    private final ScheduledExecutorService executorService;
    private final DdosProtection ddosProtection;

    public ResetScheduler(DdosProtection ddosProtection) {
        this.ddosProtection = ddosProtection;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleAtFixedRate(this::resetAllCounts, 0, RESET_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void resetAllCounts() {
        ddosProtection.resetAllCounts();
        System.out.println("All request counts reset");
    }
}
