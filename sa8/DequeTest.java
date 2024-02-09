/**
 * @Author Jack Chou
 * @Purpose To test the Deque implementation
 */
public class DequeTest {
    public static void main(String[] args) throws Exception {
        Deque<String> customerDeque = new Deque<String>();

        //add restaurant customers to the reservation list
        customerDeque.addLast("Jake");
        customerDeque.addLast("Sam");
        customerDeque.addLast("Thomas");
        customerDeque.addLast("Lily");

        //Jake has cancelled his reservation
        if (customerDeque.contains("Jake")){
            customerDeque.removeFirst();
            System.out.println("Jake has cancelled his reservation");
        }

        // A senator decides to book a reservation, given priority treatment
        customerDeque.addFirst("Senator Jackson");

        //Bring customers to the table according to the order they were added
        while (!customerDeque.isEmpty()) {
            try {
                String servedCustomer = customerDeque.removeFirst();
                System.out.println(servedCustomer + " has been brought to his table");
                System.out.println("Remaining Customers that need to be brought to their table: " + customerDeque.size());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //Cause an exception to be thrown by trying to remove a customer from an empty list
        try {
            customerDeque.removeFirst();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
