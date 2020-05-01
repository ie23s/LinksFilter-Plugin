package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import com.google.gson.JsonObject;
import com.ie23s.minecraft.plugin.linksfilter.model.ShortLink;
import com.ie23s.minecraft.plugin.linksfilter.utils.Logger;

import java.io.IOException;

import static com.ie23s.minecraft.plugin.linksfilter.utils.Error.CONNECTION_ERROR;
import static com.ie23s.minecraft.plugin.linksfilter.utils.Error.UNKNOWN;

public class ShortlinkAPIUtil extends ShortLink {
    private final String token;
    private final String api_url;
    private final Logger logger;

    public ShortlinkAPIUtil(String api_url, String token, Logger logger) {
        this.token = token;
        this.api_url = api_url;
        this.logger = logger;
    }

    @Override
    public void init() {

    }

    @Override
    public String cutLink(String link, String name) throws Exception {
        HTTPUtil HTTPUtil = new HTTPUtil(getUrl(link, name));

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
        return jsonObject.getAsJsonObject("options").get("link").getAsString();
    }

	private String getUrl(String link, String name) {
        return api_url + "?do=shortlinks&api_key=" + token + "&link=" +
                processURL(link) + "&username=" + name;
    }
}
