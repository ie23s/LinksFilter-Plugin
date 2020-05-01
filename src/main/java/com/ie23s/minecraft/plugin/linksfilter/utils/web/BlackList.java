package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import com.google.gson.JsonObject;
import com.ie23s.minecraft.plugin.linksfilter.utils.Logger;

import java.io.IOException;

import static com.ie23s.minecraft.plugin.linksfilter.utils.Error.CONNECTION_ERROR;
import static com.ie23s.minecraft.plugin.linksfilter.utils.Error.UNKNOWN;

public class BlackList implements com.ie23s.minecraft.plugin.linksfilter.model.BlackList {
    private final String api_url;
    private final Logger logger;

    public BlackList(String api_url, String token, Logger logger) {
        this.api_url = api_url + "?do=blacklist&api_key=" + token;
        this.logger = logger;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean addToList(String host) throws Exception {
        String link = api_url + "&func=add&host=" + host;

        HTTPUtil HTTPUtil = new HTTPUtil(link);

        try {
            HTTPUtil.execute();
        } catch (IOException e) {
            logger.debug(e.toString());
            throw new Exception(CONNECTION_ERROR.toString());
        }

        JsonObject jsonObject = HTTPUtil.getJSONResponse();

        if (jsonObject.getAsJsonPrimitive("status").getAsInt() == 5) {
            return false;
        }
        if (jsonObject.getAsJsonPrimitive("status").getAsInt() != 1) {
            throw new Exception(UNKNOWN.toString());
        }

        return true;
    }

    @Override
    public boolean removeFromList(String host) throws Exception {
        String link = api_url + "&func=rem&host=" + host;

        HTTPUtil HTTPUtil = new HTTPUtil(link);

        try {
            HTTPUtil.execute();
        } catch (IOException e) {
            logger.debug(e.toString());
            throw new Exception(CONNECTION_ERROR.toString());
        }

        JsonObject jsonObject = HTTPUtil.getJSONResponse();

        if (jsonObject.getAsJsonPrimitive("status").getAsInt() == 6) {
            return false;
        }
        if (jsonObject.getAsJsonPrimitive("status").getAsInt() != 1) {
            throw new Exception(UNKNOWN.toString());
        }

        return true;
    }

    @Override
    public boolean findInList(String host, String... subHosts) throws Exception {
        String link = api_url + "&func=check&host=" + host + "&subhosts=" +
                String.join("|", subHosts);

        HTTPUtil HTTPUtil = new HTTPUtil(link);

        try {
            HTTPUtil.execute();
        } catch (IOException e) {
            logger.debug(e.toString());
            throw new Exception(CONNECTION_ERROR.toString());
        }

        JsonObject jsonObject = HTTPUtil.getJSONResponse();

        if (jsonObject.getAsJsonPrimitive("status").getAsInt() != 1) {
            throw new Exception(UNKNOWN.toString());
        }
        return jsonObject.getAsJsonObject("options").getAsJsonPrimitive("found").getAsBoolean();
    }
}
