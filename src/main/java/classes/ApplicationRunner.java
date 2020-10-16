package classes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationRunner {
    private final Snake snake = new Snake();

    private JSONObject initGrid(JSONObject json){
        int gridWidth = Integer.parseInt(json.get("grid_width").toString());
        int gridHeight = Integer.parseInt(json.get("grid_height").toString());
        int foodSize = 2;
        Set<Point> randomGeneratedSpots = new HashSet<>();
        for(int i=0;i<foodSize+1;i++){
            randomGeneratedSpots.add(new Point().getRandomPoint(gridWidth, gridHeight));
        }

        snake.setFoodPointsOnMap(randomGeneratedSpots.stream().skip(1).collect(Collectors.toList()));
        Iterator<Point> it = randomGeneratedSpots.iterator();
        Point snakeHead = it.next();
        this.snake.setHead(snakeHead);
        System.out.println("HEAD ====> x = " + snakeHead.getX() + "    y = " + snakeHead.getY());

        JSONObject snakeHeadObject = new JSONObject();
        snakeHeadObject.put("x", snakeHead.getX());
        snakeHeadObject.put("y", snakeHead.getY());

        JSONArray foodPoints = new JSONArray();

        int i=1;
        while(it.hasNext()){
            Point point = it.next();
            JSONObject pointObject = new JSONObject();
            pointObject.put("x", point.getX());
            pointObject.put("y", point.getY());
            System.out.println("FOOD("+ i +") ====> x =  " + point.getX() + "    y = " + point.getY());
            foodPoints.add(pointObject);
            i++;
        }

        JSONObject randomDataObject = new JSONObject();
        randomDataObject.put("snakeHead", snakeHeadObject);
        randomDataObject.put("food", foodPoints);

        JSONObject jsonData = new JSONObject();
        jsonData.put("random_spots", randomDataObject);

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
        return "{error_on_server:true}";
    }
}
