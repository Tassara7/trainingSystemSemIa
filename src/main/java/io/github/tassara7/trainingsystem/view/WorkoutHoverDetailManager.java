package io.github.tassara7.trainingsystem.view; // Pode ser no pacote 'view' ou 'controller'

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class WorkoutHoverDetailManager {

    private final VBox infoPane;
    private final User currentUser;

    public WorkoutHoverDetailManager(VBox infoPane, User currentUser) {
        this.infoPane = infoPane;
        this.currentUser = currentUser;
        this.infoPane.setVisible(false); // Garante que comece invisível
    }

    /**
     * Chamado quando o mouse entra em um dia do calendário.
     */
    public void handleDaySelected(LocalDate date) {
        WorkoutFilter.filterByDate(currentUser.getWorkouts(), date)
                .ifPresent(this::showDetails);
    }

    /**
     * Chamado quando o mouse sai de um dia do calendário.
     */
    public void handleDayDeselected() {
        infoPane.setVisible(false);
    }

    private void showDetails(Workout workout) {
        WorkoutInfo workoutInfo = new WorkoutInfo(workout);
        workoutInfo.showInfo();

        infoPane.getChildren().clear();
        infoPane.getChildren().add(workoutInfo);
        infoPane.setVisible(true);
    }
}