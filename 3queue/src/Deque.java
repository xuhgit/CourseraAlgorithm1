import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int numItems;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        numItems = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null && last == null;
    }

    // return the number of items on the deque
    public int size() {
        return numItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (last == null) last = first;
        else oldFirst.prev = first;
        numItems += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (first == null) first = last;
        else {
            oldLast.next = last;
            last.prev = oldLast;
        }
        numItems += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) throw new java.util.NoSuchElementException();
        Item item = first.item;
        Node oldFirst = first;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.prev = null;
        }
        oldFirst.next = null;
        oldFirst.prev = null;
        numItems -= 1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) throw new java.util.NoSuchElementException();
        Item item = last.item;
        Node oldLast = last;
        last = last.prev;
        if (last == null) first = null;
        else last.next = null;
        oldLast.prev = null;

        numItems -= 1;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (current == null) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
     }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> q = new Deque<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                q.addLast(item); // queue
//                q.addFirst(item); // stack
            }
            else if (!q.isEmpty()) StdOut.print(q.removeFirst() + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}