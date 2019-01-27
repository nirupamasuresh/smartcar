package com.smartcar.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smartcar.service.SmartcarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the controller by mocking the service layer
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SmartcarController.class)
public class SmartcarControllerTest {

    private static final Gson GSON = new Gson();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SmartcarService service;

    @Test
    public void getVehicleInfo() throws Exception {
        String res = "{ \"vin\": \"123123412412\", \"color\": \"Metallic Silver\", \"driveTrain\": \"v8\", \"doorCount\": 4 }";
        given(service.getVehicleInfo("1234")).willReturn(GSON.fromJson(res, JsonObject.class));
        mvc.perform(get("/vehicles/1234").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.vin", is("123123412412")))
                .andExpect(jsonPath("$.color", is("Metallic Silver")))
                .andExpect(jsonPath("$.doorCount", is(4)))
                .andExpect(jsonPath("$.driveTrain", is("v8")));
    }

    @Test
    public void getVehicleDoors() throws Exception {
        String res = "[ { \"location\": \"frontRight\", \"locked\": false }, { \"location\": \"frontLeft\", \"locked\": false }, { \"location\": \"backLeft\", \"locked\": false }, { \"location\": \"backRight\", \"locked\": false } ]";
        given(service.getVehicleDoors("1234")).willReturn(GSON.fromJson(res, JsonArray.class));
        mvc.perform(get("/vehicles/1234/doors").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void getVehicleFuel() throws Exception {
        String res = "{ \"percent\": 1.42 }";
        given(service.getEnergy("1234")).willReturn(GSON.fromJson(res, JsonObject.class));
        mvc.perform(get("/vehicles/1234/fuel").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.percent", is(1.42)));
    }

    @Test
    public void getVehicleBattery() throws Exception {
        String res = "{ \"percent\": 1.42 }";
        given(service.getBatteryLevel("1234")).willReturn(GSON.fromJson(res, JsonObject.class));
        mvc.perform(get("/vehicles/1234/battery").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.percent", is(1.42)));
    }

    @Test
    public void getVehicleAction() throws Exception {
        String res = "{ \"status\": \"success\" }";
        String req = "{ \"action\": \"STOP\" }";
        given(service.getActionEngine("1234", GSON.fromJson(req, Map.class))).willReturn(GSON.fromJson(res, JsonObject.class));
        mvc.perform(post("/vehicles/1234/engine").contentType(MediaType.APPLICATION_JSON).content(req))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")));
    }
}