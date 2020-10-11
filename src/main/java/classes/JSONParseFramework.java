package classes;

import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JSONParseFramework {

    public JSONObject initGrid(JSONObject json){
        json = (JSONObject) json.get("params"); // retrieve params
        int gridWidth = Integer.parseInt(json.get("grid_width").toString());
        int gridHeight = Integer.parseInt(json.get("grid_height").toString());
        JSONObject randomSpotGenerator = new JSONObject();
        randomSpotGenerator.put("random_x", (int) Math.floor(Math.random() * gridWidth));
        randomSpotGenerator.put("random_y", (int) Math.floor(Math.random() * gridHeight));
        JSONObject jsonData = new JSONObject();
        jsonData.put("random_spot", randomSpotGenerator);
        return jsonData;
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
