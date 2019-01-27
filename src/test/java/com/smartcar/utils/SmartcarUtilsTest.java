package com.smartcar.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for SmartcarUtils
 */
public class SmartcarUtilsTest {
    private static final Gson GSON = new Gson();

    @Test
    public void createRequest() {
        Map<String, String> request = SmartcarUtils.createRequest("1234");
        assertEquals(request.get("id"), "1234");
        assertEquals(request.get("responseType"), "JSON");
    }

    @Test
    public void flattenObject() {
        String str = "{\"location\":{\"type\":\"String\",\"value\":\"backLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}}";
        JsonObject flatObj = SmartcarUtils.flatten(GSON.fromJson(str, JsonObject.class));
        assertEquals(flatObj.get("location").getAsString(), "backLeft");
        assertEquals(flatObj.get("locked").getAsBoolean(), false);
    }

    @Test
    public void flattenValidString() {
        String str = "{\"service\":\"getVehicleInfo\",\"status\":\"200\",\"data\":{\"vin\":{\"type\":\"String\",\"value\":\"123123412412\"},\"color\":{\"type\":\"String\",\"value\":\"Metallic Silver\"},\"fourDoorSedan\":{\"type\":\"Boolean\",\"value\":\"True\"},\"twoDoorCoupe\":{\"type\":\"Boolean\",\"value\":\"False\"},\"driveTrain\":{\"type\":\"String\",\"value\":\"v8\"}}}";
        JsonObject flatObj = SmartcarUtils.flatten(str);
        assertEquals(flatObj.size(), 5);
        assertEquals(flatObj.get("vin").getAsString(), "123123412412");
        assertEquals(flatObj.get("color").getAsString(), "Metallic Silver");
        assertEquals(flatObj.get("fourDoorSedan").getAsBoolean(), true);
        assertEquals(flatObj.get("twoDoorCoupe").getAsBoolean(), false);
        assertEquals(flatObj.get("driveTrain").getAsString(), "v8");
    }

    @Test
    public void flattenInvalidString() {
        String str = "{\"service\":\"getVehicleInfo\",\"status\":\"200\",\"NOT_DATA\":{\"vin\":{\"type\":\"String\",\"value\":\"123123412412\"},\"color\":{\"type\":\"String\",\"value\":\"Metallic Silver\"},\"fourDoorSedan\":{\"type\":\"Boolean\",\"value\":\"True\"},\"twoDoorCoupe\":{\"type\":\"Boolean\",\"value\":\"False\"},\"driveTrain\":{\"type\":\"String\",\"value\":\"v8\"}}}";
        JsonObject flatObj = SmartcarUtils.flatten(str);
        assertNull(flatObj);
    }

    @Test
    public void flattenValidArray() {
        String str = "{\"service\":\"getSecurityStatus\",\"status\":\"200\",\"data\":{\"doors\":{\"type\":\"Array\",\"values\":[{\"location\":{\"type\":\"String\",\"value\":\"backLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}},{\"location\":{\"type\":\"String\",\"value\":\"backRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}}]}}}";
        JsonArray obj = SmartcarUtils.flattenArray(str);
        assertEquals(obj.size(), 4);
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"frontLeft\",\"locked\":false}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"backRight\",\"locked\":true}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"backLeft\",\"locked\":false}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"frontRight\",\"locked\":true}", JsonObject.class)));
    }

    @Test
    public void flattenInvalidArray() {
        String str = "{\"service\":\"getSecurityStatus\",\"status\":\"200\",\"data\":{\"NOT_DOORS\":{\"type\":\"Array\",\"values\":[{\"location\":{\"type\":\"String\",\"value\":\"backLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}},{\"location\":{\"type\":\"String\",\"value\":\"backRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}}]}}}";
        JsonArray obj = SmartcarUtils.flattenArray(str);
        assertNull(obj);
    }
}