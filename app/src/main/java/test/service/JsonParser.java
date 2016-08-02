package test.service;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Стас on 28.07.2016.
 */
public class JsonParser {
    public static String getProjectName(String json) {
        try {
            return new JSONObject(json).getJSONObject("project").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
