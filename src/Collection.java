public interface Collection<T> {
    void add(T value);
    boolean contains(T value);
    void remove(T value);
    int size();
    boolean isEmpty();
}
