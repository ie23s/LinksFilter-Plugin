package com.ie23s.minecraft.plugin.linksfilter.utils.yml;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import org.bukkit.plugin.java.JavaPlugin;

public class Lang {
    private final JavaPlugin plugin;
    private ConfigUtil configUtil;

    public Lang(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load(String lang) {
        configUtil = new ConfigUtil(plugin, "lang/" + lang + ".yml");

        try {
            configUtil.loadConfiguration();
        } catch (Exception e) {
            ((Main) plugin).getInnerLogger().warn("Something wrong, loading default language!");
            loadDef();
            e.printStackTrace();
        }
    }

    private void loadDef() {
        configUtil = new ConfigUtil(plugin, "lang/en.yml");

        try {
            configUtil.loadConfiguration();
        } catch (Exception e) {
            ((Main) plugin).getInnerLogger().error("Filed loading default language.");
        }
    }

    public String getMessage(String target) {
        return configUtil.getConfig().getString(target);
    }

    public String getMessage(String target, Object... args) {
        return String.format(configUtil.getConfig().getString(target), args);
    }
}
