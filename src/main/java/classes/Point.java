package classes;

import org.json.simple.JSONObject;

import java.util.Objects;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();
        json.put("x", getX());
        json.put("y", getY());
        return json;
    }

    public Point getRandomPoint(int maxX, int maxY){
        int randomX = (int) Math.floor(Math.random() * maxX);
        int randomY = (int) Math.floor(Math.random() * maxY);
        return new Point(randomX, randomY);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
