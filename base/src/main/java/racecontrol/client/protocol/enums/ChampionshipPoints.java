package racecontrol.client.protocol.enums;

public enum ChampionshipPoints {

	BRONZE(DriverCategory.BRONZE, "/bronze-points.json"),
	SILVER(DriverCategory.SILVER, "/silver-points.json"),
	GOLD(DriverCategory.GOLD, "/gold-points.json"), 
	PLATINUM(DriverCategory.PLATINUM, "/platinum-points.json"),
	GENERAL(null, "/general-points.json");

	private DriverCategory category;
	private String filePath;

	private ChampionshipPoints(DriverCategory category, String filePath) {
		this.category = category;
		this.filePath = filePath;
	}

	public DriverCategory getCategory() {
		return category;
	}

	public String getFilePath() {
		return filePath;
	}

}
