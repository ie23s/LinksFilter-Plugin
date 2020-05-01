package com.ie23s.minecraft.plugin.linksfilter.utils.yml;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;

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


    @SuppressWarnings("deprecation")
    public void loadConfiguration() {

        if (!this.configFile.exists()) {
            plugin.saveResource(filename, false);
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        InputStream defConfigStream = plugin.getResource(filename);
        if (defConfigStream != null) {
            YamlConfiguration defConfig;
            if (!plugin.getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8) &&
                    !FileConfiguration.UTF8_OVERRIDE) {
                defConfig = new YamlConfiguration();

                byte[] contents = new byte[0];
                try {
                    //noinspection UnstableApiUsage
                    contents = ByteStreams.toByteArray(defConfigStream);
                } catch (IOException var7) {
                    plugin.getLogger().log(Level.SEVERE, "Unexpected failure reading " + filename, var7);
                }

                String text = new String(contents, Charset.defaultCharset());
                if (!text.equals(new String(contents, Charsets.UTF_8))) {
                    plugin.getLogger().warning("Default system encoding may have misread " + filename +
                            " from plugin" + "jar");
                }

                try {
                    defConfig.loadFromString(text);
                } catch (InvalidConfigurationException var6) {
                    plugin.getLogger().log(Level.SEVERE, "Cannot load configuration from jar", var6);
                }
            } else {
                defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
            }

            this.fileConfiguration.setDefaults(defConfig);
        }

    }

    public void saveConfig() {

        try {
            this.fileConfiguration.save(configFile);
        } catch (IOException e) {
            //TODO log
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null)
            loadConfiguration();
        return fileConfiguration;
    }
}
