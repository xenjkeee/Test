package test.service;

import org.json.JSONException;
import org.json.JSONObject;

import test.Constants;

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
