import java.awt.*;
import java.util.TreeMap;

/**
 * A sketch is a collection of shapes
 * @author Jack Chou
 */
public class Sketch {
    public TreeMap<Integer, Shape> shapeTreeMap; //id, shape
    public Shape currentShape; //the shape that is currently being manipulated

    /**
     * Constructor for Sketch
     */
    public Sketch(){
        this.shapeTreeMap = new TreeMap<>();
    }

    public TreeMap<Integer, Shape> getShapeTreeMap(){
        return shapeTreeMap;
    }

    /**
     * Used to find the top most shape that contains the point (x,y)
     * @param x
     * @param y
     * @return the id of the shape that contains the point (x,y)
     */
    public Integer contains(int x, int y){
        for (Integer id : shapeTreeMap.descendingKeySet()){ //get the top most shape
            if (shapeTreeMap.get(id).contains(x,y)){
                currentShape = shapeTreeMap.get(id); //set the current shape to the top most shape
                return id;
            }
        }
        return -1; //no shape contains the point
    }

    public void deleteShape(Integer id){
        shapeTreeMap.remove(id);
    }

    public void draw(Graphics g){
        for (Integer id: shapeTreeMap.navigableKeySet()){ //draw the shapes in the order they were added
            shapeTreeMap.get(id).draw(g);
        }
    }

    /**
     * Returns a string representation of the sketch to communicator
     * Format: "sketch { " + id1 + " " + shape1 + ", " + id2 + " " + shape2 + ", ... + " }"
     * Example: "sketch {1 segment 10 10 20 20 -16777216, 2 rectangle 30 30 40 40 -16777216,  }"
     * @return
     */
    @Override
    public String toString(){
        String s = "sketch {";
        for (Integer id : shapeTreeMap.navigableKeySet()){
            s += id.toString() + " "; //get the id of the shape
            s += shapeTreeMap.get(id).toString(); //get the string representation of the shape
            s += ", "; //add a comma to separate the shapes
        }
        s += " }";
        return s;
    }

}
