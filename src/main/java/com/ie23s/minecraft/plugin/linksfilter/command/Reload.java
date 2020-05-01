package com.ie23s.minecraft.plugin.linksfilter.command;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import org.bukkit.command.CommandSender;

public class Reload {
    private final Main plugin;
    private final CommandSender sender;

    Reload(Main plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    void exec() {
        if (!sender.hasPermission("linksfilter.reload")) {
            sender.sendMessage(plugin.getLang().getMessage("perm"));
            return;
        }

        String error = plugin.getLang().getMessage("plugin_not_reloaded");

        try {
            plugin.onDisable();
            plugin.onEnable();
            sender.sendMessage(plugin.getLang().getMessage("plugin_reloaded"));
        } catch (Exception e) {
            sender.sendMessage(error);
            e.printStackTrace();
        }
    }
}
