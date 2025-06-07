package io.github.tassara7.trainingsystem.persistence;

import io.github.tassara7.trainingsystem.model.User;

public class AppData {
    private static AppData instance;
    private User currentUser;

    private AppData() {}

    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
