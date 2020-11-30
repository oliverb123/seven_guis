package eu.obrowne.converter;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


// When this object receives alerts of type T, it maps them to
// type R then displays them if they are valid.
// When it receives valid input, it produces alerts of type R
public class AlertingTextDisplay<T, R> extends TextField {
    private Function<T, Optional<String>> alertMapping;
    private Map<String, Consumer<R>> listeners = new HashMap<>();

    public AlertingTextDisplay(String initVal,
                               Function<T, Optional<String>> alertMapping,
                               Function<String, Optional<R>> inputMapping) {
        super(10);
        this.alertMapping = alertMapping;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                var result = inputMapping.apply(getText());
                result.ifPresent(r -> alertListeners(r));
            }
        });
    }

    public void setListener(String id, Consumer<R> consumer) {
        listeners.put(id, consumer);
    }

    public void removeListener(String id) {
        listeners.remove(id);
    }

    public void receive(T alert) {
        alertMapping.apply(alert).ifPresent(this::setText);
    }

    private void alertListeners(R alert) {
        listeners.values().forEach(c -> c.accept(alert));
    }
}
