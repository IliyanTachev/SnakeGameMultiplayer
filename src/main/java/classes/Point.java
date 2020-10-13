package classes;

import org.json.simple.JSONObject;

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
}
