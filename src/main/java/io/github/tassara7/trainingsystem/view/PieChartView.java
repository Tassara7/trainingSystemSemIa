package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieChartView extends PieChart {

    // Na classe PieChartView
    public static void updatePieChart(PieChart pieChart, List<Workout> workouts) {
        // Agrupa as ocorrências de cada BodyParts
        Map<BodyParts, Long> frequency = workouts.stream()
                .flatMap(workout -> workout.getBodyParts().stream())
                .collect(Collectors.groupingBy(bp -> bp, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Calcula o total de ocorrências para calcular a porcentagem
        long total = frequency.values().stream().mapToLong(Long::longValue).sum();

        for (BodyParts bp : BodyParts.values()) {
            long count = frequency.getOrDefault(bp, 0L);
            if (count > 0) {
                double percentage = (double) count / total * 100;
                // Formata o rótulo para incluir a porcentagem
                String nameWithPercentage = bp.toString() + " (" + String.format("%.1f", percentage) + "%)";
                pieChartData.add(new PieChart.Data(nameWithPercentage, count));
            }
        }

        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(false); // Se desejar manter a legenda oculta


    }



}
