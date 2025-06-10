package io.github.tassara7.trainingsystem.service;

import io.github.tassara7.trainingsystem.model.BodyParts;
import io.github.tassara7.trainingsystem.model.Workout;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorkoutStatsCalculator {

    private final Map<Month, Long> countsByMonth;
    private final Map<DayOfWeek, Long> countsByDayOfWeek;
    private final Map<BodyParts, Long> countsByBodyPart;

    public WorkoutStatsCalculator(List<Workout> workouts) {
        // Calcula as estatísticas uma vez no construtor
        this.countsByMonth = workouts.stream()
                .collect(Collectors.groupingBy(w -> w.getDate().getMonth(), Collectors.counting()));

        this.countsByDayOfWeek = workouts.stream()
                .collect(Collectors.groupingBy(w -> w.getDate().getDayOfWeek(), Collectors.counting()));

        this.countsByBodyPart = workouts.stream()
                .flatMap(w -> w.getBodyParts().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    // Métodos para obter as estatísticas individuais
    public Optional<Map.Entry<Month, Long>> getMonthWithMostWorkouts() {
        return countsByMonth.entrySet().stream().max(Map.Entry.comparingByValue());
    }

    public Optional<Map.Entry<Month, Long>> getMonthWithFewestWorkouts() {
        return countsByMonth.entrySet().stream().min(Map.Entry.comparingByValue());
    }

    public Optional<Map.Entry<DayOfWeek, Long>> getMostTrainedDayOfWeek() {
        return countsByDayOfWeek.entrySet().stream().max(Map.Entry.comparingByValue());
    }

    public Optional<Map.Entry<BodyParts, Long>> getMostTrainedPart() {
        return countsByBodyPart.entrySet().stream().max(Map.Entry.comparingByValue());
    }

    public Optional<Map.Entry<BodyParts, Long>> getLeastTrainedPart() {
        return countsByBodyPart.entrySet().stream().min(Map.Entry.comparingByValue());
    }

    // Retorna o mapa completo para o BarChart
    public Map<BodyParts, Long> getCountsByBodyPart() {
        return countsByBodyPart;
    }
}