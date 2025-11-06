package com.solarcalc.calc;

import com.solarcalc.model.Appliance;
import com.solarcalc.model.ProjectData;
import com.solarcalc.model.SystemParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolarCalculatorTest {

    @Test
    public void testTotalDailyEnergy() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("Lamp", 10, 4, 5)); // 200
        pd.addAppliance(new Appliance("TV", 80, 1, 4)); // 320
        assertEquals(520, SolarCalculator.totalDailyEnergyWh(pd), 1e-6);
    }

    @Test
    public void testRequiredPanelWExample() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("Fridge", 100, 1, 24));
        pd.addAppliance(new Appliance("TV", 80, 1, 4));
        pd.addAppliance(new Appliance("Lights", 10, 5, 5));
        SystemParameters p = pd.getParams();
        p.setPeakSunHours(4.5);
        p.setEfficiency(0.75);
        double total = SolarCalculator.totalDailyEnergyWh(pd);
        assertEquals(2970, total, 1e-6);
        double panelW = SolarCalculator.requiredPanelW(pd);
        assertEquals(2970 / (4.5 * 0.75), panelW, 1e-6);
    }

    @Test
    public void testBatteryAhCalculation() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("Load", 100, 1, 10)); // 1000Wh
        pd.getParams().setDaysOfAutonomy(2);
        pd.getParams().setDepthOfDischargePercent(50);
        pd.getParams().setSystemVoltage(24);
        double ah = SolarCalculator.requiredBatteryAh(pd);
        // neededWh = 2000 Wh, Ah = 2000 / (24 * 0.5) = 166.666...
        assertEquals(2000.0 / (24 * 0.5), ah, 1e-6);
    }

    @Test
    public void testInverterSizing() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("Device", 200, 2, 1)); // peak 400
        pd.getParams().setInverterSafetyFactor(1.25);
        double inv = SolarCalculator.recommendedInverterW(pd);
        assertEquals(Math.ceil(400 * 1.25), inv, 1e-6);
    }

    @Test
    public void testControllerSizing() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("D", 100, 1, 2));
        pd.getParams().setPeakSunHours(5);
        pd.getParams().setSystemVoltage(24);
        double panelW = SolarCalculator.requiredPanelW(pd);
        double controller = SolarCalculator.recommendedControllerA(pd);
        assertTrue(controller > 0);
    }

    @Test
    public void testInvalidPshThrows() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("A", 100, 1, 1));
        pd.getParams().setPeakSunHours(0);
        assertThrows(IllegalArgumentException.class, () -> SolarCalculator.requiredPanelW(pd));
    }

    @Test
    public void testDodZeroThrows() {
        ProjectData pd = new ProjectData();
        pd.addAppliance(new Appliance("A", 100, 1, 1));
        pd.getParams().setDepthOfDischargePercent(0);
        assertThrows(IllegalArgumentException.class, () -> SolarCalculator.requiredBatteryAh(pd));
    }

    @Test
    public void testSaveLoadJsonRoundtrip() throws Exception {
        com.solarcalc.model.ProjectData pd = new com.solarcalc.model.ProjectData();
        pd.setProjectName("TestProject");
        pd.addAppliance(new Appliance("Lamp", 10, 2, 3));
        java.io.File tmp = java.io.File.createTempFile("proj",".json");
        tmp.deleteOnExit();
        com.solarcalc.io.JsonPersistence.save(pd, tmp);
        com.solarcalc.model.ProjectData loaded = com.solarcalc.io.JsonPersistence.load(tmp);
        assertEquals(pd.getProjectName(), loaded.getProjectName());
        assertEquals(pd.getAppliances().size(), loaded.getAppliances().size());
    }

    @Test
    public void testCsvExportCreatesFile() throws Exception {
        com.solarcalc.model.ProjectData pd = new com.solarcalc.model.ProjectData();
        pd.addAppliance(new Appliance("A", 10, 1, 1));
        java.io.File tmp = java.io.File.createTempFile("out",".csv");
        tmp.deleteOnExit();
        com.solarcalc.io.CsvExporter.exportResults(pd, tmp);
        assertTrue(tmp.length() > 0);
    }
}
