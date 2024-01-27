/**
 * @author Jack Chou
 * @Purpose To create a Graduate subclass with the implamentation of year and department
 */

public class Graduate extends Attendee {
    /**
     * declare instance vars with privacy protection
     */
    protected Integer graduationYear;
    protected String dept;

    /**
     * Constructor
     * Uses the super class to inherit Attendee constructor and then assign parameters to instance vars
     * @param name
     * @param rsvp
     */

    public Graduate(String name, boolean rsvp, int year, String dept) {
        super(name, rsvp);
        graduationYear = year;
        this.dept = dept;
    }



    /**
     * getters
     */

    public int getYear(){
        return graduationYear;
    }

    public String getDept(){
        return dept;
    }

    /**
     * Setters
     */
    public void setGraduationYear(int year){
        /**
         * can only provide parameters between the year 0 and 99 given the last two digits of the number
         */
        if (graduationYear > 10 && graduationYear < 99) {
            graduationYear = year;
        }
        else {
            System.out.println("Invalid Year");
        }
    }

    public void setDept(String dept){
        this.dept = dept;
    }

    /**
     * Intend to override the toString method within the superclass. "super." inherits the toString method of the base/super class and also adds the additional
     * year and department
     * @return
     */
    @Override
    public String toString() {
         String s = super.toString() + " ";
         s += "'" + graduationYear;
         s += " " + dept;
         return s;
    }
}
