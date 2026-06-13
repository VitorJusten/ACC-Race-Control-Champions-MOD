package racecontrol.gui.app.livetiming.timing.tablemodels;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static racecontrol.gui.LookAndFeel.COLOR_WHITE;
import static racecontrol.gui.LookAndFeel.LINE_HEIGHT;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import processing.core.PApplet;
import racecontrol.client.model.Car;
import racecontrol.client.model.Championship;
import racecontrol.client.model.Driver;
import racecontrol.gui.LookAndFeel;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.CarNumberColumn;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.ChampionshipDriversColumn;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.ConstructorColumn;
import racecontrol.gui.app.livetiming.timing.tablemodels.columns.PositionColumn;
import racecontrol.gui.lpui.table.LPTable.RenderContext;
import racecontrol.gui.lpui.table.LPTable;
import racecontrol.gui.lpui.table.LPTableColumn;

/**
 * Champions MOD 2026
 * @author Vitor Justen
 */
public class ChampionshipTableModel extends LiveTimingTableModel {

	private final Championship championship = new Championship(false);
	private final Map<String, BigDecimal> savedDriverPoints = championship.getDrivers();

	@Override
	public LPTableColumn[] getColumns() {
		
		 if (championship.getDrivers().isEmpty() && championship.getPointsByCategory().isEmpty()) {
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
	            new ConstructorColumn(),
	            new CarNumberColumn(),
	            
	            new LPTableColumn("Class")
	            		.setMaxWidth(100)
	            		.setMinWidth(100)
	            		.setTextAlign(LEFT)
	            		.setCellRenderer((applet, context) -> carClassRenderer(applet, context)),
	            
				new LPTableColumn("Total Points")
						.setMinWidth(90)
						.setPriority(3)
						.setCellRenderer(this::pointsRenderer),
						
				new LPTableColumn("Points +")
						.setMinWidth(90)
						.setPriority(3)
						.setCellRenderer(this::pointsGainedRenderer),
						
		};
	}
	
    private void carClassRenderer(PApplet applet, LPTable.RenderContext context) {
        Car car = (Car) context.object;
        String name = car.carModel.getCategory().getText();
        applet.fill(COLOR_WHITE);
        applet.textAlign(LEFT, CENTER);
        applet.textFont(LookAndFeel.fontRegular());
        applet.text(name, 10f, LINE_HEIGHT / 2f);
    }

	private void pointsRenderer(PApplet applet, RenderContext context) {
		Car car = (Car) context.object;
		BigDecimal total = calculatePoints(car);
		applet.fill(COLOR_WHITE);
		applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		applet.text(total.toString(), context.width / 2f, context.height / 2f);
	}
	
	private void pointsGainedRenderer(PApplet applet, RenderContext context) {
		Car car = (Car) context.object;
		BigDecimal total = calculatePositionPoints(car);
		applet.fill(COLOR_WHITE);
		applet.textAlign(PApplet.CENTER, PApplet.CENTER);
		applet.text("+ " + total.toString(), context.width / 2f, context.height / 2f);
	}

	private BigDecimal calculatePoints(Car car) {
	    return car.drivers.stream()
	            .map(d -> savedDriverPoints
	                    .getOrDefault(d.fullName(), BigDecimal.ZERO)
	                    .add(getPositionPoints(d, car)))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calculatePositionPoints(Car car) {
	    return car.drivers.stream()
	            .map(d -> getPositionPoints(d, car))
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal getPositionPoints(Driver driver, Car car) {

	    int position = championship.isSeparateByCategory()
	            ? getCategoryPosition(driver)
	            : car.position;

	    return championship.getPoints(driver.category)
	            .getOrDefault(String.valueOf(position), BigDecimal.ZERO);
	}
	
	private int getCategoryPosition(Driver driver) {

	    int position = 1;

	    for (Object obj : entries) {

	        Car car = (Car) obj;

	        for (Driver d : car.drivers) {

	            if (d == driver) {
	                return position;
	            }

	            if (d.category == driver.category) {
	                position++;
	            }
	        }
	    }

	    return position;
	}

	public void setSeparateByCategory(boolean state) {
		championship.setSeparateByCategory(state);
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
