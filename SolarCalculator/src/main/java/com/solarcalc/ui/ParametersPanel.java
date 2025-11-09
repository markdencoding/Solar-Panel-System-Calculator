package com.solarcalc.ui;

import com.solarcalc.model.ProjectData;
import com.solarcalc.model.SystemParameters;

import javax.swing.*;
import java.awt.*;

public class ParametersPanel extends JPanel {
    private ProjectData data;
    private Runnable onNext;
    private Runnable onBack;

    private JTextField pshField;
    private JComboBox<String> voltageCombo;
    private JTextField dodField;
    private JTextField daysField;
    private JTextField efficiencyField;

    public ParametersPanel(ProjectData data, Runnable onNext, Runnable onBack) {
        this.data = data;
        this.onNext = onNext;
        this.onBack = onBack;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        pshField = new JTextField();
        voltageCombo = new JComboBox<>(new String[]{"12","24","48"});
        dodField = new JTextField();
        daysField = new JTextField();
        efficiencyField = new JTextField();

        form.add(new JLabel("Peak Sun Hours (hours/day):")); form.add(pshField);
        form.add(new JLabel("System Voltage (V):")); form.add(voltageCombo);
        form.add(new JLabel("Depth of Discharge (%):")); form.add(dodField);
        form.add(new JLabel("Days of Autonomy:")); form.add(daysField);
        form.add(new JLabel("System Efficiency (0-1):")); form.add(efficiencyField);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back");
        JButton calcBtn = new JButton("Calculate & Show Results");
        buttons.add(backBtn);
        buttons.add(calcBtn);
        add(buttons, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> onBack.run());
        calcBtn.addActionListener(e -> {
            if (saveToModel()) onNext.run();
        });
    }

    public void refreshFromModel() {
        SystemParameters p = data.getParams();
        pshField.setText(String.valueOf(p.getPeakSunHours()));
        voltageCombo.setSelectedItem(String.valueOf(p.getSystemVoltage()));
        dodField.setText(String.valueOf(p.getDepthOfDischargePercent()));
        daysField.setText(String.valueOf(p.getDaysOfAutonomy()));
        efficiencyField.setText(String.valueOf(p.getEfficiency()));
    }

    private boolean saveToModel() {
        try {
            double psh = Double.parseDouble(pshField.getText().trim());
            int voltage = Integer.parseInt((String)voltageCombo.getSelectedItem());
            double dod = Double.parseDouble(dodField.getText().trim());
            int days = Integer.parseInt(daysField.getText().trim());
            double eff = Double.parseDouble(efficiencyField.getText().trim());
            if (psh <= 0 || dod <= 0 || dod > 100 || days <= 0 || eff <= 0 || eff > 1) throw new NumberFormatException();
            SystemParameters p = data.getParams();
            p.setPeakSunHours(psh);
            p.setSystemVoltage(voltage);
            p.setDepthOfDischargePercent(dod);
            p.setDaysOfAutonomy(days);
            p.setEfficiency(eff);
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid parameters. Check values and ranges:\nPSH>0, 0<DoD<=100, days>0, 0<eff<=1");
            return false;
        }
    }
}
