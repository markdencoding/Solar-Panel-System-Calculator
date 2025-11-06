package com.solarcalc.ui;

import com.solarcalc.calc.SolarCalculator;
import com.solarcalc.io.CsvExporter;
import com.solarcalc.model.ProjectData;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class ResultsPanel extends JPanel {
    private ProjectData data;
    private Runnable onBack;

    private JLabel energyLabel = new JLabel();
    private JLabel panelLabel = new JLabel();
    private JLabel batteryLabel = new JLabel();
    private JLabel inverterLabel = new JLabel();
    private JLabel controllerLabel = new JLabel();

    private DecimalFormat df = new DecimalFormat("#,##0.##");

    public ResultsPanel(ProjectData data, Runnable onBack) {
        this.data = data;
        this.onBack = onBack;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(0,1,8,8));
        center.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        center.add(energyLabel);
        center.add(panelLabel);
        center.add(batteryLabel);
        center.add(inverterLabel);
        center.add(controllerLabel);

        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back");
        JButton exportBtn = new JButton("Export CSV");
        buttons.add(backBtn);
        buttons.add(exportBtn);
        add(buttons, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> onBack.run());
        exportBtn.addActionListener(e -> exportCsv());
    }

    public void calculateAndShow() {
        double totalWh = SolarCalculator.totalDailyEnergyWh(data);
        double panelW = SolarCalculator.requiredPanelW(data);
        double batteryAh = SolarCalculator.requiredBatteryAh(data);
        double inverterW = SolarCalculator.recommendedInverterW(data);
        double controllerA = SolarCalculator.recommendedControllerA(data);

        energyLabel.setText("Total daily energy: " + df.format(totalWh) + " Wh/day");
        panelLabel.setText("Required solar array: " + df.format(panelW) + " W (round up to nearest panel)");
        batteryLabel.setText("Required battery capacity: " + df.format(batteryAh) + " Ah @ " + data.getParams().getSystemVoltage() + "V");
        inverterLabel.setText("Recommended inverter size: " + df.format(inverterW) + " W");
        controllerLabel.setText("Recommended charge controller: " + df.format(controllerA) + " A");
    }

    public void exportCsv() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                CsvExporter.exportResults(data, f);
                JOptionPane.showMessageDialog(this, "Exported CSV: " + f.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to export: " + ex.getMessage());
            }
        }
    }
}
