package classes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Snake {
    private final List<Point> body = new ArrayList<>();
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
        if(this.body.contains(collisionPosition)) {
            return null; // game over
        } else if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake();
        }

        if(this.body.size() > 1)
            nextMove(new Point(getHead().getX()+1, getHead().getY()));
        else {
            getHead().setX(getHead().getX()+1);
        }

        return getSnake();
    }
    public List<Point> moveLeft(){
        Point collisionPosition = new Point(getHead().getX()-1, getHead().getY());
        if(this.body.contains(collisionPosition)) {
            return null; // game over
        } else if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake();
        }

        if(this.body.size() > 1)
            nextMove(new Point(getHead().getX()-1, getHead().getY()));
        else {
            getHead().setX(getHead().getX()-1);
        }

        return getSnake();
    }
    public List<Point> moveUp(){
        Point collisionPosition = new Point(getHead().getX(), getHead().getY()-1);
        if(this.body.contains(collisionPosition)) {
            return null; // game over
        } else if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake();
        }

        if(this.body.size() > 1) {
            nextMove(new Point(getHead().getX(), getHead().getY()-1));
        }
        else {
            getHead().setY(getHead().getY()-1);
        }

        return getSnake();
    }
    public List<Point> moveDown(){
        Point collisionPosition = new Point(getHead().getX(), getHead().getY()+1);
        if(this.body.contains(collisionPosition)) {
            return null; // game over
        } else if(foodPointsOnMap.contains(collisionPosition)){
            this.body.add(0, collisionPosition);
            return getSnake();
        }

        if(this.body.size() > 1) {
            nextMove(new Point(getHead().getX(), getHead().getY()+1)); // pass new position for head
        }
        else {
            getHead().setY(getHead().getY()+1);
        }

        return getSnake();
    }

    public void nextMove(Point headDirection){
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(getHead().getX()); // lastX
        queue.add(getHead().getY()); // lastY

        getHead().setX(headDirection.getX());
        getHead().setY(headDirection.getY());

        for(int i=1;i<this.body.size();i++){
            queue.add(this.body.get(i).getX()); // lastX
            queue.add(this.body.get(i).getY()); // lastY
            this.body.get(i).setX(queue.poll());
            this.body.get(i).setY(queue.poll());
        }
    }

    public List<Point> getSnake(){
        return this.body;
    }

    public Point getHead(){
        return this.body.size() != 0 ? this.body.get(0) : null;
    }

    public void setHead(Point snakeHead) {
        this.body.add(0,snakeHead);
    }
}
