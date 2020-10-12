package classes;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    Point head = new Point();
    List<Point> body = new ArrayList<>();

    public List<Point> moveRight(){
        head.setX(head.getX()+1);
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
