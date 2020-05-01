package com.ie23s.minecraft.plugin.linksfilter.utils.web;

import junit.framework.TestCase;

public class CuttlyUtilTest extends TestCase {

	public void testCutLink() {
        CuttlyUtil cuttlyUtil = new CuttlyUtil("e55e962361364b2aac607e6004f1056f9ebb1", null);
        try {
            System.out.println(toString() + cuttlyUtil.cutLink("https://google.com/", "Test"));
            System.out.println(toString() + " [test] is OK.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(toString() + " [test] is FAILED.");
        }
    }
}