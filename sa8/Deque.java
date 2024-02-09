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
     * RUNTIME COMPLEXITY: O(1)
     * Updating pointers and returning data a removed node
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

    /**
     * Add item at the end of the deque
     * RUNTIME COMPLEXITY: O(1)
     * @param item
     */
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

    /**
     * Removes and returns the item at the front of the deque
     * RUNTIME COMPLEXITY: O(1)
     * @return
     * @throws Exception
     */
    public T removeFirst() throws Exception {
        if (head == null) { // if the deque is empty, throw an exception
            throw new Exception("Empty Deque");
        }
        T data = head.data;
        head = head.next; // set the head to the next element
        size--; // decrement the size of the deque
        return data;
    }

    /**
     * Removes and returns the item at the end of the deque
     * RUNTIME COMPLEXITY: O(1)
     * @return
     * @throws Exception
     */
    @Override
    public T removeLast() throws Exception {
        if (tail == null) { // if the deque is empty, throw an exception
            throw new Exception("Empty Deque");
        }
        T data = tail.data;
        tail = tail.prev; // set the tail to the previous element
        size--; // decrement the size of the deque
        return data;
    }

    /**
     * Returns the item at the front of the deque
     * RUNTIME COMPLEXITY: O(1)
     * @return
     * @throws Exception
     */
    @Override
    public T getFirst() throws Exception {
        if (head == null) {
            throw new Exception("Empty Deque");
        }
        return head.data;
    }

    /**
     * Returns the item at the end of the deque
     * RUNTIME COMPLEXITY: O(1)
     * @return
     * @throws Exception
     */
    @Override
    public T getLast() throws Exception {
        if (tail == null) {
            throw new Exception("Empty Deque");
        }
        return tail.data;
    }

    /**
     * Clears the deque
     * RUNTIME COMPLEXITY: O(1)
     */
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Returns true if the deque contains the item, false otherwise
     * RUNTIME COMPLEXITY: O(n)
     * Worst case traverse through the entire deque
     * @param item
     * @return
     */
    @Override
    public boolean contains(T item) {
        Element curr = head; // start at the head of the deque
        while (curr != null) { // iterate through the entire deque
            if (curr.data.equals(item)) { // if the current element is equal to the item, return true
                return true;
            }
            curr = curr.next; // move to the next element
        }
        return false;
    }

    /**
     * Returns true if the deque is empty, false otherwise
     * RUNTIME COMPLEXITY: O(1)
     * @return
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns # elements in the deque
     * RUNTIME COMPLEXITY: O(1)
     * @return
     */
    @Override
    public int size() {
        return size;
    }
}
