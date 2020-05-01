package com.ie23s.minecraft.plugin.linksfilter.model;

import java.sql.SQLException;

public abstract class ShortLink {
    public void init() throws SQLException {
    }

    public abstract String cutLink(String link, String name) throws Exception;

    public static String processURL(String link) {

        if (!link.startsWith("https://") && !link.startsWith("http://") && !link.startsWith("ftp://"))
            link = "http://" + link;
        return link;
    }
}
