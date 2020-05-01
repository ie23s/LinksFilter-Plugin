package com.ie23s.minecraft.plugin.linksfilter.utils.yml;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class BlackList implements com.ie23s.minecraft.plugin.linksfilter.model.BlackList {
	private final JavaPlugin plugin;
	private List<String> list;
	private ConfigUtil configFile;

	public BlackList(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
    public void init() {
        configFile = new ConfigUtil(plugin, "blacklist.yml");
        list = configFile.getConfig().getStringList("list");
    }

    @Override
    public boolean addToList(String host) {
        if (list.contains(host))
            return false;
        list.add(host);
        configFile.getConfig().set("list", list);
        configFile.saveConfig();

        return true;
    }

    @Override
    public boolean removeFromList(String host) {
        if (!list.contains(host))
            return false;
        list.remove(host);
        configFile.getConfig().set("list", list);
        configFile.saveConfig();
        return true;
    }

    @Override
    public boolean findInList(String host, String... subHosts) {
        if (list.contains(host))
            return true;
        if (subHosts != null && subHosts.length > 0) {
            for (String subHost :
                    subHosts) {
                if (list.contains(subHost))
                    return true;
            }
        }
        return false;
    }
}
