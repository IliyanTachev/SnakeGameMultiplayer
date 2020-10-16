package classes;

import java.util.ArrayList;
import java.util.List;

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
        Point collisionPosition = new Point(getHead().getX()+1, getHead().getY());
        if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake(true);
        }

        if(this.body.size() > 1) {
//            getHead().setX(getHead().getX()+1);
            nextMove(new Point(getHead().getX()+1, getHead().getY()));
        } else {
            getHead().setX(getHead().getX()+1);
        }

        return getSnake(false);
    }
    public List<Point> moveLeft(){
        System.out.println("head => (" + getHead().getX() + ", " + getHead().getY() + ")");
        Point collisionPosition = new Point(getHead().getX()-1, getHead().getY());
        if(foodPointsOnMap.contains(collisionPosition)){
            System.out.println("head => (" + getHead().getX() + ", " + getHead().getY() + ")");
            this.body.add(0, collisionPosition);
            return getSnake(true);
        }

        if(this.body.size() > 1) {
//            getHead().setX(getHead().getX()-1);
            nextMove(new Point(getHead().getX()-1, getHead().getY()));
        } else {
            getHead().setX(getHead().getX()-1);
        }

        return getSnake(false);
    }
    public List<Point> moveUp(){
        Point collisionPosition = new Point(getHead().getX(), getHead().getY()-1);
        if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake(true);
        }

        if(this.body.size() > 1) {
//            getHead().setY(getHead().getY()-1);
            nextMove(new Point(getHead().getX(), getHead().getY()-1));
        } else {
            getHead().setY(getHead().getY()-1);
        }

        return getSnake(false);
    }
    public List<Point> moveDown(){
        Point collisionPosition = new Point(getHead().getX(), getHead().getY()+1);
        if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake(true);
        }

        if(this.body.size() > 1) {
            nextMove(new Point(getHead().getX(), getHead().getY()+1)); // pass new position for head
        } else {
            getHead().setY(getHead().getY()+1);
        }

        return getSnake(false);
    }

    public void nextMove(Point headDirection){
        this.body.add(0, headDirection);
        for(int i=1;i<this.body.size()-1;i++){
            Point currentPoint = this.body.get(i);
            Point nextPoint = this.body.get(i+1);
            currentPoint.setX(nextPoint.getX());
            currentPoint.setY(nextPoint.getY());
        }
        this.body.remove(this.body.size()-1);
    }

    public List<Point> getSnake(boolean flag){
        return this.body;
    }

    public Point getHead(){
        return this.body.size() != 0 ? this.body.get(0) : null;
    }

    public void setHead(Point snakeHead) {
        this.body.add(0,snakeHead);
    }
}
