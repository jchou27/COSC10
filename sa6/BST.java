import java.security.InvalidKeyException;

/**
 * Generic binary search tree
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2024, based on prior term code
 */

public class BST<K extends Comparable<K>,V> {
	private K key;
	private V value;
	private BST<K,V> left, right;

	/**
	 * Constructs leaf node -- left and right are null
	 */
	public BST(K key, V value) {
		this.key = key; this.value = value;
	}

	/**
	 * Constructs inner node
	 */
	public BST(K key, V value, BST<K,V> left, BST<K,V> right) {
		this.key = key; this.value = value;
		this.left = left; this.right = right;
	}

	/**
	 * Getters
	 */
	public K getKey() { return key; }
	public V getValue() { return value; }
	public BST<K,V> getRight() { return right; }
	public BST<K,V> getLeft() { return left; }

	/**
	 * helper methods
	 */
	public boolean isLeaf() { return left == null && right == null; }
	public boolean hasLeft() { return left != null; }
	public boolean hasRight() { return right != null; }

	/**
	 * Returns the value associated with the search key, or throws an exception if not in BST
	 */
	public V find(K search) throws InvalidKeyException {
		int compare = search.compareTo(key);  //compare search with this node's key using search's compareTo() method
		if (compare == 0) return value; //found it
		if (compare < 0 && hasLeft()) return left.find(search); //search key < this node's key, go left, return result
		if (compare > 0 && hasRight()) return right.find(search); //search key > this node's key, go right, return result
		throw new InvalidKeyException(search.toString()); //can't go anywhere and didn't find key, throw exception
	}

	/**
	 * Smallest key in the tree, recursive version
	 */
	public K min() {
		if (left != null) return left.min();
		return key;
	}

	/**
	 * Smallest key in the tree, iterative version
	 */
	public K minIter() {
		BST<K,V> curr = this;
		while (curr.left != null) curr = curr.left;
		return curr.key;
	}

	/**
	 * Inserts the key & value into the tree (replacing old key if equal)
	 */
	public void insert(K key, V value) {
		int compare = key.compareTo(this.key);
		if (compare == 0) {
			// replace
			this.value = value;
		}
		else if (compare < 0) {
			// insert on left (new leaf if no left)
			if (hasLeft()) left.insert(key, value);
			else left = new BST<K,V>(key, value);
		}
		else if (compare > 0) {
			// insert on right (new leaf if no right)
			if (hasRight()) right.insert(key, value);
			else right = new BST<K,V>(key, value);
		}
	}

	/**
	 * Deletes the key and returns the modified tree, which might not be the same object as the original tree
	 * Thus must afterwards just use the returned one
	 */
	public BST<K,V> delete(K search) throws InvalidKeyException {
		int compare = search.compareTo(key);
		if (compare == 0) {
			// Easy cases: 0 or 1 child -- return other
			if (!hasLeft()) return right;  //no left child, return right, could be null if no right child, but that is ok
			if (!hasRight()) return left; //has left, but no right, return left
			// If both children are there, delete and substitute the successor (smallest on right)
			// Find successor
			BST<K,V> successor = right;
			while (successor.hasLeft()) successor = successor.left;
			// Delete it
			right = right.delete(successor.key);
			// And take its key & value
			this.key = successor.key;
			this.value = successor.value;
			return this;
		}
		else if (compare < 0 && hasLeft()) {
			left = left.delete(search);
			return this; 
		}
		else if (compare > 0 && hasRight()) {
			right = right.delete(search);
			return this;
		}
		throw new InvalidKeyException(search.toString());
	}


	/**
	 * Returns a string representation of the tree
	 */
	public String toString() {
		return toStringHelper("", "");
	}

	/**
	 * Recursively constructs a String representation of the tree from this node,
	 * starting with the given indentation and indenting further going down the tree
	 * formatting inspired by: https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java
	 */
	public String toStringHelper(String indent, String childIndent) {
		String res = indent + key + ":" + value + "\n";
		if (hasLeft()) {
			//not null left
			res += left.toStringHelper(childIndent + "|-- ", childIndent + "|   ");
		}
		else if (!isLeaf()){
			//null left child
			res += childIndent + "|-- null\n";
		}
		if (hasRight()) {
			//not null right
			res += right.toStringHelper(childIndent + "|-- ", childIndent + "    ");
		}
		else if (!isLeaf()){
			//null right child
			res += childIndent + "|-- null\n";
		}
		return res;
	}

	/**
	 * Tree traversals
	 */
	public void preOrderTraversal() {
		System.out.print(key +":" + value + " ");
		if (hasLeft()) left.preOrderTraversal();
		if (hasRight()) right.preOrderTraversal();
	}

	public void inOrderTraversal() {
		if (hasLeft()) left.inOrderTraversal();
		System.out.print(key + ":" + value + " ");
		if (hasRight()) right.inOrderTraversal();
	}

	public void postOrderTraversal() {
		if (hasLeft()) left.postOrderTraversal();
		if (hasRight()) right.postOrderTraversal();
		System.out.print(key + ":" + value + " ");
	}


	/**
	 * Some tree testing
	 */
	public static void main(String[] args) throws Exception {
		BST<String,Integer> root = new BST<String, Integer>("D",1);
		root.insert("B",2);
		root.insert("A",3);
		root.insert("C",4);
		root.insert("F",5);
		root.insert("M",6);
		root.insert("E",7);
		root.insert("H",8);
		root.insert("J",9);
		root.insert("P",10);
		System.out.println(root);
		System.out.println("Delete A");
		root = root.delete("A");
		System.out.println(root);
		System.out.println("Delete B");
		root = root.delete("B");
		System.out.println(root);
		System.out.println("Delete F");
		root = root.delete("F");
		System.out.println(root);

		//traversals
		System.out.println("In-order traversal");
		root.inOrderTraversal();
		System.out.println("\nPre-order traversal");
		root.preOrderTraversal();
		System.out.println("\nPost-order traversal");
		root.postOrderTraversal();
	}
}
