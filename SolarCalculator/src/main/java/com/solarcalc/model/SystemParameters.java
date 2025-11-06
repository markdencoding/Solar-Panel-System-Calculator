package com.solarcalc.model;

public class SystemParameters {
    private double peakSunHours = 4.5;
    private int systemVoltage = 24; // 12, 24, 48
    private double depthOfDischargePercent = 50.0;
    private int daysOfAutonomy = 2;
    private double efficiency = 0.75; // system efficiency
    private double inverterSafetyFactor = 1.25;
    private double controllerMargin = 1.25; // 25% margin

    public double getPeakSunHours() {
        return peakSunHours;
    }

    public void setPeakSunHours(double peakSunHours) {
        this.peakSunHours = peakSunHours;
    }

    public int getSystemVoltage() {
        return systemVoltage;
    }

    public void setSystemVoltage(int systemVoltage) {
        this.systemVoltage = systemVoltage;
    }

    public double getDepthOfDischargePercent() {
        return depthOfDischargePercent;
    }

    public void setDepthOfDischargePercent(double depthOfDischargePercent) {
        this.depthOfDischargePercent = depthOfDischargePercent;
    }

    public int getDaysOfAutonomy() {
        return daysOfAutonomy;
    }

    public void setDaysOfAutonomy(int daysOfAutonomy) {
        this.daysOfAutonomy = daysOfAutonomy;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public double getInverterSafetyFactor() {
        return inverterSafetyFactor;
    }

    public void setInverterSafetyFactor(double inverterSafetyFactor) {
        this.inverterSafetyFactor = inverterSafetyFactor;
    }

    public double getControllerMargin() {
        return controllerMargin;
    }

    public void setControllerMargin(double controllerMargin) {
        this.controllerMargin = controllerMargin;
    }
}
