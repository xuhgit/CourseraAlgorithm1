import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a; // deque of items
    private int n; // number of items

    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int length) {
        Item[] arr = (Item[]) new Object[length];
        for (int i = 0; i < n; i++) {
            arr[i] = a[i];
        }
        a = arr;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniformInt(n);
        Item item = a[i];
        a[i] = a[--n];
        a[n] = null;
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int i = StdRandom.uniformInt(n);
        return a[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] indices; // cannot new here
        private int copySize;

        public RandomIterator() {
            indices = new int[n];
            copySize = n;
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
        }

        public boolean hasNext() { return copySize > 0; }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            int ri = StdRandom.uniformInt(copySize);
            Item item = a[indices[ri]];
            indices[ri] = indices[--copySize];
//            indices[copySize] = Integer.parseInt(null);
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                q.enqueue(item);
            else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }

}