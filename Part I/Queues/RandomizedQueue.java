import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q = (Item[]) new Object[1];
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // resize queue
    private Item[] resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copy[i] = q[i];
        }
//        q = copy;
        return copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (n == q.length) q = resize(q.length * 2);
        q[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        if (n == q.length / 4) q = resize(q.length / 2);
        int i = StdRandom.uniform(n);
        Item item = q[i];
        q[i] = q[n - 1];
        q[--n] = null;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        int i = StdRandom.uniform(n);
        return q[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int k = n;      // first k elements in copy are for next iteration
        private Item[] copy = resize(n);

        public boolean hasNext() {
            return k > 0;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            int i = StdRandom.uniform(k);
            Item item = copy[i];
            copy[i] = copy[--k];
            copy[k] = null;
            return item;
        }

        public void remove(Item item) {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");

        for (String s1 : rq) {
            for (String s2 : rq) {
                System.out.println(s1 + " " + s2);
            }
        }
    }
}
