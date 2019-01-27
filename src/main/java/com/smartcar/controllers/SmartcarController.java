package com.smartcar.controllers;

import com.smartcar.service.SmartcarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/vehicles/{id}")
public class SmartcarController {

    @Autowired
    SmartcarService smartcarService;

    /**
     * Get vehicle information
     *
     * @param id vehicle id
     * @return vehicle information
     */
    @GetMapping(path = "", produces = "application/json")
    @ResponseBody
    public String getVehicleInfo(@PathVariable("id") String id) {
        return smartcarService.getVehicleInfo(id).toString();
    }

    /**
     * Get vehicle door information
     *
     * @param id vehicle id
     * @return vehicle doors information
     */
    @GetMapping(value = "/doors", produces = "application/json")
    @ResponseBody
    public String getVehicleDoors(@PathVariable("id") String id) {
        return smartcarService.getVehicleDoors(id).toString();
    }

    /**
     * Get vehicle fuel percentage
     *
     * @param id vehicle id
     * @return fuel percentage
     */
    @GetMapping(value = "/fuel", produces = "application/json")
    @ResponseBody
    public String getVehicleFuel(@PathVariable("id") String id) {
        return smartcarService.getEnergy(id).toString();
    }

    /**
     * Get battery level
     *
     * @param id vehicle id
     * @return battery level
     */
    @GetMapping(value = "/battery", produces = "application/json")
    @ResponseBody
    public String getVehicleBattery(@PathVariable("id") String id) {
        return smartcarService.getBatteryLevel(id).toString();
    }

    /**
     * Get vehicle action information
     *
     * @param id      vehicle id
     * @param request command/action
     * @return action
     */
    @PostMapping(value = "/engine", produces = "application/json")
    @ResponseBody
    public String getVehicleAction(@PathVariable("id") String id, @RequestBody Map<String, String> request) {
        return smartcarService.getActionEngine(id, request).toString();
    }
}
