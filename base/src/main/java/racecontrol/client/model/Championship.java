package racecontrol.client.model;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Champions MOD 2026
 * @author Vitor Justen
 */
public class Championship {

    private Map<String, BigDecimal> points = new LinkedHashMap<>();
    private Map<String, BigDecimal> drivers = new LinkedHashMap<>();

    public Championship() {

        String championshipPath = System.getProperty("user.dir") + "/championship";

        ObjectMapper mapper = new ObjectMapper();

        try {

            File pointsFile = new File(championshipPath + "/points.json");
            File driversFile = new File(championshipPath + "/drivers.json");

            this.points = mapper.readValue(
                    pointsFile,
                    new TypeReference<Map<String, BigDecimal>>() {}
            );

            this.drivers = mapper.readValue(
                    driversFile,
                    new TypeReference<Map<String, BigDecimal>>() {}
            );

        } catch (IOException e) {
        	 System.err.println("Warning: Could not load championship data: " + e.getMessage());
        }
    }

    public Map<String, BigDecimal> getPoints() {
        return points;
    }

    public void setPoints(Map<String, BigDecimal> points) {
        this.points = points;
    }

    public Map<String, BigDecimal> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<String, BigDecimal> drivers) {
        this.drivers = drivers;
    }
}