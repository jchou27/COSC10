import java.security.InvalidKeyException;
import java.util.*;

/**
 * @name Jack Chou
 * @purpose To create an interface for a Binary Search Tree set
 */

public class BSTSet<T extends Comparable<T>> implements SimpleSet<T> {
    private BST<T, Object> root = null;
    private int size = 0;

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

    @Override
    public void clear() {
        root = null;
        size = 0;
    } // Clears the entire tree by setting root to null


    @Override
    public boolean contains(T item) {
        try { // tries to find the item in the tree
            root.find(item);
        } catch (InvalidKeyException e) { // if the item is not found, catch the exception and return false
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    } // returns true if the size is 0

    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new BSTSetIterator(root);
    }

    private class BSTSetIterator implements Iterator<T> {
        private Stack<BST<T, Object>> stack; // Stack to store nodes

        //constructor
        public BSTSetIterator(BST<T, Object> root) {
            stack = new Stack<BST<T, Object>>(); // Initialize new stack
            while (root != null) {
                stack.push(root); // pushes all the nodes in the tree onto the stack
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
