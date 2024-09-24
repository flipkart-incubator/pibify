package com.flipkart.pibify.core;

/**
 * This class is used for configuring pibify
 * Author bageshwar.pn
 * Date 24/09/24
 */
public class PibifyConfiguration {

    private static PibifyConfiguration INSTANCE;
    private boolean ignoreUnknownFields = true;
    private boolean ignoreUnknownEnums = true;

    private PibifyConfiguration() {
    }

    public static PibifyConfiguration instance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Attempt to access configuration before initialization");
        }
        return INSTANCE;
    }

    public static PibifyConfiguration.Builder builder() {
        return new Builder();
    }

    public boolean ignoreUnknownFields() {
        return ignoreUnknownFields;
    }

    public boolean ignoreUnknownEnums() {
        return ignoreUnknownEnums;
    }

    public static class Builder {
        private final PibifyConfiguration config;

        public Builder() {
            config = new PibifyConfiguration();
        }

        synchronized public void build() {
            // A way to keep the static instance pseudo-final
            if (PibifyConfiguration.INSTANCE != null) {
                throw new IllegalStateException("Re-creating pibify configuration");
            }
            PibifyConfiguration.INSTANCE = config;
        }

        public Builder ignoreUnknownFields(boolean flag) {
            config.ignoreUnknownFields = flag;
            return this;
        }

        public Builder ignoreUnknownEnums(boolean flag) {
            config.ignoreUnknownEnums = flag;
            return this;
        }
    }

}
