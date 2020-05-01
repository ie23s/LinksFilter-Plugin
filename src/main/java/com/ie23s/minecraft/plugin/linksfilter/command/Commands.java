package com.ie23s.minecraft.plugin.linksfilter.command;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    private final Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length == 0) {

            commandSender.sendMessage(plugin.getLang().getMessage("help"));
            return true;
        }
        switch (args[0]) {
            case "whitelist":
                if(plugin.getWhiteList() == null) {
                    commandSender.sendMessage(plugin.getLang().getMessage("white_disabled"));
                    return true;
                }
                new WhiteList(plugin, commandSender, args).exec();
                break;
            case "blacklist":
                if(plugin.getBlackList() == null) {
                    commandSender.sendMessage(plugin.getLang().getMessage("black_disabled"));
                    return true;
                }
                new BlackList(plugin, commandSender, args).exec();
                break;
            case "reload":
                new Reload(plugin, commandSender).exec();
                break;
            default:
                commandSender.sendMessage(plugin.getLang().getMessage("help"));
        }
        return true;
    }
}
