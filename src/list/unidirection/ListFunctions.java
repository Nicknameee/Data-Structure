package list.unidirection;

import java.util.stream.Stream;

public interface ListFunctions<T> {
    void add(T node);
    void add(int position, T node);
    T get(int index);
    void remove(int index);
    void remove(T node);
    int size();
    Stream<List.Node<T>> stream();
    void print();
    void clear();
    boolean isEmpty();
    boolean contains(T node);
}
