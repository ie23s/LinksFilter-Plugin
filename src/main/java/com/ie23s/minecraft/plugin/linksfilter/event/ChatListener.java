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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatListener implements Listener {
    private final Main plugin;
    private final Lang lang;

    private final ShortLink shortLink;

    private final List<String> uuids = new ArrayList<>();

    public ChatListener(@NotNull Main plugin) {
        this.plugin = plugin;

        lang = plugin.getLang();

        shortLink = plugin.getShortLink();
    }

    static boolean checkLists(Main plugin, AsyncPlayerChatEvent event, URLUtil.URL url) {
        final BlackList blackList = plugin.getBlackList();
        final WhiteList whiteList = plugin.getWhiteList();

        final Lang lang = plugin.getLang();

        if (blackList != null || whiteList != null)
            url.generateSubHosts();

        if (whiteList != null) {
            try {
                if (!whiteList.findInList(url.getHostname(), url.getSubHosts())) {
                    event.getPlayer().sendMessage(lang.getMessage("not_whitelisted", url.getHostname()));
                    return true;
                }
            } catch (Exception e) {
                if (Error.find(e.getMessage()) != null) {
                    if (Error.CONNECTION_ERROR.equals(e.getMessage()))
                        event.getPlayer().sendMessage(lang.getMessage("connection_error"));
                } else {
                    event.getPlayer().sendMessage(lang.getMessage("unknown_error"));
                    plugin.getInnerLogger().error(e.toString());
                }
                return true;
            }
        } else if (blackList != null) {
            try {
                if (blackList.findInList(url.getHostname(), url.getSubHosts())) {
                    event.getPlayer().sendMessage(lang.getMessage("blacklisted", url.getHostname()));
                    return true;
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
                return true;
            }
        }

        return false;
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.getRecipients();

        String message = event.getMessage();

        String[] messParts = message.split("#");
        String messUUID = messParts[messParts.length - 1];

        if (uuids.contains(messUUID)) {
            uuids.remove(messUUID);
            event.setMessage(message.replace("#" + messUUID, ""));
            return;
        }

        URLUtil urlUtil = new URLUtil(message);
        URLUtil.URL[] urls = urlUtil.getURLs();
        if (urls == null || urls.length == 0)
            return;

        if (shortLink != null) {
            event.getPlayer().sendMessage(lang.getMessage("cutting"));
        }
        event.setCancelled(true);
        new Thread(() -> {

            String msg = message;
            for (URLUtil.URL url :
                    urls) {

                if (checkLists(plugin, event, url))
                    return;

                if (shortLink != null) {
                    try {
                        msg = msg.replaceFirst(url.getUrl(),
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
                        return;
                    }

                }
            }
            String uuid = UUID.randomUUID().toString();
            uuids.add(uuid);
            event.getPlayer().chat(msg + "#" + uuid);
        }).start();
	}
}
