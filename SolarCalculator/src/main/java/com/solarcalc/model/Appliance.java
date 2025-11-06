package com.solarcalc.model;

public class Appliance {
    private String name;
    private double watts;
    private int quantity;
    private double hoursPerDay;

    public Appliance() {}

    public Appliance(String name, double watts, int quantity, double hoursPerDay) {
        this.name = name;
        this.watts = watts;
        this.quantity = quantity;
        this.hoursPerDay = hoursPerDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWatts() {
        return watts;
    }

    public void setWatts(double watts) {
        this.watts = watts;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(double hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public double dailyEnergyWh() {
        return watts * quantity * hoursPerDay;
    }

    @Override
    public String toString() {
        return name + " (" + watts + "W x" + quantity + " x" + hoursPerDay + "h)";
    }
}
