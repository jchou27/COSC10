import java.awt.*;
import java.util.TreeMap;

public class Sketch {
    public TreeMap<Integer, Shape> shapeTreeMap;
    public Shape currentShape;

    public Sketch(){
        this.shapeTreeMap = new TreeMap<>();
    }

    public TreeMap<Integer, Shape> getShapeTreeMap(){
        return shapeTreeMap;
    }

    public Integer contains(int x, int y){
        for (Integer id : shapeTreeMap.keySet()){
            if (shapeTreeMap.get(id).contains(x,y)){
                currentShape = shapeTreeMap.get(id);
                return id;
            }
        }
        return -1;
    }

    public void deleteShape(Integer id){
        shapeTreeMap.remove(id);
    }

    public void draw(Graphics g){
        for (Integer id: shapeTreeMap.descendingKeySet()){
            shapeTreeMap.get(id).draw(g);
        }
    }

    @Override
    public String toString(){
        String s = "sketch {";
        for (Integer id : shapeTreeMap.keySet()){
            s += id.toString() + " ";
            s += shapeTreeMap.get(id).toString();
            s += ", ";
        }
        s += " }";
        return s;
    }

}
