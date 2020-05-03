package com.ie23s.minecraft.plugin.linksfilter.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Logger {
    private final JavaPlugin plugin;
    private final java.util.logging.Logger logger;

    public Logger(JavaPlugin plugin, java.util.logging.Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    @SuppressWarnings("unused")
    public void info(String msg) {
        logger.info(msg);
    }

    public void warn(String msg) {
        logger.warning(msg);
    }

    public void debug(String msg) {
        if (!plugin.getConfig().getBoolean("debug"))
            return;
        logger.log(Level.INFO, "[DEBUG] " + msg);
    }

    public void error(String msg) {
        logger.log(Level.WARNING, "[ERROR] " + msg);
    }
}
