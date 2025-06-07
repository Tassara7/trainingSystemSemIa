package io.github.tassara7.trainingsystem.view;

public record ScreenStorage(String fxmlPath, String title, int width, int height) {

    public static final ScreenStorage FIRSTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/FirstScreen.fxml",
            "Workout Recorder", 1920, 1080);

    public static final ScreenStorage NEWWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/NewWorkoutScreen.fxml"
            , "Workout Recorder", 1632, 918);

    public static final ScreenStorage MONTHWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/MonthWorkoutScreen.fxml"
            , "Workout Recorder", 1632, 918);

    public static final ScreenStorage YEARWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/YearWorkoutScreen.fxml"
            , "Workout Recorder", 1632, 918);

    public static final ScreenStorage CONFIGSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/ConfigScreen.fxml"
            ,"Settings", 250, 400);
}
