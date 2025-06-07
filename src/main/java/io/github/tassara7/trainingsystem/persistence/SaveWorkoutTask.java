package io.github.tassara7.trainingsystem.persistence;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.concurrent.Task;

import java.util.List;

public class SaveWorkoutTask extends Task<Void> {
    private User user;

    @Override
    protected Void call() throws Exception {
        DataManager.save(user);
        return null;
    }
}
