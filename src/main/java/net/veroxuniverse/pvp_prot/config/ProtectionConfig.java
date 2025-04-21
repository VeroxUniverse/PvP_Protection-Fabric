package net.veroxuniverse.pvp_prot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProtectionConfig {
    public static int protectionMinutes = 20;

    public static void load() {
        File file = new File("config/pvp_prot.json");

        try {
            if (!file.exists()) {
                createDefaultConfig(file);
                return;
            }

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(new FileReader(file), JsonObject.class);

            if (json.has("protection_minutes")) {
                protectionMinutes = json.get("protection_minutes").getAsInt();
            }

        } catch (Exception e) {
            System.err.println("[PvPProtection] Failed to load config: " + e.getMessage());
        }
    }

    public static void save() {
        File file = new File("config/pvp_prot.json");

        try {
            if (!file.exists()) {
                createDefaultConfig(file);
            }

            JsonObject json = new JsonObject();
            json.addProperty("protection_minutes", protectionMinutes);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(file);
            gson.toJson(json, writer);
            writer.close();
            System.out.println("[PvPProtection] Config updated at " + file.getPath());
        } catch (IOException e) {
            System.err.println("[PvPProtection] Failed to save config: " + e.getMessage());
        }
    }

    private static void createDefaultConfig(File file) {
        try {
            file.getParentFile().mkdirs();

            JsonObject defaultJson = new JsonObject();
            defaultJson.addProperty("protection_minutes", protectionMinutes);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(file);
            gson.toJson(defaultJson, writer);
            writer.close();

            System.out.println("[PvPProtection] Default config created at " + file.getPath());
        } catch (IOException e) {
            System.err.println("[PvPProtection] Failed to create default config: " + e.getMessage());
        }
    }
}
