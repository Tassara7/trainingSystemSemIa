package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.Workout;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthCardFactory {

    private final VBox monthContainer;

    /**
     * O construtor agora também aceita o BarChart principal da tela.
     */
    public MonthCardFactory(int year, int month, List<Workout> monthWorkouts, List<Workout> yearWorkouts,
                            PieChart mainPieChart, BarChart<String, Number> mainBarChart) {

        CalendarView calendarView = createCalendarView(year, month, monthWorkouts);
        Label monthLabel = createMonthLabel(year, month);

        this.monthContainer = new VBox(5, monthLabel, calendarView);
        this.monthContainer.setAlignment(Pos.CENTER);

        // Configura as interações, agora passando o BarChart também
        setupInteractions(monthWorkouts, yearWorkouts, mainPieChart, mainBarChart, year, month);
    }

    public VBox getMonthCard() {
        return monthContainer;
    }

    // ... (métodos createCalendarView e createMonthLabel continuam iguais)
    private CalendarView createCalendarView(int year, int month, List<Workout> monthWorkouts) {
        Set<LocalDate> workoutDays = monthWorkouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());
        return new CalendarView(year, month, workoutDays, 10, 3, 3);
    }

    private Label createMonthLabel(int year, int month) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));
        String monthName = LocalDate.of(year, month, 1).format(monthFormatter);
        Label label = new Label(monthName.substring(0, 1).toUpperCase() + monthName.substring(1));
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        return label;
    }


    /**
     * O método de interações agora também atualiza o BarChart.
     */
    private void setupInteractions(List<Workout> monthWorkouts, List<Workout> yearWorkouts,
                                   PieChart mainPieChart, BarChart<String, Number> mainBarChart, int year, int month) {

        HoverZoomEffect hoverZoom = new HoverZoomEffect(this.monthContainer, 1.05);

        this.monthContainer.setOnMouseClicked(event -> {
            try {
                LocalDate dateOfMonth = LocalDate.of(year, month, 1);
                ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, dateOfMonth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Evento para mostrar dados do mês no PieChart E no BarChart
        this.monthContainer.setOnMouseEntered(event -> {
            hoverZoom.playZoomIn();
            PieChartView.updatePieChart(mainPieChart, monthWorkouts);
            BarChartManager.updateChartWithWorkouts(mainBarChart, monthWorkouts); // Atualiza o BarChart
        });

        // Evento para reverter para os dados do ano no PieChart E no BarChart
        this.monthContainer.setOnMouseExited(event -> {
            hoverZoom.playZoomOut();
            PieChartView.updatePieChart(mainPieChart, yearWorkouts);
            BarChartManager.updateChartWithWorkouts(mainBarChart, yearWorkouts); // Reverte o BarChart
        });
    }
}