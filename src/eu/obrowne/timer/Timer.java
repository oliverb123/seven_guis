package eu.obrowne.timer;

import eu.obrowne.booker.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Timer implements Triggerable<Pair<Long, Long>> {
    private final List<Consumer<Pair<Long, Long>>> receivers = new ArrayList<>();
    private LocalDateTime lastReset = LocalDateTime.now();
    private LocalDateTime lastTick = LocalDateTime.now();
    private LocalDateTime finish = LocalDateTime.now();

    @Override
    public void addConsumer(Consumer<Pair<Long, Long>> receiver) {
        receivers.add(receiver);
    }

    private synchronized void message() {
        receivers.forEach(r -> r.accept(new Pair<>(timeElapsed(), timeRemaining())));
    }

    public synchronized void tick() {
        lastTick = LocalDateTime.now();
        message();
    }

    public synchronized void reset() {
        var duration = Duration.between(lastReset, finish).toMillis();
        lastReset = LocalDateTime.now();
        setDuration(duration);
        message();
    }

    public synchronized void setDuration(long durationMillis) {
        finish = lastReset.plus(durationMillis, ChronoUnit.MILLIS);
        message();
    }

    public Long timeRemaining() {
        return Math.max(0, Duration.between(lastTick, finish).toMillis());
    }

    public Long timeElapsed() {
        return Math.max(0,
                Math.min(Duration.between(lastReset, finish).toMillis(),
                        Duration.between(lastReset, lastTick).toMillis()));
    }
}
