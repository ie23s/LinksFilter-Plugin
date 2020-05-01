package com.ie23s.minecraft.plugin.linksfilter.utils;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {
    private final static String URL_REGEX = "(http://|ftp://|https://)?" +
            "([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?";

    private final String text;
    private URL[] urls;

    public URLUtil(String text) {
        this.text = text;
        parseURLs();
    }

	public URL[] getURLs() {
		return urls;
	}

	private void parseURLs() {
		Pattern pattern = Pattern.compile(URL_REGEX);
		Matcher matcher = pattern.matcher(text);

		int count = 0;
		while (matcher.find())
			count++;
		matcher.reset();

        urls = new URL[count];

        for (int i = 0; matcher.find(); i++) {
            String url = text.substring(matcher.start(0), matcher.end(0));
            String hostname = text.substring(matcher.start(2), matcher.end(2));
            urls[i] = new URL(url, hostname);
        }
    }


    public static final class URL {
        private final String url;
        private final String hostname;
        private String[] subHosts = null;

        public URL(String url, String hostname) {
            this.url = url;
            this.hostname = hostname;
        }

        public String getUrl() {
            return url;
        }

		public String getHostname() {
			return hostname;
		}

		@Nullable
		public String[] getSubHosts() {
			return subHosts;
		}

		public void generateSubHosts() {
			String[] parts = hostname.split("\\.");
			int length = parts.length;

			subHosts = new String[length - 1];
			StringBuilder last = new StringBuilder(parts[length - 1]);
			for(int i = length - 2, j = 0; i >= 0; i--, j++) {
				last.insert(0, parts[i] + ".");
				subHosts[j] = "*." + last.toString();
			}
		}
	}
}
