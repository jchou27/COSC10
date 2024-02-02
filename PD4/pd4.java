/**
 * @author Jack Chou
 * @purpose To create a map that keeps track of student and their courses
 */

import java.util.*;
public class pd4 {
    public static void main(String[] args) {
        // Create a map that keeps track of students and their courses
        Map<String, List<String>> studentCourses = new HashMap<String, List<String>>();

        // Add students and their courses to the map
        studentCourses.put("Jack", new ArrayList<String>());
        studentCourses.get("Jack").add("CS10");

        // Print out the map
        System.out.println(studentCourses);
    }

}
