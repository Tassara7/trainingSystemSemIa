package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BarChartManager {

    /**
     * Calcula as contagens a partir de uma lista de treinos e atualiza o gráfico de barras.
     * @param barChart O gráfico de barras a ser atualizado.
     * @param workouts A lista de treinos para analisar.
     */
    public static void updateChartWithWorkouts(BarChart<String, Number> barChart, List<Workout> workouts) {
        // 1. Calcula a contagem de cada parte do corpo a partir da lista
        Map<BodyParts, Long> counts = workouts.stream()
                .flatMap(w -> w.getBodyParts().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 2. Limpa os dados antigos do gráfico
        barChart.getData().clear();

        // Se não houver dados, não faz mais nada
        if (counts.isEmpty()) {
            return;
        }

        // 3. Adiciona os novos dados calculados
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Ordena do maior para o menor
                .forEach(entry -> {
                    String partName = entry.getKey().toString();
                    Number count = entry.getValue();
                    series.getData().add(new XYChart.Data<>(partName, count));
                });

        barChart.getData().add(series);
        barChart.setLegendVisible(false);
    }
}