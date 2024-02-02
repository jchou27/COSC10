import java.util.Iterator;

/**
 * A basic interface for a generic set
 * @author Ethan Chen, primary author
 * @author Tim Pierson, Dartmouth CS10, provided for Winter 2024
 */
public interface SimpleSet<T> extends Iterable<T> {

    /**
     * Add an item to the Set if it is not already present
     * Return true if the item is added, false otherwise
     */
    public boolean add(T item);

    /**
     * Remove an item from the Set
     * Return true if the item is removed, false otherwise
     */
    public boolean remove(T item);

    /**
     * Returns a Set containing no items
     */
    public void clear();

    /**
     * Return true if the item is in the Set, false otherwise
     */
    public boolean contains(T item);

    /**
     * Return true if the Set is empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Returns # elements in the set
     */
    public int size();

    /**
     * Returns an iterator to loop over the Set
     */
    public Iterator<T> iterator();
}
