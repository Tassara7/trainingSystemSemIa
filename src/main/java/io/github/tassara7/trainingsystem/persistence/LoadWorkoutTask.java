package io.github.tassara7.trainingsystem.persistence;

import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.concurrent.Task;

import java.util.List;

public class LoadWorkoutTask extends Task<User> {
    @Override
    protected User call() throws Exception {
        return DataManager.load();
    }
}

