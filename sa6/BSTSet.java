import java.security.InvalidKeyException;
import java.util.*;

/**
 * @name Jack Chou
 * @purpose To create an interface for a Binary Search Tree set
 */

public class BSTSet<T extends Comparable<T>> implements SimpleSet<T> {
    private BST<T, Object> root = null;
    private int size = 0;

    /**
     * Add an item to the Set if it is not already present
     * @param item
     * @return
     */
    @Override
    public boolean add(T item) {
        if (root == null) { // if the tree is empty, set the root to the new item
            root = new BST<T,Object>(item, null);
            size++;
        }
        if (contains(item)) { // if the item is already in the tree, return false
            return false;
        }
        root.insert(item, null);
        size++;
        return true;
    }

    /**
     * Remove an item from the Set
     * @param item
     * @return
     */
    @Override
    public boolean remove(T item) {
        try { // tries to remove the item from the tree
            root = root.delete(item);
        } catch (InvalidKeyException e) { // if the item is not found, catch the exception and return false
            return false; //
        }
        size--;
        return true;
    }

    /**
     * Clears the entire set by setting root to null
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Return true if the item is in the Set, false otherwise
      */
    @Override
    public boolean contains(T item) {
        try { // tries to find the item in the tree
            root.find(item);
        } catch (InvalidKeyException e) { // if the item is not found, catch the exception and return false
            return false;
        }
        return true;
    }

    /**
     * Return true if the Set is empty, false otherwise
     * @return
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns # elements in the set
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator to loop over the Set
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return new BSTSetIterator(root);
    }

    private class BSTSetIterator implements Iterator<T> {
        private Stack<BST<T, Object>> stack; // Stack to store nodes

        //constructor
        public BSTSetIterator(BST<T, Object> root) {
            stack = new Stack<BST<T, Object>>(); // Initialize new stack
            if (root != null) {
                stack.push(root); // pushes root node in the tree onto the stack
            }
        }

        // Checks if there are more elements to iterate over
        public boolean hasNext() {
            return !stack.isEmpty(); // returns true if the stack is not empty
        }

        // Returns the next element in the iteration
        public T next(){
            BST<T, Object> node = stack.pop(); // pops the top node off the stack
            //pushes the right and left children of the node onto the stack
            if (node.getRight() != null) {
                stack.push(node.getRight());
            }
            if (node.getLeft() != null) {
                stack.push(node.getLeft());
            }
            //returns the key of the node
            return node.getKey();
        }
    }
}
