package com.smartcar.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smartcar.utils.SmartcarConstants;
import com.smartcar.utils.SmartcarUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SmartcarServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Spy
    SmartcarService service;

    private static final Gson GSON = new Gson();

    @Test
    public void getVehicleInfo() throws Exception {
        String jsonResp = "{\"service\":\"getSecurityStatus\",\"status\":\"200\",\"data\":{\"doors\":{\"type\":\"Array\",\"values\":[{\"location\":{\"type\":\"String\",\"value\":\"backLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}},{\"location\":{\"type\":\"String\",\"value\":\"backRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}}]}}}";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_DOORS,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        String resp = "{ \"service\": \"getVehicleInfo\", \"status\": \"200\", \"data\": { \"vin\": { \"type\": \"String\", \"value\": \"123123412412\" }, \"color\": { \"type\": \"String\", \"value\": \"Metallic Silver\" }, \"fourDoorSedan\": { \"type\": \"Boolean\", \"value\": \"True\" }, \"twoDoorCoupe\": { \"type\": \"Boolean\", \"value\": \"False\" }, \"driveTrain\": { \"type\": \"String\", \"value\": \"v8\" } } }";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_INFO,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(resp);
        JsonObject flatObj = service.getVehicleInfo("1234");
        assertEquals(flatObj.size(), 4);
        assertEquals(flatObj.get("vin").getAsString(), "123123412412");
        assertEquals(flatObj.get("color").getAsString(), "Metallic Silver");
        assertEquals(flatObj.get("doorCount").getAsNumber(), 4);
        assertEquals(flatObj.get("driveTrain").getAsString(), "v8");
    }

    @Test
    public void getVehicleDoors() throws Exception {
        String jsonResp = "{\"service\":\"getSecurityStatus\",\"status\":\"200\",\"data\":{\"doors\":{\"type\":\"Array\",\"values\":[{\"location\":{\"type\":\"String\",\"value\":\"backLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}},{\"location\":{\"type\":\"String\",\"value\":\"backRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontRight\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"True\"}},{\"location\":{\"type\":\"String\",\"value\":\"frontLeft\"},\"locked\":{\"type\":\"Boolean\",\"value\":\"False\"}}]}}}";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_DOORS,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        JsonArray obj = service.getVehicleDoors("1234");
        assertEquals(obj.size(), 4);
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"frontLeft\",\"locked\":false}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"backRight\",\"locked\":true}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"backLeft\",\"locked\":false}", JsonObject.class)));
        assertTrue(obj.contains(GSON.fromJson("{\"location\":\"frontRight\",\"locked\":true}", JsonObject.class)));
    }

    @Test
    public void getEnergy() throws Exception {
        String jsonResp = "{ \"service\": \"getEnergyService\", \"status\": \"200\", \"data\": { \"tankLevel\": { \"type\": \"Number\", \"value\": \"30\" }, \"batteryLevel\": { \"type\": \"Null\", \"value\": \"null\" } } }";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        JsonObject resp = service.getEnergy("1234");
        assertEquals(resp.size(), 1);
        assertEquals(resp.get(SmartcarConstants.PERCENT).getAsInt(), 30);
    }

    @Test
    public void getEnergyNull() throws Exception {
        String jsonResp = "{ \"service\": \"getEnergyService\", \"status\": \"200\", \"data\": { \"batteryLevel\": { \"type\": \"Number\", \"value\": \"30\" }, \"tankLevel\": { \"type\": \"Null\", \"value\": \"null\" } } }";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        JsonObject resp = service.getEnergy("1234");
        assertEquals(resp.size(), 1);
        assertTrue(resp.get(SmartcarConstants.PERCENT).isJsonNull());
    }

    @Test
    public void getBatteryLevel() throws Exception {
        String jsonResp = "{ \"service\": \"getEnergyService\", \"status\": \"200\", \"data\": { \"tankLevel\": { \"type\": \"Number\", \"value\": \"30\" }, \"batteryLevel\": { \"type\": \"Number\", \"value\": \"30\" } } }";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        JsonObject resp = service.getBatteryLevel("1234");
        assertEquals(resp.size(), 1);
        assertEquals(resp.get(SmartcarConstants.PERCENT).getAsInt(), 30);
    }

    @Test
    public void getBatteryLevelNull() throws Exception {
        String jsonResp = "{ \"service\": \"getEnergyService\", \"status\": \"200\", \"data\": { \"tankLevel\": { \"type\": \"Number\", \"value\": \"30\" }, \"batteryLevel\": { \"type\": \"Null\", \"value\": \"null\" } } }";
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest("1234"), String.class)).thenReturn(jsonResp);
        JsonObject resp = service.getBatteryLevel("1234");
        assertEquals(resp.size(), 1);
        assertTrue(resp.get(SmartcarConstants.PERCENT).isJsonNull());
    }

    @Test
    public void getActionEngineExecuted() throws Exception {
        String jsonResp = "{ \"service\": \"actionEngine\", \"status\": \"200\", \"actionResult\": { \"status\": \"EXECUTED\" } }";
        Map<String, String> req = SmartcarUtils.createRequest("1234");
        req.put(SmartcarConstants.COMMAND, "START_VEHICLE");
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ACTION,
                req, String.class)).thenReturn(jsonResp);
        Map<String, String> actualReq = new HashMap<>();
        actualReq.put(SmartcarConstants.ACTION, "START");
        JsonObject resp = service.getActionEngine("1234", actualReq);
        assertEquals(resp.size(), 1);
        assertEquals(resp.get("status").getAsString(), "success");
    }

    @Test
    public void getActionEngineFailed() throws Exception {
        String jsonResp = "{ \"service\": \"actionEngine\", \"status\": \"200\", \"actionResult\": { \"status\": \"FAILED\" } }";
        Map<String, String> req = SmartcarUtils.createRequest("1234");
        req.put(SmartcarConstants.COMMAND, "START_VEHICLE");
        Mockito.when(restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ACTION,
                req, String.class)).thenReturn(jsonResp);
        Map<String, String> actualReq = new HashMap<>();
        actualReq.put(SmartcarConstants.ACTION, "START");
        JsonObject resp = service.getActionEngine("1234", actualReq);
        assertEquals(resp.size(), 1);
        assertEquals(resp.get("status").getAsString(), "error");
    }
}