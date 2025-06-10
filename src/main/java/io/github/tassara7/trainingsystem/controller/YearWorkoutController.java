package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.view.BarChartManager;
import io.github.tassara7.trainingsystem.view.MonthCardFactory;
import io.github.tassara7.trainingsystem.view.PieChartView;
import io.github.tassara7.trainingsystem.view.WallpaperUtil;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class YearWorkoutController implements ActualDate, SkinnableScreen {

    @FXML private GridPane yearGridPane;
    @FXML private Label yearLabel;
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart; // O BarChart principal
    @FXML private Pane rootPane;

    private LocalDate actualDate = LocalDate.now();


    public void initialize() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        yearLabel.setText(String.valueOf(actualDate.getYear()));

        pieChart.setAnimated(false);
        barChart.setAnimated(false);

        int year = actualDate.getYear();
        List<Workout> yearWorkouts = WorkoutFilter.filterByYear(AppData.getInstance().getCurrentUser().getWorkouts(), year);

        // Define o estado inicial dos dois gráficos com os dados do ano inteiro
        PieChartView.updatePieChart(pieChart, yearWorkouts);
        BarChartManager.updateChartWithWorkouts(barChart, yearWorkouts);

        // Popula o grid de meses
        int row = 0;
        int col = 0;
        for (int month = 1; month <= 12; month++) {
            List<Workout> monthWorkouts = WorkoutFilter.filterByMonth(yearWorkouts, month);

            // A fábrica agora recebe o BarChart para poder controlá-lo
            MonthCardFactory cardFactory = new MonthCardFactory(year, month, monthWorkouts, yearWorkouts, this.pieChart, this.barChart);

            VBox monthContainer = cardFactory.getMonthCard();
            yearGridPane.add(monthContainer, col, row);

            col++;
            if (col == 4) { col = 0; row++; }
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