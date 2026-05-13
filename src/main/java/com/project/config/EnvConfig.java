package com.project.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = initDotenv();

    private static Dotenv initDotenv() {
        if (new java.io.File(".env").exists()) {
            return Dotenv.configure().load();
        }
        if (new java.io.File("Proyecto_RH/.env").exists()) {
            return Dotenv.configure().directory("./Proyecto_RH").load();
        }
        return Dotenv.configure().ignoreIfMissing().load();
    }


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
