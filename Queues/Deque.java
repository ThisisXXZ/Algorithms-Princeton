import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    // head & tail are both real nodes
    private int n;
    private Node<Item> head;
    private Node<Item> tail;

    private class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> last;
    }

    // construct an empty deque
    public Deque() {
        n = 0;
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> oldHead = head;
        head = new Node<Item>();
        head.item = item;
        head.next = oldHead;
        head.last = null;
        if (oldHead != null)    oldHead.last = head;
        if (isEmpty())          tail = head;
        ++n;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> oldTail = tail;
        tail = new Node<Item>();
        tail.item = item;
        tail.last = oldTail;
        tail.next = null;
        if (oldTail != null)    oldTail.next = tail;
        if (isEmpty())          head = tail;
        ++n;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item result = head.item;
        head = head.next;
        if (head != null)   head.last = null;
        --n;
        if (isEmpty())      tail = null;
        return result;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item result = tail.item;
        tail = tail.last;
        if (tail != null)   tail.next = null;
        --n;
        if (isEmpty())      head = null;
        return result;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> itr;

        public LinkedIterator() {
            itr = head;
        }
        public boolean hasNext() {
            return itr != null;
        }
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item result = itr.item;
            itr = itr.next;
            return result;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    } 
    
    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();

        
        String[] nameList = {"Ren", "Ryuji", "Morugana", "Ann", 
                            "Yusuke", "Makoto", "Futaba", "Haru"};
        String[] newNameList = {"Akeji", "Kasumi", "Sophie", "Zenkiji"};
        
        if (dq.isEmpty()) {
            StdOut.println("The deque is originally empty.\n");
        }
        
        for (int i = 0; i < newNameList.length; ++i) {
            dq.addLast(newNameList[i]);
        }

        dq.removeFirst();
        dq.removeFirst();

        for (int i = 0; i < nameList.length / 2; ++i) {
            dq.addFirst(nameList[i]);
        }

        dq.removeLast();
        dq.removeLast();

        for (int i = nameList.length / 2; i < nameList.length; ++i) {
            dq.addLast(nameList[i]);
        }

        StdOut.println("There are " + dq.size() + " names in queue.\n");
        for (String s : dq) {
            StdOut.println(s);
        }

        while (!dq.isEmpty())   dq.removeFirst();
        
    }
    

}