package classes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationRunner {
    private Snake snake = new Snake();

    private JSONObject initGrid(JSONObject json){
        int gridWidth = Integer.parseInt(json.get("grid_width").toString());
        int gridHeight = Integer.parseInt(json.get("grid_height").toString());
        int randomX = (int) Math.floor(Math.random() * gridWidth), randomY = (int) Math.floor(Math.random() * gridHeight);
        this.snake.head.setX(randomX);
        this.snake.head.setY(randomY);
        JSONObject randomSpotGenerator = new JSONObject();
        randomSpotGenerator.put("x", randomX);
        randomSpotGenerator.put("y", randomY);
        JSONObject jsonData = new JSONObject();
        jsonData.put("random_spot", randomSpotGenerator);
        System.out.println("<----- HEAD ------>");
        System.out.println(this.snake.getHead().getX());
        System.out.println(this.snake.getHead().getY());
        return jsonData;
    }

    public JSONObject snakeMove(JSONObject json){
        String direction = json.get("direction").toString();
        try {
            List<Point> snake = (List<Point>) Snake.class.getMethod(direction).invoke(this.snake);
            JSONArray renderSnake = new JSONArray();
            for(Point point : snake){
                JSONObject pointData = new JSONObject();
                pointData.put("point", point.toJSONObject());
                renderSnake.add(pointData);
            }
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("renderSnake", renderSnake);
            return jsonResponse;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parseMethod(String methodName, JSONObject jsonObject) {
        Method method = null;
        try {
            method = getClass().getDeclaredMethod(methodName, JSONObject.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            assert method != null;
            return method.invoke(this, jsonObject).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
