package io.github.tassara7.trainingsystem.controller;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import io.github.tassara7.trainingsystem.service.WorkoutStatsCalculator;
import io.github.tassara7.trainingsystem.view.BarChartManager;
import io.github.tassara7.trainingsystem.view.MonthCardFactory;
import io.github.tassara7.trainingsystem.view.PieChartView;
import io.github.tassara7.trainingsystem.view.WallpaperUtil;
import io.github.tassara7.trainingsystem.view.skin.ScreenType;
import io.github.tassara7.trainingsystem.view.skin.SkinnableScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class YearWorkoutController implements ActualDate, SkinnableScreen {

    @FXML private Label nextYearLabel, previuosYearLabel;
    @FXML private GridPane yearGridPane;
    @FXML private GridPane statsGridPane;
    @FXML private Label yearLabel;
    @FXML private PieChart pieChart;
    @FXML private Pane rootPane;


    private LocalDate actualDate = LocalDate.now();


    public void initialize() {
        rootPane.setStyle(WallpaperUtil.buildWallpaperStyle());
        yearLabel.setText(String.valueOf(actualDate.getYear()));



        pieChart.setAnimated(false);





        int year = actualDate.getYear();
        List<Workout> yearWorkouts = WorkoutFilter.filterByYear(AppData.getInstance().getCurrentUser().getWorkouts(), year);

        updateStatsDisplay(yearWorkouts);

        boolean hasWorkouts = !yearWorkouts.isEmpty();


        pieChart.setVisible(hasWorkouts);

        // Apenas popule os gráficos se houver dados
        if (hasWorkouts) {
            pieChart.setAnimated(false);
            PieChartView.updatePieChart(pieChart, yearWorkouts);
        }


        // Define o estado inicial dos dois gráficos com os dados do ano inteiro
        PieChartView.updatePieChart(pieChart, yearWorkouts);


        // Popula o grid de meses
        int row = 0;
        int col = 0;
        for (int month = 1; month <= 12; month++) {
            List<Workout> monthWorkouts = WorkoutFilter.filterByMonth(yearWorkouts, month);

            // A fábrica agora recebe o BarChart para poder controlá-lo
            MonthCardFactory cardFactory = new MonthCardFactory(year, month,
                    monthWorkouts, yearWorkouts, this.pieChart, this::updateStatsDisplay);

            VBox monthContainer = cardFactory.getMonthCard();
            yearGridPane.add(monthContainer, col, row);

            col++;
            if (col == 4) { col = 0; row++; }
        }

    }

    private void updateStatsDisplay(List<Workout> workouts) {
        statsGridPane.getChildren().clear();

        if (workouts.isEmpty()) { return; }

        WorkoutStatsCalculator stats = new WorkoutStatsCalculator(workouts);
        Map<BodyParts, Long> counts = stats.getCountsByBodyPart();

        BodyParts[] partsInOrder = {
                BodyParts.CHEST, BodyParts.BACK, BodyParts.SHOULDERS, BodyParts.BICEPS,
                BodyParts.TRICEPS, BodyParts.QUADRICEPS, BodyParts.HAMSTRINGS, BodyParts.CALVES
        };

        for(int i = 0 ; i < partsInOrder.length ; i++) {
            BodyParts part = partsInOrder[i];

            int rowIndex = i % 4; // Linha de 0 a 3
            int colIndex = (i / 4) * 2; // Coluna 0 para os 4 primeiros, Coluna 2 para os 4 últimos

            String count = String.valueOf(counts.getOrDefault(part, 0L));
            String name = part.toString() + ":";

            Label nameLabel = new Label(name);
            Label countLabel = new Label(count);

            // Adiciona os Labels ao GridPane na posição calculada
            statsGridPane.add(nameLabel, colIndex, rowIndex);
            statsGridPane.add(countLabel, colIndex + 1, rowIndex);
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