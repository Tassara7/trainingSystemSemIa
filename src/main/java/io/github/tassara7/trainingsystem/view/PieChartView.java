// Em: src/main/java/io/github/tassara7/trainingsystem/view/PieChartView.java

package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieChartView extends PieChart {

    /**
     * MÉTODO ATUALIZADO: Agora limpa os dados antigos antes de adicionar os novos
     * para garantir uma renderização limpa e sem bugs.
     */
    public static void updatePieChart(PieChart pieChart, List<Workout> workouts) {
        // 1. LINHA CRUCIAL: Limpa completamente todos os dados existentes no gráfico.
        pieChart.getData().clear();

        // Se a lista de treinos for vazia, simplesmente esconde o gráfico e termina.
        if (workouts == null || workouts.isEmpty()) {
            pieChart.setVisible(false);
            return;
        }

        // Agrupa as ocorrências de cada BodyParts
        Map<BodyParts, Long> frequency = workouts.stream()
                .flatMap(workout -> workout.getBodyParts().stream())
                .collect(Collectors.groupingBy(bp -> bp, Collectors.counting()));

        // Se não houver frequência (ex: treinos sem partes do corpo), esconde o gráfico.
        if (frequency.isEmpty()) {
            pieChart.setVisible(false);
            return;
        }

        // Se chegamos aqui, temos dados para mostrar, então garantimos que o gráfico está visível.
        pieChart.setVisible(true);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Calcula o total de ocorrências para a porcentagem
        long total = frequency.values().stream().mapToLong(Long::longValue).sum();

        for (BodyParts bp : BodyParts.values()) {
            long count = frequency.getOrDefault(bp, 0L);
            if (count > 0) {
                double percentage = (double) count / total * 100;
                String translatedName = I18nManager.get(bp); // Pega a tradução do enum
                String nameWithPercentage = translatedName + " (" + String.format("%.1f", percentage) + "%)";
                pieChartData.add(new PieChart.Data(nameWithPercentage, count));
            }
        }

        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(false);
    }
}