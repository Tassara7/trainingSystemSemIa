package io.github.tassara7.trainingsystem.view;

public record ScreenStorage(String fxmlPath, String titleKey, int width, int height) {

    public static final ScreenStorage FIRSTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/FirstScreen.fxml",
            "app.title", 1920, 1080);

    public static final ScreenStorage NEWWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/NewWorkoutScreen.fxml"
            , "app.title", 1632, 918);

    public static final ScreenStorage MONTHWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/MonthWorkoutScreen.fxml"
            , "app.title", 1632, 918);

    public static final ScreenStorage YEARWORKOUTSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/YearWorkoutScreen.fxml"
            , "app.title", 1632, 918);

    public static final ScreenStorage CONFIGSCREEN = new ScreenStorage("/io/github/tassara7/trainingsystem/fxml/ConfigScreen.fxml"
            ,"settings.title", 250, 400);
}
