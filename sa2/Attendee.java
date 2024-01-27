/**
 * @author Jack Chou
 * @Purpose: To represent one person at the reunion, base class
 */

public class Attendee {
    protected String name;
    protected Boolean rsvp;

    /**
     * Constructor
     *
     * @param name
     */
    public Attendee(String name, boolean rsvp){
        this.name = name;
        this.rsvp = rsvp;
    }

    /**
     * Getters
     */

    public String getName(){
        return name;
    }
    public boolean getRsvp(){
        return rsvp;
    }

    /**
     * Setters
     */
    public void setName(String name){
        this.name = name;
    }
    public void setRsvp(boolean attend){
        rsvp = attend;
    }

    public String toString() {
        String s = name;
        return s;
    }
}
