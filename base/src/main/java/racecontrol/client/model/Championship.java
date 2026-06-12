package racecontrol.client.model;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import racecontrol.client.protocol.enums.ChampionshipPoints;
import racecontrol.client.protocol.enums.DriverCategory;

/**
 * Champions MOD 2026
 * @author Vitor Justen
 */
public class Championship {

	private Boolean separateByCategory = false;
    private Map<String, BigDecimal> points = new LinkedHashMap<>();
    private Map<DriverCategory, Map<String, BigDecimal>> pointsByCategory = new HashMap<>();
    private Map<String, BigDecimal> drivers = new LinkedHashMap<>();

    public Championship() {
        this(false);
    }

    public Championship(Boolean separateByCategory) {
        this.separateByCategory = separateByCategory;
        loadChampionshipData();
    }

    private void loadChampionshipData() {

        String championshipPath = System.getProperty("user.dir") + "/championship";

        ObjectMapper mapper = new ObjectMapper();

        try {

            File driversFile = new File(championshipPath + "/drivers.json");

            this.points = readPointsFile(mapper, championshipPath, ChampionshipPoints.GENERAL);
            for (ChampionshipPoints pointFile : ChampionshipPoints.values()) {
                if (pointFile.getCategory() != null) {
                    pointsByCategory.put(pointFile.getCategory(), readPointsFile(mapper, championshipPath, pointFile));
                }
            }

            this.drivers = mapper.readValue(
                    driversFile,
                    new TypeReference<Map<String, BigDecimal>>() {}
            );
            
        } catch (IOException e) {
        	//Inicialize default values
        	points.put("1", BigDecimal.ZERO);
        	drivers.put("", BigDecimal.ZERO);
        	System.err.println("Warning: Could not load championship data: " + e.getMessage());
        }
    }

    private Map<String, BigDecimal> readPointsFile(ObjectMapper mapper, String championshipPath, ChampionshipPoints pointFile)
            throws IOException {
        File file = new File(championshipPath + "/points" + pointFile.getFilePath());
        return mapper.readValue(file, new TypeReference<Map<String, BigDecimal>>() {});
    }

    public Map<String, BigDecimal> getPoints(DriverCategory category) {
        if (!separateByCategory) {
            return points;
        }
        return pointsByCategory.getOrDefault(category, points);
    }

    public Boolean isSeparateByCategory() {
        return separateByCategory;
    }

    public void setSeparateByCategory(Boolean separateByCategory) {
        this.separateByCategory = separateByCategory;
    }

    public Map<String, BigDecimal> getPoints() {
        return points;
    }

    public void setPoints(Map<String, BigDecimal> points) {
        this.points = points;
    }

    public Map<DriverCategory, Map<String, BigDecimal>> getPointsByCategory() {
        return pointsByCategory;
    }

    public void setPointsByCategory(Map<DriverCategory, Map<String, BigDecimal>> pointsByCategory) {
        this.pointsByCategory = pointsByCategory;
    }

    public Map<String, BigDecimal> getDrivers() {
        return drivers;
    }

    public void setDrivers(Map<String, BigDecimal> drivers) {
        this.drivers = drivers;
    }
}
