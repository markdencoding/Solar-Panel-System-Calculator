package com.solarcalc.ui;

import com.solarcalc.model.ProjectData;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);

    private ProjectData projectData = new ProjectData();

    private LoadInputPanel loadInputPanel;
    private ParametersPanel parametersPanel;
    private ResultsPanel resultsPanel;

    public MainFrame() {
        setTitle("Solar Power System Calculator");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        loadInputPanel = new LoadInputPanel(projectData, this::showParameters);
        parametersPanel = new ParametersPanel(projectData, this::showResults, this::showLoadInput);
        resultsPanel = new ResultsPanel(projectData, this::showParameters);

        cards.add(loadInputPanel, "load");
        cards.add(parametersPanel, "params");
        cards.add(resultsPanel, "results");

        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem save = new JMenuItem("Save Project");
        JMenuItem load = new JMenuItem("Load Project");
        JMenuItem export = new JMenuItem("Export CSV");
        JMenuItem exit = new JMenuItem("Exit");

        save.addActionListener(e -> loadInputPanel.saveProject());
        load.addActionListener(e -> loadInputPanel.loadProject());
        export.addActionListener(e -> resultsPanel.exportCsv());
        exit.addActionListener(e -> dispose());

        file.add(save);
        file.add(load);
        file.add(export);
        file.addSeparator();
        file.add(exit);
        menuBar.add(file);
        setJMenuBar(menuBar);
    }

    public void showParameters() {
        parametersPanel.refreshFromModel();
        cardLayout.show(cards, "params");
    }

    public void showLoadInput() {
        loadInputPanel.refreshTable();
        cardLayout.show(cards, "load");
    }

    public void showResults() {
        resultsPanel.calculateAndShow();
        cardLayout.show(cards, "results");
    }
}
