package eu.obrowne.booker;

import java.util.function.BiFunction;

public interface ExternalConstraintChild<T> {
    void accept(ExternalConstraintParent<T> parent, T val);
    void addExternalConstraint(BiFunction<T, Pair<ExternalConstraintParent<T>, ExternalConstraintChild<T>>, ConstraintState> constraint);
    ConstraintState currentConstraintState();
}
