package classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Snake {
    private List<Point> body = new ArrayList<>();
    private List<Point> foodPointsOnMap = new ArrayList<>();

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
        getHead().setX(getHead().getX()+1);
        Point checkPoint = new Point(getHead().getX(), getHead().getY());
        return getSnake(false);
    }
    public List<Point> moveLeft(){
        getHead().setX(getHead().getX()-1);
        return getSnake(false);
    }
    public List<Point> moveUp(){
        getHead().setY(getHead().getY()-1);
        return getSnake(false);
    }
    public List<Point> moveDown(){
        Point collisionPosition = new Point(getHead().getX(), getHead().getY()+1);
        if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake(true);
        }

        getHead().setY(getHead().getY()+1);
        for (int i = 1; i < body.size(); i++) {
            Point currentBodyPoint = this.body.get(i);
            currentBodyPoint = this.body.get(i + 1);
        }
        this.body.remove(this.body.size() - 1);
        return getSnake(false);
    }
    public List<Point> getSnake(boolean flag){
       if(flag) {
           for(Point p : this.body){
               System.out.println(p.toString());
           }
       }
        return this.body;
    }

    public Point getHead(){
        return this.body.size() != 0 ? this.body.get(0) : null;
    }
}
