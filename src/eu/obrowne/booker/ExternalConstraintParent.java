package eu.obrowne.booker;

public interface ExternalConstraintParent<T> {
    void addConstrainedChild(ExternalConstraintChild<T> constrainable);
}
