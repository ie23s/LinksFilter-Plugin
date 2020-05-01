package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import org.junit.Test;

import java.io.IOException;


public class HTTPUtilTest {

    @Test
    public void testExecute() throws Exception {
        HTTPUtil HTTPUtil = new HTTPUtil("https://raw.githubusercontent.com/ie23s/LinksFilter/tests/1/test-standart?token=ADBE5QKDFMSXSUS6B6G3MPK6T5BN4");

        try {
            HTTPUtil.execute();
            System.out.println(toString() + " [exec] is OK.");
        } catch (IOException e) {
            System.out.println(toString() + " [exec] is FAILED.");
            throw new Exception("Connection error!");
        }


        if (HTTPUtil.getResponse().equalsIgnoreCase("test is OK")) {
            System.out.println(toString() + " [response] is OK.");
        } else {
            System.out.println(toString() + " [response] is FAILED.");
            throw new Exception("Error!");
        }


        HTTPUtil = new HTTPUtil("https://raw.githubusercontent.com/ie23s/LinksFilter/tests/1/test-json?token=ADBE5QPXVSARNTM3GYG65LK6UYXPG");

        try {
            HTTPUtil.execute();
            System.out.println(toString() + " [json-exec] is OK.");
        } catch (IOException e) {
            System.out.println(toString() + " [json-exec] is FAILED.");
            throw new Exception("Connection error!");
        }

        if (HTTPUtil.getJSONResponse().getAsJsonArray("test").get(1).getAsString().equals("OK")) {
            System.out.println(toString() + " [json-response] is OK.");
        } else {
            System.out.println(toString() + " [json-response] is FAILED.");
            throw new Exception("Error!");
        }
    }

}