package eu.obrowne.timer;

import java.util.function.Consumer;

public interface Triggerable<T> {
    void addConsumer(Consumer<T> consumer);
}
