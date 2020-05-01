package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import com.google.gson.JsonObject;
import com.ie23s.minecraft.plugin.linksfilter.model.ShortLink;
import com.ie23s.minecraft.plugin.linksfilter.utils.Logger;

import java.io.IOException;

import static com.ie23s.minecraft.plugin.linksfilter.utils.Error.*;

public class CuttlyUtil extends ShortLink {
    private final String token;
    private final Logger logger;

    public CuttlyUtil(String token, Logger logger) {
        this.token = token;
        this.logger = logger;
    }

    @Override
    public void init() {

    }

	@Override
	public String cutLink(String link, String name) throws IOException {
        HTTPUtil HTTPUtil = new HTTPUtil(getUrl(link));

        try {
            HTTPUtil.execute();
        } catch (IOException e) {
            logger.debug(e.toString());
            throw new IOException(CONNECTION_ERROR.toString());
        }

        JsonObject jsonObject = HTTPUtil.getJSONResponse();

        if (jsonObject.getAsJsonObject("url").getAsJsonPrimitive("status").getAsInt() == 6) {
            throw new IOException(CUTTLY_BLACKLIST.toString());
        }

        if (jsonObject.getAsJsonObject("url").getAsJsonPrimitive("status").getAsInt() != 7) {
            throw new IOException(UNKNOWN.toString());
        }
        return jsonObject.getAsJsonObject("url").get("shortLink").getAsString();
    }

	private String getUrl(String link) {
        return "https://cutt.ly/api/api.php?key=" + token + "&short=" +
                processURL(link);
    }
}
