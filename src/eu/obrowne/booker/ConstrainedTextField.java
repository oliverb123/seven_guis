package eu.obrowne.booker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ConstrainedTextField extends JTextField implements
        InternalConstraints<String>,
        ExternalConstraintChild<String>,
        ExternalConstraintParent<String>,
        KeyListener {

    private final List<BiFunction<String, InternalConstraints<String>, ConstraintState>> internalConstraints = new ArrayList<>();
    private final List<BiFunction<String,
            Pair<ExternalConstraintParent<String>,
                    ExternalConstraintChild<String>>, ConstraintState>> externalConstraints = new ArrayList<>();
    private final List<ExternalConstraintChild<String>> constraintChildren = new ArrayList<>();
    private ConstraintState externalState = ConstraintState.NONE;
    private ConstraintState internalState = ConstraintState.NONE;

    public ConstrainedTextField(String initVal, int cols) {
        super(initVal, cols);
        setFont(new Font(getFont().getName(), Font.PLAIN, 20));
        addKeyListener(this);
    }

    @Override
    public void accept(ExternalConstraintParent<String> parent, String val) {
        externalState = externalConstraints.stream()
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
        return internalState.ordinal() > externalState.ordinal() ? internalState : externalState;
    }

    @Override
    public void addConstrainedChild(ExternalConstraintChild<String> child) {
        constraintChildren.add(child);
    }

    @Override
    public void addInternalConstraint(BiFunction<String, InternalConstraints<String>, ConstraintState> internalConstraint) {
        internalConstraints.add(internalConstraint);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        internalState = internalConstraints.stream()
                .map(c -> c.apply(getText(), this))
                .reduce((a, b) -> a.ordinal() > b.ordinal() ? a : b).orElse(ConstraintState.NONE);
        constraintChildren.forEach(c -> c.accept(this, getText()));
        applyConstraintState();
    }

    private void applyConstraintState() {
        setEnabled(!currentConstraintState().equals(ConstraintState.DISABLED));
        setBackground(currentConstraintState().equals(ConstraintState.INVALID) ? Color.RED : Color.WHITE);
    }
}
