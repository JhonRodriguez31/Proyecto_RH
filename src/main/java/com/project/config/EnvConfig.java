package com.project.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();


    private EnvConfig() {
    }

    public static String get(String key) {
        String systemValue = System.getenv(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return dotenv.get(key);
    }

    public static String get(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }

}
