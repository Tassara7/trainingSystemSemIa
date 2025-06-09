package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
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

    @FXML
    private GridPane yearGridPane;
    @FXML
    private Label yearLabel;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart barChart;
    @FXML
    private Pane rootPane;

    private LocalDate actualDate = LocalDate.now();


    public void initialize() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        yearLabel.setText(String.valueOf(actualDate.getYear()));

        int year = actualDate.getYear();
        List<Workout> yearWorkouts = WorkoutFilter.filterByYear(AppData.getInstance().getCurrentUser().getWorkouts(), year);

        // Define o estado inicial do PieChart com os dados do ano inteiro
        PieChartView.updatePieChart(pieChart, yearWorkouts);

        int row = 0;
        int col = 0;

        for (int month = 1; month <= 12; month++) {
            List<Workout> monthWorkouts = WorkoutFilter.filterByMonth(yearWorkouts, month);

            // 1. Usa a fábrica para criar o card de mês completo e interativo
            MonthCardFactory cardFactory = new MonthCardFactory(year, month, monthWorkouts, yearWorkouts, this.pieChart);

            // 2. Pega o componente VBox pronto da fábrica
            VBox monthContainer = cardFactory.getMonthCard();

            // 3. Adiciona o componente pronto ao grid
            yearGridPane.add(monthContainer, col, row);

            col++;
            if (col == 4) {
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