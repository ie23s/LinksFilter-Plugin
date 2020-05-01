package com.ie23s.minecraft.plugin.linksfilter.utils;

public enum Error {
    CONNECTION_ERROR("CONNECTION_ERROR"),
    CUTTLY_BLACKLIST("CUTTLY_BLACKLIST"),
    UNKNOWN("UNKNOWN");

    private final String name;

    Error(String name) {
        this.name = name;
    }

    public static Error find(String name) {
        for (Error e : Error.values()) {
            if (e.name.equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }
}
