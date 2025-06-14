// Em: src/main/java/io/github/tassara7/trainingsystem/view/MonthCardFactory.java
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MonthCardFactory {

    private final VBox monthContainer;

    public MonthCardFactory(int year, int month, List<Workout> monthWorkouts, List<Workout> yearWorkouts,
                            PieChart mainPieChart,  Consumer<List<Workout>> statsUpdater) {

        CalendarView calendarView = createCalendarView(year, month, monthWorkouts);
        Label monthLabel = createMonthLabel(year, month);

        this.monthContainer = new VBox(5, monthLabel, calendarView);
        this.monthContainer.setAlignment(Pos.CENTER);

        // A lógica de interação foi refatorada aqui
        setupInteractions(monthWorkouts, yearWorkouts, mainPieChart, statsUpdater, year, month);
    }

    public VBox getMonthCard() {
        return monthContainer;
    }

    private CalendarView createCalendarView(int year, int month, List<Workout> monthWorkouts) {
        Set<LocalDate> workoutDays = monthWorkouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());
        // A classe CalendarView já foi corrigida para o alinhamento dos dias.
        return new CalendarView(year, month, workoutDays, 10, 3, 3);
    }

    private Label createMonthLabel(int year, int month) {
        // Usar Locale para garantir que o nome do mês seja em português.

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", I18nManager.getLocale());
        String monthName = LocalDate.of(year, month, 1).format(monthFormatter);
        // Deixa a primeira letra maiúscula.
        Label label = new Label(monthName.substring(0, 1).toUpperCase() + monthName.substring(1));
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");
        return label;
    }

    /**
     * MÉTODO ATUALIZADO: A lógica agora é mais simples e confia no PieChartView
     * para gerenciar a visibilidade e os dados do gráfico.
     */
    private void setupInteractions(List<Workout> monthWorkouts, List<Workout> yearWorkouts,
                                   PieChart mainPieChart, Consumer<List<Workout>> statsUpdater, int year, int month) {

        HoverZoomEffect hoverZoom = new HoverZoomEffect(this.monthContainer, 1.05);

        this.monthContainer.setOnMouseClicked(event -> {
            try {
                LocalDate dateOfMonth = LocalDate.of(year, month, 1);
                ScreenManager.getInstance().setScreen(ScreenStorage.MONTHWORKOUTSCREEN, dateOfMonth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Evento para QUANDO O MOUSE ENTRA no cartão do mês
        this.monthContainer.setOnMouseEntered(event -> {
            hoverZoom.playZoomIn();

            // Simplesmente pede para o PieChartView se atualizar com os dados do mês.
            // Ele saberá o que fazer se a lista de treinos do mês for vazia.
            PieChartView.updatePieChart(mainPieChart, monthWorkouts);

            // Atualiza as estatísticas também.
            statsUpdater.accept(monthWorkouts);
        });

        // Evento para QUANDO O MOUSE SAI do cartão do mês
        this.monthContainer.setOnMouseExited(event -> {
            hoverZoom.playZoomOut();

            // Restaura a visualização do ano inteiro, pedindo ao PieChartView para se atualizar.
            PieChartView.updatePieChart(mainPieChart, yearWorkouts);

            // Restaura as estatísticas do ano.
            statsUpdater.accept(yearWorkouts);
        });
    }
}