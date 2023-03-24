package list.unidirection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class List<T> implements ListFunctions<T> {
    private Node<T> head;
    private final AtomicInteger size;

    List() {
        this.size = new AtomicInteger(0);
    }
    @Override
    public void add(T node) {
        synchronized (this) {
            this.add(new Node<>(node));
        }
    }

    @Override
    public void add(int position, T node) {
        synchronized (this) {
            this.add(position, new Node<>(node));
        }
    }

    private void add(Node<T> node) {
        if (this.head == null) {
            this.head = node;
        } else {
            Node<T> temp = this.head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
        }
        this.size.incrementAndGet();
    }

    private void add(int position, Node<T> node) {
        if (position < 0 || position > size.get()) {
            throw new RuntimeException("Index out of bounds exception for length " + this.size.get());
        }
        if (position == 0) {
            Node<T> temp = this.head;
            this.head = node;
            this.head.next = temp;
            return;
        }
        Node<T> temp = this.head;
        while (position-- > 0) {
            temp = temp.next;
        }
        Node<T> pendingLink = temp.next;
        temp.next = node;
        node.next = pendingLink;
        this.size.incrementAndGet();
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size.get()) {
            throw new RuntimeException("Index out of bounds exception for length " + this.size.get());
        }
        synchronized (this) {
            Node<T> temp = this.head;
            while (index-- > 0) {
                temp = temp.next;
            }
            return temp.getValue();
        }
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index > size.get()) {
            throw new RuntimeException("Index out of bounds exception for length " + this.size.get());
        }
        synchronized (this) {
            if (index == 0) {
                this.head = this.head.next;
                this.size.decrementAndGet();
                return;
            }
            Node<T> temp = this.head;
            while (index-- > 1) {
                temp = temp.next;
            }
            temp.next = temp.next.next;
            this.size.decrementAndGet();
        }
    }

    @Override
    public void remove(T node) {
        synchronized (this) {
            if (node == this.head.getValue()) {
                this.head = this.head.next;
            } else {
                Node<T> temp = this.head;
                while (temp.next != null && !temp.next.getValue().equals(node)) {
                    temp = temp.next;
                }
                if (temp.next == null) {
                    return;
                }
                temp.next = temp.next.next;
            }
            this.size.decrementAndGet();
        }
    }

    @Override
    public int size() {
        return this.size.get();
    }

    @Override
    public Stream<Node<T>> stream() {
        synchronized (this) {
            Stream.Builder<Node<T>> builder = Stream.builder();
            Node<T> temp = this.head;
            while (temp != null) {
                builder.add(temp);
                temp = temp.next;
            }
            return builder.build();
        }
    }

    @Override
    public void print() {
        synchronized (this) {
            Node<T> temp = this.head;
            System.out.print("[");
            while (temp != null) {
                System.out.printf("%s", temp.getValue());
                if (temp.next != null) {
                    System.out.print(",");
                }
                temp = temp.next;
            }
            System.out.println("]");
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size.get() == 0;
    }

    @Override
    public void clear() {
        synchronized (this) {
            this.head = null;
            this.size.set(0);
        }
    }

    @Override
    public boolean contains(T node) {
        synchronized (this) {
            Node<T> temp = this.head;
            while (temp != null && !temp.getValue().equals(node)) {
                temp = temp.next;
            }
            return temp != null;
        }
    }

    public static final class Node<T> {
        private T value;

        private Node<T> next;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node(T value) {
            this.value = value;
        }
    }
}
