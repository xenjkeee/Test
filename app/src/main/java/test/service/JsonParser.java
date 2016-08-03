package test.service;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import test.Constants;

public class JsonParser {
    public static String getProjectName(String json) {
        try {
            return new JSONObject(json).getJSONObject("project").getString("name");
        } catch (JSONException e) {
            Log.e(Constants.APP_LOG_TAG, "exception", e);
        }
        return "";
    }
}
