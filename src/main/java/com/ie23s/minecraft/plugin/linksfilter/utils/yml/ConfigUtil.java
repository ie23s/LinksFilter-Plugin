package com.ie23s.minecraft.plugin.linksfilter.utils.yml;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ConfigUtil {
    private final JavaPlugin plugin;
    private final String filename;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigUtil(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        this.filename = filename;
        configFile = new File(plugin.getDataFolder(), filename);
    }


    public void loadConfiguration() {

        if (!this.configFile.exists()) {
            plugin.saveResource(filename, false);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = plugin.getResource(filename);
        if (defConfigStream != null) {
            this.fileConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
        }

    }

    public void saveConfig() {

        try {
            this.fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null)
            loadConfiguration();
        return fileConfiguration;
    }
}
