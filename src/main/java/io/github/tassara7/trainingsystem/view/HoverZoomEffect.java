package io.github.tassara7.trainingsystem.view;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Prepara as animações de zoom para um Node, mas não controla os eventos.
 */
public class HoverZoomEffect {

    private final ScaleTransition zoomIn;
    private final ScaleTransition zoomOut;

    public HoverZoomEffect(Node node, double zoomFactor) {
        // Animação para aumentar o tamanho (zoom in)
        zoomIn = new ScaleTransition(Duration.millis(100), node);
        zoomIn.setToX(zoomFactor);
        zoomIn.setToY(zoomFactor);

        // Animação para voltar ao tamanho original (zoom out)
        zoomOut = new ScaleTransition(Duration.millis(100), node);
        zoomOut.setToX(1.0);
        zoomOut.setToY(1.0);
    }

    /**
     * Inicia a animação de aumentar (zoom in).
     */
    public void playZoomIn() {
        zoomIn.play();
    }

    /**
     * Inicia a animação de diminuir (zoom out).
     */
    public void playZoomOut() {
        zoomOut.play();
    }
}