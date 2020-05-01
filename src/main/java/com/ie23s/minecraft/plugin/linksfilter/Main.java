package com.ie23s.minecraft.plugin.linksfilter;

import com.ie23s.minecraft.plugin.linksfilter.command.Commands;
import com.ie23s.minecraft.plugin.linksfilter.event.ChatListener;
import com.ie23s.minecraft.plugin.linksfilter.model.BlackList;
import com.ie23s.minecraft.plugin.linksfilter.model.ShortLink;
import com.ie23s.minecraft.plugin.linksfilter.model.WhiteList;
import com.ie23s.minecraft.plugin.linksfilter.utils.Logger;
import com.ie23s.minecraft.plugin.linksfilter.utils.mysql.MySQLUtil;
import com.ie23s.minecraft.plugin.linksfilter.utils.yml.Lang;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
	private FileConfiguration config;
	private MySQLUtil mySQL;
	private Logger logger;
	private Lang lang;

	private BlackList blackList = null;
	private WhiteList whiteList = null;
	private ShortLink shortLink = null;

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		logger = new Logger(this, super.getLogger());
		reloadConfig();
		config = super.getConfig();
		lang = new Lang(this);

		lang.load(config.getString("linksfilter.lang"));

		initMySQL();
		initWhitelist();
		initBlacklist();
		initShortLinks();

		Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
		this.getCommand("linksfilter").setExecutor(new Commands(this));
	}

	@Override
	public void onDisable() {

		blackList = null;
		whiteList = null;
		shortLink = null;

		if (mySQL != null)
			mySQL.close();
		HandlerList.unregisterAll(this);
	}

	@Override
	public FileConfiguration getConfig() {
		return config;
	}

	private void initWhitelist() {
		if(!config.getBoolean("linkfilter.whitelist"))
			return;
		switch (config.getString("whitelist.mode").toLowerCase()) {
			case "mysql":
				whiteList = new com.ie23s.minecraft.plugin.linksfilter.utils.mysql.WhiteList(mySQL);
				break;
			case "linkfilter":
				whiteList = new com.ie23s.minecraft.plugin.linksfilter.utils.web.WhiteList(
						config.getString("linkfilter.api.url"),
						config.getString("linkfilter.api.key"),
						logger

				);
				break;
			default:
				config.set("whitelist.mode", "yml");
				this.saveConfig();
			case "yml":
				whiteList = new com.ie23s.minecraft.plugin.linksfilter.utils.yml.WhiteList(this);
		}
		try {
			whiteList.init();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	private void initBlacklist() {
        if (!config.getBoolean("linkfilter.blacklist") || config.getBoolean("linkfilter.whitelist"))
            return;
        switch (config.getString("blacklist.mode").toLowerCase()) {
            case "mysql":
                blackList = new com.ie23s.minecraft.plugin.linksfilter.utils.mysql.BlackList(mySQL);
                break;
            case "linkfilter":
				blackList = new com.ie23s.minecraft.plugin.linksfilter.utils.web.BlackList(
						config.getString("linkfilter.api.url"),
						config.getString("linkfilter.api.key"),
						logger

				);
				break;
			default:
				config.set("blacklist.mode", "yml");
				this.saveConfig();
			case "yml":
				blackList = new com.ie23s.minecraft.plugin.linksfilter.utils.yml.BlackList(this);
		}
		try {
			blackList.init();
		} catch (SQLException throwables) {
			logger.error(throwables.toString());
		}
	}

    private void initShortLinks() {
        if (!config.getBoolean("linkfilter.shortlinks"))
            return;
        switch (config.getString("shortlinks.mode").toLowerCase()) {
			case "cuttly":
				shortLink = new com.ie23s.minecraft.plugin.linksfilter.utils.web.CuttlyUtil(
						config.getString("shortlinks.cuttly.api_key"),
						logger
				);
				break;
			case "mysql":
				shortLink = new com.ie23s.minecraft.plugin.linksfilter.utils.mysql.ShortLink(mySQL,
						config.getString("shortlinks.mysql.url"));
				break;
			case "linkfilter":
				shortLink = new com.ie23s.minecraft.plugin.linksfilter.utils.web.ShortlinkAPIUtil(
						config.getString("linkfilter.api.url"),
						config.getString("linkfilter.api.key"),
						logger

				);
				break;
		}

		try {
			shortLink.init();
		} catch (SQLException throwables) {
			logger.error(throwables.toString());
		}
	}

    private void initMySQL() {
        if (!((config.getBoolean("linkfilter.blacklist") &&
                config.getString("blacklist.mode").equalsIgnoreCase("mysql")) ||
                (config.getBoolean("linkfilter.whitelist") &&
						config.getString("whitelist.mode").equalsIgnoreCase("mysql")) ||
				(config.getBoolean("linkfilter.shortlinks") &&
						config.getString("shortlinks.mode").equalsIgnoreCase("mysql"))
		)) return;
		mySQL = new MySQLUtil(config.getString("linkfilter.mysql.host"),
				config.getInt("linkfilter.mysql.port"),
				config.getString("linkfilter.mysql.user"),
				config.getString("linkfilter.mysql.password"),
				config.getString("linkfilter.mysql.database"),
				config.getString("linkfilter.mysql.prefix"));
		try {
			mySQL.connect();
		} catch (SQLException throwables) {
			logger.error(throwables.toString());
		}
	}

	public BlackList getBlackList() {
		return blackList;
	}

	public WhiteList getWhiteList() {
		return whiteList;
	}

	public ShortLink getShortLink() {
		return shortLink;
	}

	public Logger getInnerLogger() {
		return logger;
	}

	public Lang getLang() {
		return lang;
	}
}
