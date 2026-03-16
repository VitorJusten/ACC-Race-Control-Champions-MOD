/*
 * Copyright (c) 2021 Leonard Sch�ngel
 * 
 * For licensing information see the included license (LICENSE.txt)
 */
package racecontrol.gui.app.livetiming.timing.tablemodels.columns;

import java.util.stream.Collectors;

import processing.core.PApplet;
import racecontrol.client.model.Car;
import racecontrol.client.model.Driver;
import racecontrol.gui.LookAndFeel;
import racecontrol.gui.lpui.table.LPTable;
import racecontrol.gui.lpui.table.LPTableColumn;

/**
 * 
 * Champions MOD 2026
 * @author Vitor Justen
 */
public class ChampionshipDriversColumn extends LPTableColumn {

    public ChampionshipDriversColumn() {
        super("Drivers");
        setMinWidth(140);
        setPriority(5);
        setCellRenderer(this::driversRenderer);
    }
	
	private void driversRenderer(PApplet applet, LPTable.RenderContext context) {
        Car car = (Car) context.object;

        String driverNames = car.drivers.stream()
                .map(Driver::fullName)
                .collect(Collectors.joining(", "));

        applet.textAlign(PApplet.LEFT, PApplet.CENTER);
        applet.textFont(LookAndFeel.fontMedium());
        applet.fill(LookAndFeel.COLOR_WHITE);
        applet.text(driverNames, 4, context.height / 2f);
    }
	
}
