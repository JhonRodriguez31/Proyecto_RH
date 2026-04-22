package com.project.common.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class ViewLoader {
    private ViewLoader() {
    }

    public static Parent load(String absoluteResourcePath) {
        URL url = ViewLoader.class.getResource(absoluteResourcePath);

        if (url == null) {
            throw new IllegalArgumentException("FXML no encontrado" + absoluteResourcePath);
        }

        try {
            return new FXMLLoader(url).load();
        } catch (
                IOException e) {
            throw new RuntimeException("Error al cargar FXML: " + absoluteResourcePath, e);
        }

    }

}
