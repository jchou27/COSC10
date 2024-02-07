/**
 * @Author Jack Chou
 * @Purpose To implement a double ended queue using a double linked list
 */

public class Deque<T> implements SimpleDeque<T> {
    private int size;
    private Element head;
    private Element tail;

    /**
     * Constructor for Element
     */
    private class Element {
        private T data;
        private Element next;
        private Element prev;

        private Element(T data, Element next, Element prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
    /**
     * Constructor for Deque
     */
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }
    /**
     * Add item at the front of the deque
     */
    @Override
    public void addFirst(T item) {
        if (head == null || tail == null) { // if the deque is empty, set the head and tail to the new item
            head = new Element(item, null, null);
            tail = head;
        } else { // if the deque is not empty, set the head to the new item
            Element newHead = new Element(item, head, null);
            head.prev = newHead;
            head = newHead;
        }
        size++; // increment the size of the deque
    }

    public void addLast(T item) {
        if (head == null || tail == null) { // if the deque is empty, set the head and tail to the new item
            tail = new Element(item, null, null);
            head = tail;
        } else { // if the deque is not empty, set the tail to the new item
            Element newTail = new Element(item, null, tail);
            tail.next = newTail;
            tail = newTail;
        }
        size++;
    }

    public T removeFirst() throws Exception {
        if (head == null) { // if the deque is empty, throw an exception
            throw new Exception("Empty Deque");
        }
        T data = head.data;
        head = head.next;
        size--; // decrement the size of the deque
        return data;
    }

    @Override
    public T removeLast() throws Exception {
        if (tail == null) {
            throw new Exception("Empty Deque");
        }
        T data = tail.data;
        tail = tail.prev;
        size--;
        return data;
    }

    @Override
    public T getFirst() throws Exception {
        if (head == null) {
            throw new Exception("Empty Deque");
        }
        return head.data;
    }

    @Override
    public T getLast() throws Exception {
        if (tail == null) {
            throw new Exception("Empty Deque");
        }
        return tail.data;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean contains(T item) {
        Element curr = head;
        while (curr != null) {
            if (curr.data.equals(item)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }
}
