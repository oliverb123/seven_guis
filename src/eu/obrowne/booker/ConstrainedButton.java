package eu.obrowne.booker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ConstrainedButton extends JButton implements ExternalConstraintChild<String> {
    private final List<BiFunction<String,
            Pair<ExternalConstraintParent<String>,
                    ExternalConstraintChild<String>>, ConstraintState>> externalConstraints = new ArrayList<>();

    private ConstraintState state;

    public ConstrainedButton(String value) {
        super(value);
        setFont(new Font(getFont().getName(), Font.PLAIN, 20));
    }

    @Override
    public void accept(ExternalConstraintParent<String> parent, String val) {
        state = externalConstraints.stream()
                .map(c -> c.apply(val, new Pair<>(parent, this)))
                .reduce((a, b) -> a.ordinal() > b.ordinal() ? a : b).orElse(ConstraintState.NONE);
        applyConstraintState();
    }

    @Override
    public void addExternalConstraint(BiFunction<String, Pair<ExternalConstraintParent<String>, ExternalConstraintChild<String>>, ConstraintState> constraint) {
        externalConstraints.add(constraint);
    }

    @Override
    public ConstraintState currentConstraintState() {
        return state;
    }

    private void applyConstraintState() {
        setEnabled(!state.equals(ConstraintState.DISABLED));
    }
}
