package com.project.common.util;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class NavigationService {

    private final StackPane contentHost;
    private final Map<String, Node> cache = new HashMap<>();

    public NavigationService(StackPane contentHost) {
        this.contentHost = contentHost;
    }

    public void navigate(String key, String fxmlPath) {
        Node view = cache.computeIfAbsent(key, k -> ViewLoader.load(fxmlPath));
        switchWithSlideUp(view);
    }

    /**
     * Smooth slide-up + fade-in transition for page navigation.
     * Gives a modern, premium feel to every page change.
     */
    private void switchWithSlideUp(Node newContent) {
        if (!contentHost.getChildren().isEmpty() && contentHost.getChildren().getFirst() == newContent) {
            return;
        }

        newContent.setOpacity(0);
        newContent.setTranslateY(16);
        contentHost.getChildren().setAll(newContent);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(300), newContent);
        slideUp.setFromY(16);
        slideUp.setToY(0);
        slideUp.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));

        ParallelTransition parallel = new ParallelTransition(fadeIn, slideUp);
        parallel.play();
    }

}
