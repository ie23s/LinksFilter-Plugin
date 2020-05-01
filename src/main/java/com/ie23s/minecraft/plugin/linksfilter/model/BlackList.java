package com.ie23s.minecraft.plugin.linksfilter.model;

import java.sql.SQLException;

public interface BlackList {
    void init() throws SQLException;

    boolean addToList(String host) throws Exception;

    boolean removeFromList(String host) throws Exception;

    boolean findInList(String host, String... subHosts) throws Exception;
}
