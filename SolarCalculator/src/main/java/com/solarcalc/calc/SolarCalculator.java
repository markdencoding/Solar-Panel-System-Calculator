package com.solarcalc.calc;

import com.solarcalc.model.Appliance;
import com.solarcalc.model.ProjectData;

public class SolarCalculator {

    public static double totalDailyEnergyWh(ProjectData data) {
        return data.getAppliances().stream()
                .mapToDouble(Appliance::dailyEnergyWh)
                .sum();
    }

    public static double requiredPanelW(ProjectData data) {
        double total = totalDailyEnergyWh(data);
        double psh = data.getParams().getPeakSunHours();
        double eff = data.getParams().getEfficiency();
        if (psh <= 0 || eff <= 0) throw new IllegalArgumentException("PSH and efficiency must be > 0");
        return total / (psh * eff);
    }

    public static double requiredBatteryAh(ProjectData data) {
        double total = totalDailyEnergyWh(data);
        double days = data.getParams().getDaysOfAutonomy();
        double dod = data.getParams().getDepthOfDischargePercent() / 100.0;
        int voltage = data.getParams().getSystemVoltage();
        double neededWh = total * days;
        if (dod <= 0) throw new IllegalArgumentException("DoD must be > 0");
        return neededWh / (voltage * dod);
    }

    public static double recommendedInverterW(ProjectData data) {
        double peakLoad = data.getAppliances().stream()
                .mapToDouble(a -> a.getWatts() * a.getQuantity())
                .sum();
        double factor = data.getParams().getInverterSafetyFactor();
        return Math.ceil(peakLoad * factor);
    }

    public static double recommendedControllerA(ProjectData data) {
        double panelW = requiredPanelW(data);
        double voltage = data.getParams().getSystemVoltage();
        double rawA = panelW / voltage;
        return Math.ceil(rawA * data.getParams().getControllerMargin());
    }
}
