package com.solarcalc.io;

import com.solarcalc.model.Appliance;
import com.solarcalc.model.ProjectData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvExporter {
    public static void exportResults(ProjectData data, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // Header with summary
            bw.write("Project," + data.getProjectName()); bw.newLine();
            bw.write("TotalDailyEnergyWh," + com.solarcalc.calc.SolarCalculator.totalDailyEnergyWh(data)); bw.newLine();
            bw.write("RequiredPanelW," + com.solarcalc.calc.SolarCalculator.requiredPanelW(data)); bw.newLine();
            bw.write("RequiredBatteryAh," + com.solarcalc.calc.SolarCalculator.requiredBatteryAh(data)); bw.newLine();
            bw.write("RecommendedInverterW," + com.solarcalc.calc.SolarCalculator.recommendedInverterW(data)); bw.newLine();
            bw.write("RecommendedControllerA," + com.solarcalc.calc.SolarCalculator.recommendedControllerA(data)); bw.newLine();
            bw.newLine();
            // Appliances table
            bw.write("Name,Watts,Quantity,HoursPerDay,DailyEnergyWh"); bw.newLine();
            for (Appliance a : data.getAppliances()) {
                bw.write(String.format("%s,%.2f,%d,%.2f,%.2f", a.getName(), a.getWatts(), a.getQuantity(), a.getHoursPerDay(), a.dailyEnergyWh()));
                bw.newLine();
            }
        }
    }
}
