package eu.obrowne.booker;

import java.util.function.BiFunction;

public interface InternalConstraints<T> {
    void addInternalConstraint(BiFunction<T, InternalConstraints<T>, ConstraintState> internalConstraint);
}
