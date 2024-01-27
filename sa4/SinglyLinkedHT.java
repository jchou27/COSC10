import java.util.Iterator;

/**
 * SA-4 provided code
 * A singly-linked list implementation of the SimpleList interface
 * Also implements iterable so SinglyLinked can be used in for-each loops
 *
 * @author Jack Chou
 */
public class SinglyLinkedHT<T> implements SimpleList<T> {
	private Element head;	// front of the linked list
	private Element tail;	// end
	private int size;		// # elements in the list

	/**
	 * The linked elements in the list: each has a piece of data and a next pointer
	 */
	private class Element {
		private T data;
		private Element next;

		private Element(T data, Element next) {
			this.data = data;
			this.next = next;
		}
	}

	public SinglyLinkedHT() {
		// TODO: this is just copied from SinglyLinked; modify as needed
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * Returns the number of elements in the List
	 * @return number of items
	 */
	public int size() {
		// TODO: your code here
		return size;
	}

	/**
	 * Return true if no elements in the List, false otherwise
	 * @return true or false
	 */
	public boolean isEmpty() {
		// TODO: your code here
		return size == 0;

	}

	/**
	 * Helper function, advancing to the nth Element in the list and returning it
	 * (exception if not that many elements)
	 */
	private Element advance(int n) throws Exception {
		Element e = head;
		//safety check for valid index (don't assume caller checked!)
		if (e == null || n < 0 || n >= size) {
			throw new Exception("invalid index");
		}

		// Just follow the next pointers n times
		for (int i = 0; i < n; i++) {
			e = e.next;
		}
		return e;
	}

	/**
	 * Add item at index idx
	 * @param idx index to add item
	 * @param item item to add
	 * @throws Exception invalid index
	 */
	public void add(int idx, T item) throws Exception {        
		// TODO: this is just copied from SinglyLinked; modify as needed
		//safety check for valid index (can add at size index)
		if (idx < 0 || idx > size) {
			throw new Exception("invalid index");
		}
		else if (idx == 0) {
			// Insert at head
			head = new Element(item, head); //new item gets next pointer set to head

			// If tail is null, then we know that the ele added is the only one, therefore head and tail should be pointing at that ele
			if (tail == null) {
				tail = head;
			}
		}
		//If insert index is at the end, then assign tail to the insert element
		else if (idx == size) {
			// Creates a new element given the parameter, next is null bcuz adding to end the list, assign the old last element.next to the new last element
			tail.next = new Element(item, null);
			//
			tail = tail.next;
		}

		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			// Splice it in
			e.next = new Element(item, e.next);	//create new element with next pointing at prior element's next
			//and prior element's next updated to point to this item

		}


		size++;
	}

	/**
	 * Add item at end of List
	 * @param item - item to add to List
	 * @throws Exception
	 */
	public void add(T item) throws Exception {
		//TODO: complete this method efficiently using tail pointer
		if (size == 0){
			throw new Exception("Empty List");
		}
		else{
			tail.next = new Element(item, null);
			tail = tail.next;
		}


		
	}

	/**
	 * Remove and return item at index idx
	 * @param idx index to remove
	 * @throws Exception
	 */
	public T remove(int idx) throws Exception {
		// TODO: this is just copied from SinglyLinked; modify as needed
		T data = null; //data to return
		//safety check for valid index
		if (head == null || idx < 0 || idx >= size) {
			throw new Exception("invalid index");
		}
		else if (idx == 0) {
			data = head.data;
			head = head.next;
		}
		else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			data = e.next.data;
			// Splice it out
			e.next = e.next.next;  //nice!
			if (idx == size - 1) {
				tail = e;
			}
		}

