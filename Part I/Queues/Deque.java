import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int cnt = 0;

    // construct an empty deque
    public Deque() {
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return cnt == 0;
    }

    // return the number of items on the deque
    public int size() {
        return cnt;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;

        if (isEmpty()) {
            first.next = null;
            last = first;
        } else {
            first.next = oldFirst;
            oldFirst.prev = first;
        }

        cnt++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;

        if (isEmpty()) {
            last.prev = null;
            first = last;
        } else {
            last.prev = oldLast;
            oldLast.next = last;
        }

        cnt++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = first.item;
        first = first.next;
        cnt--;
        if (isEmpty()) last = null;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item item = last.item;
        last = last.prev;
        cnt--;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove(Item item) {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();
        dq.addFirst("a");
        dq.addLast("b");
        dq.removeLast();
        dq.removeFirst();
        dq.addLast("c");
        dq.addFirst("q");
        for (String s : dq) System.out.println(s);
    }
}
