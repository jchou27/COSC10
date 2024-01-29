import java.util.ArrayList;
import java.util.List;

/**
 * Generic binary tree, storing data of generic type in each node
 *
 * @author Jack Chou
 * @source Tim Pierson, Winter 2024, based on binary tree from prior terms
 */

public class BinaryTreeRecitation<E> {
	private BinaryTreeRecitation<E> left, right;	// children; can be null
	E data;

	/**
	 * Constructor leaf node -- left and right are null
	 */
	public BinaryTreeRecitation(E data) {
		this.data = data; this.left = null; this.right = null;
	}

	/**
	 * Constructor inner node
	 */
	public BinaryTreeRecitation(E data, BinaryTreeRecitation<E> left, BinaryTreeRecitation<E> right) {
		this.data = data; this.left = left; this.right = right;
	}


	/**
	 * helper methods
	 */
	public boolean isInner() {
		return left != null || right != null;
	}
	public boolean isLeaf() {
		return left == null && right == null;
	}
	public boolean isFull() { return left != null && right !=null;}
	public boolean hasLeft() {
		return left != null;
	}
	public boolean hasRight() {
		return right != null;
	}

	public BinaryTreeRecitation<E> getLeft() {
		return left;
	}
	public BinaryTreeRecitation<E> getRight() {
		return right;
	}
	public E getData() {
		return data;
	}

	public void setData(E data) {this.data = data;}
	public void setLeft(BinaryTreeRecitation<E> child) {
		left = child;
	}
	public void setRight(BinaryTreeRecitation<E> child) {
		right = child;
	}

	/**
	 * Number of nodes (inner and leaf) in tree
	 */
	public int size() {
		int num = 1;
		if (hasLeft()) num += left.size();
		if (hasRight()) num += right.size();
		return num;
	}

	/**
	 * Longest length to a leaf node from here
	 */
	public int height() {
		if (isLeaf()) return 0;
		int h = 0;
		if (hasLeft()) h = Math.max(h, left.height());
		if (hasRight()) h = Math.max(h, right.height());
		return h+1;		// inner: one higher than highest child
	}

	/**
	 * Same structure and data
	 * @param t2 compare with this tree
	 * @return true if this tree and t2 have the have structure and same data in each node, false otherwise
	 */
	public boolean equals(BinaryTreeRecitation<E> t2) {
		if (hasLeft() != t2.hasLeft() || hasRight() != t2.hasRight()) return false;
		if (!data.equals(t2.data)) return false;
		if (hasLeft() && !left.equals(t2.left)) return false;
		if (hasRight() && !right.equals(t2.right)) return false;
		return true;
	}

	/**
	 * Check to see if value is in the tree
	 * @param value value to seek
	 * @return true if value in tree, false otherwise
	 */
	public boolean contains(E value) {
		//see if this node's data is equal to value
		if (data.equals(value)) {
			return true;
		}
		else {
			//see if left or right child has value
			boolean leftResult = false;
			boolean rightResult = false;
			if (hasLeft()) leftResult = left.contains(value);
			if (hasRight()) rightResult = right.contains(value);
			return leftResult || rightResult; //true if left or right is true
		}
	}

	/**
	 * Leaves, in order from left to right
	 */
	public List<E> fringe() {
		List<E> f = new ArrayList<E>();
		addToFringe(f);
		return f;
	}

	/**
	 * Helper for fringe, adding fringe data to the list
	 */
	private void addToFringe(List<E> fringe) {
		if (isLeaf()) {
			fringe.add(data);
		}
		else {
			if (hasLeft()) left.addToFringe(fringe);
			if (hasRight()) right.addToFringe(fringe);
		}
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
		String res = indent + data + "\n";
		if (hasLeft()) res += left.toStringHelper(childIndent + "├── ", childIndent + "│   ");
		if (hasRight()) res += right.toStringHelper(childIndent + "└── ", childIndent + "    ");
		return res;
	}

	/**
	 * Tree traversals
	 */
	public void preOrderTraversal() {
		System.out.println(data);
		if (hasLeft()) left.preOrderTraversal();
		if (hasRight()) right.preOrderTraversal();
	}

	public void inOrderTraversal() {
		if (hasLeft()) left.preOrderTraversal();
		System.out.println(data);
		if (hasRight()) right.preOrderTraversal();
	}

	public void postOrderTraversal() {
		if (hasLeft()) left.preOrderTraversal();
		if (hasRight()) right.preOrderTraversal();
		System.out.println(data);
	}

// produces a copy of the tree down with the same data elements down to its given depth
	public BinaryTreeRecitation<E> copyToDepth(int d){
		if (d == 0) {return new BinaryTreeRecitation(data);}
		BinaryTreeRecitation<E> leftCopy = null;
		BinaryTreeRecitation<E> rightCopy = null;
		if (hasLeft()){leftCopy = left.copyToDepth(d-1);}
		if (hasRight()) {rightCopy = right.copyToDepth(d-1);}
		return new BinaryTreeRecitation(data, leftCopy, rightCopy);
	}


	public static void main(String[] args) throws Exception {
		//manually build a tree
		BinaryTreeRecitation<String> root = new BinaryTreeRecitation<String>("G");
		root.left = new BinaryTreeRecitation<String>("B");
		root.right = new BinaryTreeRecitation<String>("F");
		BinaryTreeRecitation<String> temp = root.left;
		temp.left = new BinaryTreeRecitation<String>("A");
		temp.right = new BinaryTreeRecitation<String>("C");
		temp = root.right;
		temp.left = new BinaryTreeRecitation<String>("D");
		temp.right = new BinaryTreeRecitation<String>("E");
		System.out.println("root");
		System.out.println(root);
		
		System.out.println("Fringe");
		System.out.println(root.fringe());

		System.out.println("Contains A: " + root.contains("A"));
		System.out.println("Contains Z: " + root.contains("Z"));

		//build an identical second tree
		BinaryTreeRecitation<String> root2 = new BinaryTreeRecitation<String>("G");
		root2.left = new BinaryTreeRecitation<String>("B");
		root2.right = new BinaryTreeRecitation<String>("F");
		BinaryTreeRecitation<String> temp2 = root2.left;
		temp2.left = new BinaryTreeRecitation<String>("A");
		temp2.right = new BinaryTreeRecitation<String>("C");
		temp2 = root2.right;
		temp2.left = new BinaryTreeRecitation<String>("D");
		temp2.right = new BinaryTreeRecitation<String>("E");
		System.out.println("root");
		System.out.println(root2);

		//check equals
		System.out.println("root and root2 equal: " + root.equals(root2));
		System.out.println("Updating root2 to have data Z");
		root2.setData("Z");
		System.out.println(root2);
		System.out.println("root and root2 equal: " + root.equals(root2));

		System.out.println("Copy to depth:");
		System.out.println(root2.copyToDepth(2));



	}
}
