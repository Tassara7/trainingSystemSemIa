package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.User;

public class DayWorkoutController implements UserAware {

    private User currentUser;

    @Override
    public void setUser(User user) {
    this.currentUser = user;
    }
}
