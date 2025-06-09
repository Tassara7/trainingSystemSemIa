package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.Workout;
import javafx.geometry.Pos;
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

/**
 * Uma fábrica para criar o componente visual completo para um único mês na tela do ano.
 */
public class MonthCardFactory {

    private final VBox monthContainer;

    public MonthCardFactory(int year, int month, List<Workout> monthWorkouts, List<Workout> yearWorkouts, PieChart mainPieChart) {

        // 1. Cria os componentes visuais
        CalendarView calendarView = createCalendarView(year, month, monthWorkouts);
        Label monthLabel = createMonthLabel(year, month);

        // 2. Agrupa os componentes em um VBox
        this.monthContainer = new VBox(5, monthLabel, calendarView);
        this.monthContainer.setAlignment(Pos.CENTER);

        // 3. Configura todas as interações no VBox
        setupInteractions(monthWorkouts, yearWorkouts, mainPieChart, year, month);
    }

    /**
     * Retorna o componente VBox final e pronto para ser adicionado à tela.
     */
    public VBox getMonthCard() {
        return monthContainer;
    }

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

    private void setupInteractions(List<Workout> monthWorkouts, List<Workout> yearWorkouts, PieChart mainPieChart, int year, int month) {
        HoverZoomEffect hoverZoom = new HoverZoomEffect(this.monthContainer, 1.05);

        // Evento de clique para navegar
        this.monthContainer.setOnMouseClicked(event -> {
            try {
                LocalDate dateOfMonth = LocalDate.of(year, month, 1);
                ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, dateOfMonth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Evento para mostrar dados do mês
        this.monthContainer.setOnMouseEntered(event -> {
            hoverZoom.playZoomIn();
            PieChartView.updatePieChart(mainPieChart, monthWorkouts);
        });

        // Evento para reverter para os dados do ano
        this.monthContainer.setOnMouseExited(event -> {
            hoverZoom.playZoomOut();
            PieChartView.updatePieChart(mainPieChart, yearWorkouts);
        });
    }
}