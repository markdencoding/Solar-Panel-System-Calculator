package com.solarcalc.ui;

import com.solarcalc.io.JsonPersistence;
import com.solarcalc.model.Appliance;
import com.solarcalc.model.ProjectData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class LoadInputPanel extends JPanel {
    private ProjectData data;
    private JTable table;
    private DefaultTableModel tableModel;

    public LoadInputPanel(ProjectData data, Runnable onNext) {
        this.data = data;
        initUI(onNext);
    }

    private void initUI(Runnable onNext) {
        setLayout(new BorderLayout());
        // Top: toolbar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Appliance");
        JButton editBtn = new JButton("Edit Selected");
        JButton delBtn = new JButton("Delete Selected");
        JButton nextBtn = new JButton("Next: Parameters");

        top.add(addBtn);
        top.add(editBtn);
        top.add(delBtn);
        top.add(nextBtn);

        add(top, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"Name","Watts (W)","Qty","Hours/day","Daily Energy (Wh)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save Project");
        JButton loadBtn = new JButton("Load Project");
        bottom.add(loadBtn);
        bottom.add(saveBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showApplianceDialog(-1));
        editBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel >= 0) showApplianceDialog(sel);
            else JOptionPane.showMessageDialog(this, "Select a row to edit");
        });
        delBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel >= 0) {
                data.removeAppliance(sel);
                refreshTable();
            } else JOptionPane.showMessageDialog(this, "Select a row to delete");
        });
        nextBtn.addActionListener(e -> onNext.run());

        saveBtn.addActionListener(e -> saveProject());
        loadBtn.addActionListener(e -> loadProject());
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Appliance a : data.getAppliances()) {
            Vector<Object> row = new Vector<>();
            row.add(a.getName());
            row.add(a.getWatts());
            row.add(a.getQuantity());
            row.add(a.getHoursPerDay());
            row.add(a.dailyEnergyWh());
            tableModel.addRow(row);
        }
    }

    private void showApplianceDialog(int editIndex) {
        JTextField nameF = new JTextField(20);
        JTextField wattsF = new JTextField(8);
        JTextField qtyF = new JTextField(4);
        JTextField hoursF = new JTextField(6);

        if (editIndex >= 0) {
            Appliance a = data.getAppliances().get(editIndex);
            nameF.setText(a.getName());
            wattsF.setText(String.valueOf(a.getWatts()));
            qtyF.setText(String.valueOf(a.getQuantity()));
            hoursF.setText(String.valueOf(a.getHoursPerDay()));
        }

        JPanel p = new JPanel(new GridLayout(0,2));
        p.add(new JLabel("Name:")); p.add(nameF);
        p.add(new JLabel("Watts (W):")); p.add(wattsF);
        p.add(new JLabel("Quantity:")); p.add(qtyF);
        p.add(new JLabel("Hours per day:")); p.add(hoursF);

        int res = JOptionPane.showConfirmDialog(this, p, editIndex>=0?"Edit Appliance":"Add Appliance", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String name = nameF.getText().trim();
                double watts = Double.parseDouble(wattsF.getText().trim());
                int qty = Integer.parseInt(qtyF.getText().trim());
                double hours = Double.parseDouble(hoursF.getText().trim());
                if (name.isEmpty() || watts < 0 || qty < 0 || hours < 0) throw new NumberFormatException();
                Appliance a = new Appliance(name, watts, qty, hours);
                if (editIndex >= 0) data.getAppliances().set(editIndex, a);
                else data.addAppliance(a);
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter non-negative numbers and a name.");
            }
        }
    }

    public void saveProject() {
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                JsonPersistence.save(data, f);
                JOptionPane.showMessageDialog(this, "Saved successfully: " + f.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage());
            }
        }
    }

    public void loadProject() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                com.solarcalc.model.ProjectData loaded = JsonPersistence.load(f);
                // copy into current model
                data.setProjectName(loaded.getProjectName());
                data.setAppliances(loaded.getAppliances());
                data.setParams(loaded.getParams());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Loaded successfully: " + f.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage());
            }
        }
    }
}
