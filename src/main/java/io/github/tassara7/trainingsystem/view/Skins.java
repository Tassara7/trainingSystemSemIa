package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import javafx.scene.paint.Color;

import java.io.Serializable;

public enum Skins implements Serializable {

    DEFAULT("/io/github/tassara7/trainingsystem/skins/default/Month-Default.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/default/NewWorkout-Default.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/backgrounds/beach.jpg",
            "#1292e1", "#12c0c5"),

    DARK("/io/github/tassara7/trainingsystem/skins/dark/Month-Dark.css",
            "/io/github/tassara7/trainingsystem/skins/dark/Year-Dark.css",
            "/io/github/tassara7/trainingsystem/skins/dark/NewWorkout-Dark.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/backgrounds/dark.jpg",
            "#242424", "#5d5d5d"),

    FOREST("/io/github/tassara7/trainingsystem/skins/forest/Month-Forest.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/forest/NewWorkout-Forest.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/backgrounds/forest.jpg",
            "#1f6f06", "#3ea81d"),

    GOJO("/io/github/tassara7/trainingsystem/skins/gojo/Month-Gojo.css",
                   "",
                   "/io/github/tassara7/trainingsystem/skins/gojo/NewWorkout-Gojo.css",
                   "",
                   "/io/github/tassara7/trainingsystem/skins/backgrounds/gojo.jpg",
                   "#7a0707", "#109ad6"),

    FRIEREN("/io/github/tassara7/trainingsystem/skins/frieren/Month-Frieren.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/frieren/NewWorkout-Frieren.css",
            "",
            "/io/github/tassara7/trainingsystem/skins/backgrounds/Frieren.jpg",
            "#b0becf", "#094ab4");


    private final String month;
    private final String year;
    private final String newWorkout;
    private final String config;
    private final String wallpaperPath;
    private final String dayColorHex;
    private final String workoutDayColorHex;

    Skins(String month, String year, String newWorkout, String config, String wallpaperPath, String dayColorHex, String workoutDayColorHex) {
        this.month = month;
        this.year = year;
        this.newWorkout = newWorkout;
        this.config = config;
        this.wallpaperPath = wallpaperPath;
        this.dayColorHex = dayColorHex;
        this.workoutDayColorHex = workoutDayColorHex;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getNewWorkout() {
        return newWorkout;
    }

    public String getConfig() {
        return config;
    }

    public String getWallpaperPath() {
        return wallpaperPath;
    }

    public String getCssFor(ScreenType type) {
        return switch (type){
            case MONTH -> month;
            case YEAR -> year;
            case NEW_WORKOUT -> newWorkout;
            case CONFIG -> config;
        };

    }


    // Converte a representação hex para Color quando necessário
    public Color getDayColor() {
        return Color.web(dayColorHex);
    }

    public Color getWorkoutDayColor() {
        return Color.web(workoutDayColorHex);
    }
}
