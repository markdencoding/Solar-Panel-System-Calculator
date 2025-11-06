package com.solarcalc.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectData {
    private String projectName = "Untitled";
    private List<Appliance> appliances = new ArrayList<>();
    private SystemParameters params = new SystemParameters();

    public ProjectData() {}

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    public SystemParameters getParams() {
        return params;
    }

    public void setParams(SystemParameters params) {
        this.params = params;
    }

    public void addAppliance(Appliance a) {
        appliances.add(a);
    }

    public void removeAppliance(int index) {
        appliances.remove(index);
    }
}
