package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.User;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.view.*;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinManager;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewWorkoutController implements UserAware, WorkoutCreator, ActualDate, SkinChangeListener, SkinnableScreen {

    private User currentUser;
    private LocalDate date;
    private Workout workout;
    private Boolean editWorkout = false;

    @FXML
    private Pane newWorkoutRootPane;
    @FXML
    private Label confirmWorkoutLabel;
    @FXML
    private Label restDayLabel;

    @FXML
    private ToggleButton chestButton;
    @FXML
    private ToggleButton backButton;
    @FXML
    private ToggleButton bicepsButton;
    @FXML
    private ToggleButton tricepsButton;
    @FXML
    private ToggleButton shouldersButton;
    @FXML
    private ToggleButton quadricepsButton;
    @FXML
    private ToggleButton hamstringsButton;
    @FXML
    private ToggleButton calvesButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button deleteButton;



    @Override
    public void setUser(User user) {
        currentUser = user;
    }

    public void initialize() {

        SkinManager.registerListener(this);
        newWorkoutRootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        LocalDate localDate = LocalDate.now();
        datePicker.setValue(localDate);
        datePicker.setVisible(false);




    }


    public void confirmWorkout() throws IOException {

        List<BodyParts> bodyParts = new ArrayList<>();
        LocalDate workoutDate = datePicker.getValue();



        if(date != null){
            workoutDate = date;
        }


        if (chestButton.isSelected())
            bodyParts.add(BodyParts.CHEST);
        if (backButton.isSelected())
            bodyParts.add(BodyParts.BACK);
        if (bicepsButton.isSelected())
            bodyParts.add(BodyParts.BICEPS);
        if (tricepsButton.isSelected())
            bodyParts.add(BodyParts.TRICEPS);
        if (shouldersButton.isSelected())
            bodyParts.add(BodyParts.SHOULDERS);
        if (quadricepsButton.isSelected())
            bodyParts.add(BodyParts.QUADRICEPS);
        if (hamstringsButton.isSelected())
            bodyParts.add(BodyParts.HAMSTRINGS);
        if (calvesButton.isSelected())
            bodyParts.add(BodyParts.CALVES);


        if (!bodyParts.isEmpty()) {
            if (editWorkout) {
                workout.getBodyParts().clear();
                workout.getBodyParts().addAll(bodyParts);
                workout.setDate(workoutDate);
                ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, workoutDate);
            } else {


                Workout newWorkout = new Workout(bodyParts, workoutDate);
                if (currentUser != null) {
                    currentUser.getWorkouts().add(newWorkout);
                    ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, workoutDate);
                }
            }
        }

    }

    @FXML
    public void deleteButton() throws IOException {
        if (editWorkout && currentUser != null) {
            currentUser.getWorkouts().remove(workout); // Remove o treino atual
            ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, workout.getDate());
        }
    }

    public void restDay() throws IOException {
        LocalDate localDate = LocalDate.now();
        ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, localDate);
    }

    @Override
    public void setWorkout(Workout workout) {
        this.workout = workout;

        deleteButton.setVisible(true);

        if (workout != null) {

            editWorkout = true;
            datePicker.setValue(workout.getDate());

            if (workout.getBodyParts() != null) {
                if (workout.getBodyParts().contains(BodyParts.CHEST)) {
                    chestButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.BACK)) {
                    backButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.BICEPS)) {
                    bicepsButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.TRICEPS)) {
                    tricepsButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.HAMSTRINGS)) {
                    hamstringsButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.CALVES)) {
                    calvesButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.QUADRICEPS)) {
                    quadricepsButton.setSelected(true);
                }
                if (workout.getBodyParts().contains(BodyParts.SHOULDERS)) {
                    shouldersButton.setSelected(true);
                }

            }


        }
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
        datePicker.setValue(date);
    }

    @Override
    public void onSkinChanged() {
        newWorkoutRootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.NEW_WORKOUT;
    }
}
