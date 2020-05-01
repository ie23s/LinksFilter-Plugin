package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPUtil {
	private final URL url;
	private String response;

	public HTTPUtil(String url) {
		URL tempURL = null;
		try {
			tempURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.url = tempURL;
	}

	public void execute() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
		StringBuilder builder = new StringBuilder();
		for (String line; (line = reader.readLine()) != null; ) {
			builder.append(line);
		}
		response = builder.toString();
	}

	public String getResponse() {
		return response;
	}

	public JsonObject getJSONResponse() {
		return new JsonParser().parse(response).getAsJsonObject();
	}

}
