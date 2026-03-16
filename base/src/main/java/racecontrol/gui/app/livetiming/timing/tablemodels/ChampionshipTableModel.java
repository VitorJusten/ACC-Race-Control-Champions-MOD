package racecontrol.gui.app.livetiming.timing.tablemodels;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import processing.core.PApplet;
import racecontrol.client.model.Car;
import racecontrol.client.model.Championship;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.CarNumberColumn;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.ChampionshipDriversColumn;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.PositionColumn;
import racecontrol.gui.lpui.table.LPTable.RenderContext;
import racecontrol.gui.lpui.table.LPTableColumn;

/**
 * Champions MOD 2026
 * @author Vitor Justen
 */
public class ChampionshipTableModel extends LiveTimingTableModel {

	private final Championship championship = new Championship();
	private final Map<String, BigDecimal> savedDriverPoints = championship.getDrivers();
	private final Map<String, BigDecimal> racePoints = championship.getPoints();

	@Override
	public LPTableColumn[] getColumns() {
		
		 if (championship.getDrivers().isEmpty() && championship.getPoints().isEmpty()) {
		        return new LPTableColumn[]{
		            new LPTableColumn("Championship Error")
		                .setCellRenderer((applet, context) -> {
		                    applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		                    applet.text(
		                        "Championship JSON files not found or empty.\n" +
		                        "Please check drivers.json and points.json in: " +
		                        System.getProperty("user.dir") + "/championship",
		                        context.width / 2f,
		                        context.height / 2f
		                    );
		                })
		        };
		    }
		
		return new LPTableColumn[] {
				new PositionColumn(),
			    new ChampionshipDriversColumn(), 
				new CarNumberColumn(),

				new LPTableColumn("Car")
						.setMinWidth(140)
						.setCellRenderer(this::carRenderer),

				new LPTableColumn("Team")
						.setMinWidth(160)
						.setCellRenderer(this::teamRenderer),

				new LPTableColumn("Points")
						.setMinWidth(90)
						.setPriority(3)
						.setCellRenderer(this::pointsRenderer)
		};
	}

	private void carRenderer(PApplet applet, RenderContext context) {
		Car car = (Car) context.object;
		applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		applet.text(car.carModel.getName(), context.width / 2f, context.height / 2f);
	}

	private void teamRenderer(PApplet applet, RenderContext context) {
		Car car = (Car) context.object;
		applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		applet.text(car.teamName, context.width / 2f, context.height / 2f);
	}

	private void pointsRenderer(PApplet applet, RenderContext context) {
		Car car = (Car) context.object;
		BigDecimal total = calculatePoints(car);
		applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		applet.text(total.toString(), context.width / 2f, context.height / 2f);
	}

	private BigDecimal calculatePoints(Car car) {
		BigDecimal positionPoints = racePoints.getOrDefault(String.valueOf(car.position), BigDecimal.ZERO);
		return car.drivers.stream()
				.map(d -> savedDriverPoints.getOrDefault(d.fullName(), BigDecimal.ZERO).add(positionPoints))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public String getName() {
		return "updated championship";
	}

	@Override
	public void sort() {
	    entries = entries.stream()
	            .sorted((c1, c2) -> calculatePoints(c2).compareTo(calculatePoints(c1)))
	            .collect(Collectors.toList());
	}
}