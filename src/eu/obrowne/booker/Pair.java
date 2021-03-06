package eu.obrowne.booker;

public class Pair<A, B> {
    public Pair() {}

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first;
    public B second;

    public String toString() {
        return "Pair<" + first + ", " + second + ">";
    }
}
