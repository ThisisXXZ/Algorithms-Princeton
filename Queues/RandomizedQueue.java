import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INIT_CAPACITY = 8;
    
    // head is real node, tail is virtual node
    private Item[] que;
    private int n;
    private int head;
    private int tail;

    // construct an empty randomized queue
    public RandomizedQueue() {
        n = 0;
        head = 0;
        tail = 0;
        que = (Item[]) new Object[INIT_CAPACITY];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; ++i)
            copy[i] = que[(head + i) % que.length];
        que = copy;
        head = 0;
        tail = n;
    }

    private void swap(int x, int y) {
        Item t = que[x];
        que[x] = que[y];
        que[y] = t;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        que[tail++] = item;
        if (tail == que.length) tail = 0;
        ++n;
        if (n == que.length)
            resize(2 * que.length);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int rnd = StdRandom.uniformInt(n);
        swap(head, (head + rnd) % que.length);
        Item result = que[head];
        que[head++] = null;
        if (head == que.length) head = 0;
        --n;
        if (n > 0 && 4 * n <= que.length)
            resize(que.length / 2);
        return result;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int rnd = StdRandom.uniformInt(n);
        return que[(head + rnd) % que.length];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] rndOrder;
        private int i;

        public RandomIterator() {
            i = 0;
            rndOrder = StdRandom.permutation(n);
        }
        public boolean hasNext() {
            return i < n;
        }
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item result = que[(head + rndOrder[i]) % que.length];
            ++i;
            return result;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        String[] nameList = {"Ren", "Ryuji", "Morugana", "Ann", 
                            "Yusuke", "Makoto", "Futaba", "Haru"};
        
        if (rq.isEmpty()) {
            StdOut.println("The randomized queue is originally empty.\n");
        }
        
        rq.enqueue(nameList[0]);
        rq.dequeue();
        rq.enqueue(nameList[7]);
        rq.dequeue();
        for (int i = 0; i < nameList.length; ++i)
            rq.enqueue(nameList[i]);
        
        StdOut.println("There are " + rq.size() + " names in queue.\n");
        for (int i = 1; i <= 8; ++i) {
            StdOut.println("Sample #" + i + ":");
            StdOut.println(rq.sample());
        }
        
        StdOut.println("Random iterator tests.");
        for (String s1 : rq) {
            StdOut.println(s1);
            StdOut.println("----------");
            for (String s2 : rq) {
                StdOut.println(s2);
            }
            StdOut.println("----------");
        }
        
    }

}