package com.ie23s.minecraft.plugin.linksfilter.event;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import com.ie23s.minecraft.plugin.linksfilter.model.BlackList;
import com.ie23s.minecraft.plugin.linksfilter.model.ShortLink;
import com.ie23s.minecraft.plugin.linksfilter.model.WhiteList;
import com.ie23s.minecraft.plugin.linksfilter.utils.Error;
import com.ie23s.minecraft.plugin.linksfilter.utils.URLUtil;
import com.ie23s.minecraft.plugin.linksfilter.utils.yml.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChatListener implements Listener {
    private final Main plugin;
    private final Lang lang;

    private final BlackList blackList;
    private final WhiteList whiteList;
    private final ShortLink shortLink;

    public ChatListener(@NotNull Main plugin) {
        this.plugin = plugin;

        lang = plugin.getLang();

        blackList = plugin.getBlackList();
        whiteList = plugin.getWhiteList();
        shortLink = plugin.getShortLink();
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        URLUtil urlUtil = new URLUtil(message);
        URLUtil.URL[] urls = urlUtil.getURLs();
        if (urls == null)
            return;
        if (shortLink != null) {
            event.getPlayer().sendMessage(lang.getMessage("cutting"));
        }
        for (URLUtil.URL url :
                urls) {
            if (blackList != null || whiteList != null)
                url.generateSubHosts();

            if (whiteList != null) {
                try {
                    if (!whiteList.findInList(url.getHostname(), url.getSubHosts())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(lang.getMessage("not_whitelisted", url.getHostname()));
                        return;
                    }
                } catch (Exception e) {
                    if (Error.find(e.getMessage()) != null) {
                        if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                            event.getPlayer().sendMessage(lang.getMessage("connection_error"));
                    } else {
                        event.getPlayer().sendMessage(lang.getMessage("unknown_error"));
                        plugin.getInnerLogger().error(e.toString());
                    }
                }
            } else if (blackList != null) {
                try {
                    if (blackList.findInList(url.getHostname(), url.getSubHosts())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(lang.getMessage("blacklisted", url.getHostname()));
                        return;
                    }
                } catch (Exception e) {
                    if (Error.find(e.getMessage()) != null) {
                        if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                            event.getPlayer().sendMessage(lang.getMessage("connection_error"));

                    } else {
                        event.getPlayer().sendMessage(lang.getMessage("unknown_error"));
                        plugin.getInnerLogger().error(e.toString());
                        e.printStackTrace();
                    }
                }
            }


            if (shortLink != null) {
                try {
                    message = message.replaceFirst(url.getUrl(),
                            shortLink.cutLink(url.getUrl(), event.getPlayer().getDisplayName()));
                } catch (Exception e) {
                    if (Error.find(e.getMessage()) != null) {
                        if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                            event.getPlayer().sendMessage(lang.getMessage("connection_error"));

                        if (Error.CUTTLY_BLACKLIST.equals(e.getMessage()))
                            event.getPlayer().sendMessage(lang.getMessage("blacklisted", url.getHostname()));
                    } else {
                        event.getPlayer().sendMessage(lang.getMessage("unknown_error"));
                        plugin.getInnerLogger().error(e.toString());
                        e.printStackTrace();
                    }
                }

            }
        }
		event.setMessage(message);
	}
}
