import java.awt.*;
import java.util.*;

public class Sketch {
    private  Map<Integer, Shape> shapes;
    private  int ID;

    public Sketch(){
        shapes = new HashMap<>();
        ID = 0;
    }

    public synchronized void addShape(Shape s) {
        shapes.put(ID, s);
        ID ++;
    }

    public synchronized void removeShape(int id){
        if (shapes.containsKey(id)){
            shapes.remove(id);
        }
    }

    public Shape getShape(int id){
        return shapes.get(id);
    }

    public Map<Integer, Shape>  getShape(){
        return shapes;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Shape shape: shapes.values()){
            res.append("draw ");
            res.append(shape.toString());
            res.append("\n");
        }
        return res.toString();
    }
}
