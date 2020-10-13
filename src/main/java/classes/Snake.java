package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Snake {
    Point head = new Point();
    List<Point> body = new ArrayList<>();
    List<Point> foodPointsOnMap = new ArrayList<>();

    public List<Point> getFoodPointsOnMap() {
        return foodPointsOnMap;
    }

    public void setFoodPointsOnMap(List<Point> foodPointsOnMap) {
        this.foodPointsOnMap = foodPointsOnMap;
    }

    public void addPointToBody(Point point){
        body.add(point);
    }

    public List<Point> moveRight(){
        head.setX(head.getX()+1);
        Point checkPoint = new Point(head.getX(), head.getY());
        for(Point p : foodPointsOnMap){
           // if(p == checkPoint) this.addPointToBody(); check for collision
        }
        return getSnake();
    }
    public List<Point> moveLeft(){
        head.setX(head.getX()-1);
        return getSnake();
    }
    public List<Point> moveUp(){
        head.setY(head.getY()-1);
        return getSnake();
    }
    public List<Point> moveDown(){
        head.setY(head.getY()+1);
        return getSnake();
    }
    public List<Point> getSnake(){
        this.body.add(0, this.head);
        return this.body;
    }

    public Point getHead(){
        return this.head;
    }
}
