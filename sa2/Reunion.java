/**
 * @author Jack Chou
 * @purpose To create a drive class that has a list of all the attendees names and rsvp status
 *
 */

public class Reunion{
    /**
     * declare instance variables and creates an array
     */
    protected Attendee[] reunionGroup;

    /**
     * Constructor where we reference the array the reunionGroup address and how many memory spaces it needs
     * @param num
     */
    public Reunion(int num) {
        reunionGroup = new Attendee[num];
    }

    /**
     * A method addAttendee that iterates through the entire array and adds information of the student to the array
     * @param student
     */

    public void addAttendee(Attendee student){
        for (int i = 0; i < reunionGroup.length; i++){
            if (reunionGroup[i] == null) {
                reunionGroup[i] = student;
                System.out.println("Added " + reunionGroup[i].getName() + ", rsvp: " + reunionGroup[i].getRsvp());
                return;
            }
        }
        System.out.println("Unable to accommodate " + student + ", max attendees is " + reunionGroup.length);
    }

    /**
     * Create a rsvp method that iterates through the entire array and sees if any name within the array matches the name given in the parameter
     * If true, then change the rsvp reply from the stored boolean to the parameter boolean given by user input
     * If false, then notify the user that the parameter name was not found within the array
     * @param name
     * @param reply
     */
    public void rsvp(String name, boolean reply){
        for (int j = 0; j < reunionGroup.length; j++){
            String s = reunionGroup[j].getName();
            if (s.equals(name)) {
                reunionGroup[j].setRsvp(reply);
                System.out.println("Set RSVP to " + reply + " for " + name);
                return;
            }
        }
        System.out.println(name + " not found");
    }

    /**
     * Create a toString method that lists out the RSVP list.
     * First created two empty strings, then stored all of those who rsvp into one string while those who didn't in another
     * Then, set a string var to save both list and return the string var to the main method
     * @return
     */

    public String toString() {
        String yRSVP = "";
        String nRSVP = "";
        for(Attendee x : reunionGroup){
            if(x.getRsvp()){
                yRSVP += "\t" + x + "\n";
            }
            else{
                nRSVP += "\t" + x + "\n";
            }
        }
        String s = "Reunion attendees that have RSVP:" + "\n";
        s += yRSVP + "\n";
        s += "Reunion attendees that have NOT RSVP:" + "\n";
        s += nRSVP + "\n";
        return s;
    }

    public static void main(String[] args) {
        Reunion r = new Reunion(5);
        r.addAttendee(new Attendee("Alice", true));
        r.addAttendee(new Attendee("Bob", false));
        r.addAttendee(new Graduate("Charlie", true, 22, "Government"));
        r.addAttendee(new Graduate("Denise", false, 21, "Econ"));
        r.addAttendee(new Graduate("Elvis", true, 23, "Computer Science"));
        r.addAttendee(new Graduate("Falcon", false, 26, "Biology"));
        System.out.println(r);
        System.out.println();

        System.out.println("Update rsvp status");
        r.rsvp("George", false);  //print not found
        r.rsvp("Bob", true);  //update
        System.out.println(r);
    }
}
