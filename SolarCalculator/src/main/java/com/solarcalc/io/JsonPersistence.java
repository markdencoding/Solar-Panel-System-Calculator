package com.solarcalc.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solarcalc.model.ProjectData;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonPersistence {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(ProjectData data, File file) throws IOException {
        try (FileWriter fw = new FileWriter(file)) {
            GSON.toJson(data, fw);
        }
    }

    public static ProjectData load(File file) throws IOException {
        try (FileReader fr = new FileReader(file)) {
            return GSON.fromJson(fr, ProjectData.class);
        }
    }
}
