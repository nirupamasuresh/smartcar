package com.smartcar.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smartcar.utils.SmartcarConstants;
import com.smartcar.utils.SmartcarUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Services to fetch information about vehicles
 */
@Service
public class SmartcarService {

    RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
//        System.out.println(getVehicleDoors("1234"));
        System.out.println(new SmartcarService().getVehicleInfo("1234"));
//        System.out.println(getEnergy("1234"));
//        System.out.println(getBatteryLevel("1234"));
//        Map<String, String> command = new HashMap<>();
//        command.put("action", "START|STOP");
//        System.out.println(getActionEngine("1234", command));
    }

    /**
     * Calls the GM API to get vehicle information
     *
     * @param id id of the vehicle
     * @return Final object with the required fields
     */
    public JsonObject getVehicleInfo(String id) {
        String info = restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_INFO,
                SmartcarUtils.createRequest(id), String.class);
        JsonObject vehicleInfo = SmartcarUtils.flatten(info);

        //get the number of doors by calling the service
        JsonArray vehicleDoors = getVehicleDoors(id);
        vehicleInfo.addProperty(SmartcarConstants.DOOR_COUNT, vehicleDoors.size());

        //removing fields not required in the response
        vehicleInfo.remove(SmartcarConstants.FOUR_DOOR_SEDAN);
        vehicleInfo.remove(SmartcarConstants.TWO_DOOR_COUPE);
        return vehicleInfo;
    }

    /**
     * Calls the GM API to fetch information about vehicle doors
     *
     * @param id id of the vehicle
     * @return list of door information
     */
    public JsonArray getVehicleDoors(String id) {
        String doors = restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_DOORS,
                SmartcarUtils.createRequest(id), String.class);
        return SmartcarUtils.flattenArray(doors);
    }

    /**
     * Calls GM API to get fuel percentage
     *
     * @param id id of the vehicle
     * @return percent of fuel (null if not available)
     */
    public JsonObject getEnergy(String id) {
        String energy = restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest(id), String.class);
        JsonObject object = SmartcarUtils.flatten(energy);

        //forming the required response after flattening
        JsonObject resp = new JsonObject();
        resp.add(SmartcarConstants.PERCENT, object.get(SmartcarConstants.TANK_LEVEL));
        return resp;
    }

    /**
     * Calls GM API to get battery level
     *
     * @param id id of the vehicle
     * @return percent of battery level (null if not available)
     */
    public JsonObject getBatteryLevel(String id) {
        String energy = restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ENERGY,
                SmartcarUtils.createRequest(id), String.class);
        JsonObject object = SmartcarUtils.flatten(energy);

        //forming the required response after flattening
        JsonObject resp = new JsonObject();
        resp.add(SmartcarConstants.PERCENT, object.get(SmartcarConstants.BATTERY_LEVEL));
        return resp;
    }

    /**
     * Fetches the action of the engine
     *
     * @param id     id of the vehicle
     * @param action action
     * @return action
     */
    public JsonObject getActionEngine(String id, Map<String, String> action) {
        //form the request by including action
        Map<String, String> req = new HashMap<>();
        req.putAll(SmartcarUtils.createRequest(id));

        //modify the command according to specification
        String command = action.get(SmartcarConstants.ACTION);
        command += "_VEHICLE";
        req.put(SmartcarConstants.COMMAND, command);
        String actionString = restTemplate.postForObject(SmartcarConstants.GM_URL + SmartcarConstants.GET_ACTION,
                req, String.class);
        JsonObject object = new Gson().fromJson(actionString, JsonObject.class);
        JsonObject resp = new JsonObject();
        String status = object.getAsJsonObject(SmartcarConstants.ACTION_RESULT).get(SmartcarConstants.STATUS)
                .getAsString().replaceAll(SmartcarConstants.EXECUTED, SmartcarConstants.SUCCESS)
                .replaceAll(SmartcarConstants.FAILED, SmartcarConstants.ERROR);
        resp.addProperty(SmartcarConstants.STATUS, status);
        return resp;
    }
}
