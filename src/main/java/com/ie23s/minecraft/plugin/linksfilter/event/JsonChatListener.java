package com.ie23s.minecraft.plugin.linksfilter.event;

import com.ie23s.minecraft.plugin.linksfilter.Main;
import com.ie23s.minecraft.plugin.linksfilter.model.BlackList;
import com.ie23s.minecraft.plugin.linksfilter.model.ShortLink;
import com.ie23s.minecraft.plugin.linksfilter.model.WhiteList;
import com.ie23s.minecraft.plugin.linksfilter.utils.URLUtil;
import com.ie23s.minecraft.plugin.linksfilter.utils.yml.Lang;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class JsonChatListener implements Listener {
    private final Main plugin;
    private final Lang lang;

    private final BlackList blackList;
    private final WhiteList whiteList;


    public JsonChatListener(@NotNull Main plugin) {
        this.plugin = plugin;

        lang = plugin.getLang();

        blackList = plugin.getBlackList();
        whiteList = plugin.getWhiteList();
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        String message = event.getMessage();

        URLUtil urlUtil = new URLUtil(message);
        URLUtil.URL[] urls = urlUtil.getURLs();
        if (urls == null || urls.length == 0)
            return;

        event.setCancelled(true);

        Object[] created_links = new String[urls.length];

        new Thread(() -> {
            int i = 0;
            String msg = "[\"" + String.format(event.getFormat(), event.getPlayer().getName(), message) + "\"]";
            for (URLUtil.URL url :
                    urls) {
                if (ChatListener.checkLists(plugin, event, url))
                    return;
                created_links[i++] = "\", {\"text\":\"" + url.getHostname() + "\"," +
                        "\"color\":\"blue\",\"bold\":true,\"underlined\":true,\"" +
                        "clickEvent\":{\"action\":\"open_url\",\"value\":\"" + ShortLink.processURL(url.getUrl()) + "\"}," +
                        "\"hoverEvent\":{\"action\":\"show_text\",\"value\":" +
                        "\"" + lang.getMessage("danger") + "\"}}, \"";
                msg = msg.replace(url.getUrl(), "%s");

            }

            msg = String.format(msg, created_links);

            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(msg),
                    ChatMessageType.CHAT);
            for (Player p : event.getRecipients())
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }).start();
    }
}
