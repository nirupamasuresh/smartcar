package com.smartcar.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility for smartcar services
 */
public class SmartcarUtils {

    /**
     * Creates request object
     *
     * @param id vehicle id
     * @return request object
     */
    public static Map<String, String> createRequest(String id) {
        Map<String, String> req = new HashMap<>();
        req.put(SmartcarConstants.ID, id);
        req.put(SmartcarConstants.RESPONSE_TYPE, "JSON");
        return req;
    }

    /**
     * Flattens object
     *
     * @param obj object to flatten
     * @return flattened object
     */
    public static JsonObject flatten(JsonObject obj) {
        JsonObject flatten = new JsonObject();
        for (String key : obj.keySet()) {
            String type = obj.getAsJsonObject(key).get(SmartcarConstants.TYPE).getAsString().toLowerCase();
            JsonElement value;
            value = obj.getAsJsonObject(key).get(SmartcarConstants.VALUE);

            switch (type) {
                case "number":
                    flatten.addProperty(key, value.getAsNumber());
                    break;
                case "boolean":
                    flatten.addProperty(key, value.getAsBoolean());
                    break;
                case "string":
                    flatten.addProperty(key, value.getAsString());
                    break;
                case "null":
                    flatten.addProperty(key, (String) null);
                    break;
            }
        }
        return flatten;
    }

    /**
     * Flattens a string into an object
     *
     * @param infoString string to be flattened
     * @return flattened object
     */
    public static JsonObject flatten(String infoString) {
        JsonObject info = new Gson().fromJson(infoString, JsonObject.class);
        if (info == null) {
            return null;
        }
        JsonObject infoData = (JsonObject) info.get(SmartcarConstants.DATA);
        if (infoData == null) {
            return null;
        }
        return flatten(infoData);
    }

    /**
     * Flatten a string to array (for doors)
     *
     * @param doorsString
     * @return return list of flattened objects
     */
    public static JsonArray flattenArray(String doorsString) {
        JsonObject doorsData = new Gson().fromJson(doorsString, JsonObject.class);
        if (doorsData == null) {
            return null;
        }
        JsonObject data = doorsData.getAsJsonObject(SmartcarConstants.DATA);
        if (data == null) {
            return null;
        }
        JsonObject doors = data.getAsJsonObject(SmartcarConstants.DOORS);
        if (doors == null) {
            return null;
        }
        JsonArray values = doors.getAsJsonArray(SmartcarConstants.VALUES);
        JsonArray vehicleDoors = new JsonArray();
        for (JsonElement value : values) {
            vehicleDoors.add(flatten(value.getAsJsonObject()));
        }
        return vehicleDoors;
    }
}
