/**
 * @author Jack Chou
 * @Purpose To test the BSTSet class
 */
public class BSTSetTest {
    public static void main(String[] args) {
        BSTSet<Integer> set = new BSTSet<Integer>();
        System.out.println(set.isEmpty()); // true
        System.out.println(set.size()); // 0
        set.add(1);
        set.add(2);
        set.add(3);
        System.out.println(set.isEmpty()); // false
        System.out.println(set.size()); // 3
        System.out.println(set.contains(1)); // true
        System.out.println(set.contains(2)); // true
        System.out.println(set.contains(3)); // true
        System.out.println(set.contains(4)); // false
        set.remove(1);
        System.out.println(set.contains(1)); // false
        System.out.println(set.size()); // 2
        set.clear();
        System.out.println(set.isEmpty()); // true
        System.out.println(set.size()); // 0
    }
}
