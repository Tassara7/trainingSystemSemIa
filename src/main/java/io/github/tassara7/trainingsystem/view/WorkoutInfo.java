package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public class WorkoutInfo extends VBox {
    private Workout selectedWorkout;

    public WorkoutInfo(Workout workout) {
        selectedWorkout = workout;
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("info-pane");
    }

    public void showInfo(){

        Image image = new Image(getClass().getResourceAsStream("/io/github/tassara7/trainingsystem/images/InfoPane.png"));


        this.getChildren().clear();


        List<BodyParts> partsTrained = selectedWorkout.getBodyParts();

        if(partsTrained.isEmpty()){
            return;
        }

        for(BodyParts part: partsTrained){
            Label label = new Label(part.toString());
            label.setStyle("-fx-font-family: 'Blockhead';" +
                    "-fx-font-size: 29px;");
            label.setVisible(true);
            this.getChildren().add(label);
        }
    }

    public static WorkoutInfo createWorkoutInfo(Workout workout) {
        return new WorkoutInfo(workout);
    }
}
