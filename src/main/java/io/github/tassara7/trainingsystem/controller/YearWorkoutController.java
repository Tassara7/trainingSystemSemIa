package io.github.tassara7.trainingsystem.controller;


import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.view.CalendarView;
import io.github.tassara7.trainingsystem.view.WallpaperUtil;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class YearWorkoutController implements ActualDate, SkinnableScreen {

    @FXML
    private GridPane yearGridPane;

    @FXML
    private Pane rootPane;

    private LocalDate actualDate = LocalDate.now();


    public void initialize(){

        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());

        int year = actualDate.getYear();
        int row = 0;
        int col = 0;

        List<Workout> yearWorkouts = WorkoutFilter.filterByYear(AppData.getInstance().getCurrentUser().getWorkouts(), year);


        for(int month = 1 ; month < 13 ; month++){
            List<Workout> monthWorkouts = WorkoutFilter.filterByMonth(yearWorkouts, month);

            Set<LocalDate> workoutDays = monthWorkouts.stream()
                    .map(Workout::getDate)
                    .collect(Collectors.toSet());

            CalendarView calendarView = new CalendarView(year, month, workoutDays, 10, 3, 3);
            yearGridPane.add(calendarView, col, row);

            col++;

            if(col == 4){
                col = 0;
                row++;
            }

        }
    }

    @Override
    public void setDate(LocalDate date) {
        actualDate = date;
    }

    @Override
    public ScreenType getScreenType() {
        return ScreenType.YEAR;
    }
}
