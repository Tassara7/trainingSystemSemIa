package io.github.tassara7.trainingsystem.model;

import io.github.tassara7.trainingsystem.view.Skins;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private String name;
    private List<Workout> workouts;
    private Skins skin;

    public User(String name) {
        this.name = name;
        this.workouts = new ArrayList<>();
    }

    // Getters e setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public Skins getSkin() {
        if (skin == null) {
            setSkin(Skins.DEFAULT);
        }
        return skin;
    }

    public void setSkin(Skins skin) {
        this.skin = skin;
    }




}
