package com.ie23s.minecraft.plugin.linksfilter.command;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import com.ie23s.minecraft.plugin.linksfilter.utils.Error;
import org.bukkit.command.CommandSender;

public class BlackList {
    private final Main plugin;
    private final CommandSender sender;
    private final String[] args;

    public BlackList(Main plugin, CommandSender sender, String[] args) {
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;
    }

    public void exec() {
        if (args.length < 3) {
            sender.sendMessage(plugin.getLang().getMessage("help"));
            return;
        }

        switch (args[1]) {
            case "add":
                add(args[2]);
                break;
            case "remove":
                remove(args[2]);
                break;
            default:
                sender.sendMessage(plugin.getLang().getMessage("help"));

        }
    }

    private void add(String site) {
        if (!sender.hasPermission("linksfilter.blacklist.add")) {
            sender.sendMessage(plugin.getLang().getMessage("perm"));
            return;
        }

        try {
            if (plugin.getBlackList().addToList(site)) {
                sender.sendMessage(plugin.getLang().getMessage("added_black", site));
            } else {
                sender.sendMessage(plugin.getLang().getMessage("blacklisted", site));
            }
        } catch (Exception e) {
            if (Error.find(e.getMessage()) != null) {
                if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                    sender.sendMessage(plugin.getLang().getMessage("connection_error"));
            } else {
                sender.sendMessage(plugin.getLang().getMessage("unknown_error"));
                plugin.getInnerLogger().error(e.toString());
            }
        }
    }

    private void remove(String site) {
        if (!sender.hasPermission("linksfilter.blacklist.remove")) {
            sender.sendMessage(plugin.getLang().getMessage("perm"));
            return;
        }
        try {
            if (plugin.getBlackList().removeFromList(site)) {
                sender.sendMessage(plugin.getLang().getMessage("removed_black", site));
            } else {
                sender.sendMessage(plugin.getLang().getMessage("not_blacklisted", site));
            }
        } catch (Exception e) {
            if (Error.find(e.getMessage()) != null) {
                if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                    sender.sendMessage(plugin.getLang().getMessage("connection_error"));
            } else {
                sender.sendMessage(plugin.getLang().getMessage("unknown_error"));
                plugin.getInnerLogger().error(e.toString());
            }
        }
    }
}
