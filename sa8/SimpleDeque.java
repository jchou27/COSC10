/**
 * A basic interface for a generic deque (double-ended queue)
 * @author Ethan Chen, primary author
 * @author Tim Pierson, Dartmouth CS10, Winter 2024
 */
public interface SimpleDeque<T> {

    /**
     * Add item at the front of the deque
     */
    public void addFirst(T item);

    /**
     * Add item at the end of the deque
     */
    public void addLast(T item);

    /**
     * Removes and returns the item at the front of the deque
     * throws exception if deque is empty
     */
    public T removeFirst() throws Exception;

    /**
     * Removes and returns the item at the end of the deque
     * throws exception if deque is empty
     */
    public T removeLast() throws Exception;

    /**
     * Returns the item at the front of the deque
     * throws exception if deque is empty
     */
    public T getFirst() throws Exception;

    /**
     * Returns the item at the end of the deque
     * throws exception if deque is empty
     */
    public T getLast() throws Exception;

    /**
     * Clears the deque
     */
    public void clear();

    /**
     * Returns true if the deque contains the item, false otherwise
     */
    public boolean contains(T item);

    /**
     * Returns true if the deque is empty, false otherwise
     */
    public boolean isEmpty(); 

    /**
     * Returns # elements in the deque
     */
    public int size();
}
