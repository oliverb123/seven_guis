package eu.obrowne.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Ticker {
    private ScheduledFuture<Boolean> run;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private List<Runnable> targets = new ArrayList<>();

    Ticker(long intervalMillis) {
        run = executor.schedule(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                executor.schedule(this, intervalMillis, TimeUnit.MILLISECONDS); // This should use time instants and offsets to avoid time wandering
                try {
                    targets.forEach(Runnable::run);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
                return true;
            }
        }, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public void addTarget(Runnable r) {
        targets.add(r);
    }

    public void stop() {
        run.cancel(true);
        executor.shutdownNow();
    }
}
