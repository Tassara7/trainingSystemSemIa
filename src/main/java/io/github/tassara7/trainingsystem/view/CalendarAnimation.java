package io.github.tassara7.trainingsystem.view;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class CalendarAnimation {

    private final GridPane calendarContainer;
    private ParallelTransition currentTransition;

    public CalendarAnimation(GridPane calendarContainer) {
        this.calendarContainer = calendarContainer;
    }

    public void animateCalendarTransition(Node newCalendarView) {
        if (currentTransition != null) {
            currentTransition.stop();
            // Garante que apenas o calendário visível permaneça
            if (calendarContainer.getChildren().size() > 1) {
                calendarContainer.getChildren().remove(0);
            }
            currentTransition = null;
        }

        double width = calendarContainer.getWidth() > 0 ? calendarContainer.getWidth() : 600; // Fallback
        newCalendarView.setTranslateX(width);
        calendarContainer.add(newCalendarView, 0, 0);

        if (calendarContainer.getChildren().size() > 1) {
            Node oldCalendarView = calendarContainer.getChildren().get(0);

            TranslateTransition ttOld = createSlideTransition(oldCalendarView, -width);
            setupOpacityBinding(oldCalendarView, width);

            TranslateTransition ttNew = createSlideTransition(newCalendarView, 0);

            currentTransition = new ParallelTransition(ttOld, ttNew);
            currentTransition.setOnFinished(e -> {
                calendarContainer.getChildren().remove(oldCalendarView);
                currentTransition = null;
            });
            currentTransition.play();
        } else {
            // Apenas anima a entrada do novo calendário se não houver um antigo
            TranslateTransition ttNew = createSlideTransition(newCalendarView, 0);
            ttNew.setDuration(Duration.millis(500));
            ttNew.play();
        }
    }

    private TranslateTransition createSlideTransition(Node node, double toX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(250), node);
        transition.setToX(toX);
        return transition;
    }

    private void setupOpacityBinding(Node node, double width) {
        double disappearThreshold = -width * 0.3;
        double finalWidth = width;

        node.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
            double x = node.getTranslateX();
            if (x >= disappearThreshold) {
                return 1.0;
            } else if (x <= -finalWidth * 0.7) {
                return 0.0;
            } else {
                return (x + finalWidth * 0.7) / (disappearThreshold + finalWidth * 0.7);
            }
        }, node.translateXProperty()));
    }
}