		size--;
		return data;
	}

	/**
	 * Return the item at index idx
	 * @param idx index of item
	 * @return item at index idx
	 * @throws Exception invalid index
	 */
	public T get(int idx) throws Exception {
		//safety check for valid index
		if (idx < 0 || idx >= size) {
			throw new Exception("invalid index");
		}
		Element e = advance(idx);
		return e.data;
	}

	/**
	 * Overwrite the data at index idx
	 * @param idx index to overwrite
	 * @param item data to store at idx
	 * @throws Exception invalid index
	 */
	public void set(int idx, T item) throws Exception {
		//safety check for valid index
		if (idx < 0 || idx >= size) {
			throw new Exception("invalid index");
		}
		Element e = advance(idx);
		e.data = item;
	}

	/**
	 * Appends other to the end of this in constant time, by manipulating head/tail pointers
	 */
	public void append(SinglyLinkedHT<T> other) {
		// TODO: YOUR CODE HERE
		if (tail == null){
			head = other.head;
		}
		else {
			tail.next = other.head;
		}

		if (other.tail != null) {
			tail = other.tail;
		}

		size += other.size;
	}

	/**
	 * Return a String representation of the List
	 * @return box and pointer diagram with end marked with [/]
	 */
	public String toString() {
		String result = "";
		for (Element x = head; x != null; x = x.next) 
			result += x.data + "->"; 
		result += "[/]";

		return result;
	}

	/**
	 * Returns an iterator over the elements in this List.
	 * @return iterator
	 */
	public Iterator<T> iterator() {  //satisfy iterator requirement in SimpleSet interface
		return new ListIterator();
	}

	/**
	 * Iterator class that implements the required functionality to use this List in a for each loop
	 */
	private class ListIterator implements Iterator<T> {
		// Use curr to point to next item in List
		Element curr; //store current value

		public ListIterator() {
			curr = head;
		}

		/**
		 * Does iterator have more items?
		 *
		 * @return true if more items, false otherwise
		 */
		public boolean hasNext() {
			return curr != null;
		}

		/**
		 * Return the next item in the iterator, advance to next item in list
		 *
		 * @return item stored at curr position in the list
		 */
		public T next() {
			if (curr == null) {
				throw new IndexOutOfBoundsException();
			}
			T data = curr.data;
			curr = curr.next;
			return data;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Part 1 -- empty lists");
		SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
		SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();

		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1 + " size: " + list1.size());


		System.out.println("\nPart 2 -- tail pointer");
		SinglyLinkedHT<String> list = new SinglyLinkedHT<String>();
		list.add(0, "a");
		System.out.println(list);

		//Making sure that the head and tail is pointing at the correct element
		System.out.println("Printing head and tail pointers 1");
		System.out.println(list.head.data);
		System.out.println(list.tail.data);

		list.add(1, "b");
		System.out.println(list);
		//Making sure that the head and tail is pointing at the correct element
		System.out.println("Printing head and tail pointers 2");
		System.out.println(list.head.data);
		System.out.println(list.tail.data);

		list.add(0, "c");
		System.out.println(list);
		//Making sure that the head and tail is pointing at the correct element
		System.out.println("Printing head and tail pointers 3");
		System.out.println(list.head.data);
		System.out.println(list.tail.data);

		list.remove(0);
		System.out.println(list);
		//Making sure that the head and tail is pointing at the correct element
		System.out.println("Printing head and tail pointers 4");
		System.out.println(list.head.data);
		System.out.println(list.tail.data);

		list.remove(1);
		System.out.println(list);
		//Making sure that the head and tail is pointing at the correct element
		System.out.println("Printing head and tail pointers 5");
		System.out.println(list.head.data);
		System.out.println(list.tail.data);

		list.remove(0);
		System.out.println(list);

		System.out.println("\nPart 3 -- append");
		list1 = new SinglyLinkedHT<String>();
		list2 = new SinglyLinkedHT<String>();

		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1 + " size: " + list1.size());

		list2.add(0, "a");
		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1 + " size: " + list1.size());

		list1.add(1, "b");
		list1.add("c");
		SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
		System.out.println(list1 + " + " + list3);
		list1.append(list3);
		System.out.println(" = " + list1 + " size: " + list1.size());

		SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
		list4.add(0, "z");
		list4.add(0, "y");
		list4.add(0, "x");
		System.out.println(list1 + " + " + list4);
		list1.append(list4);
		System.out.println(" = " + list1 + " size: " + list1.size());

		list1.remove(4);
		SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
		list5.add(0, "2");
		list5.add(0, "1");
		System.out.println(list1 + " + " + list5);
		list1.append(list5);
		System.out.println(" = " + list1 + " size: " + list1.size());

		System.out.println("\nPart 4 -- for each loops");
		System.out.println("list1 size: " + list1.size());
		for (int i = 0; i < list1.size(); i++){
			System.out.print(list1.get(i) + ", ");
		}
		System.out.println();

		for (String s : list1) {
			System.out.print(s + ", ");
		}
		System.out.println();

	}
}