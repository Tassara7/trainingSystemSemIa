package io.github.tassara7.trainingsystem.service;

import io.github.tassara7.trainingsystem.model.Workout;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutFilter {

    public static Workout filterByDate(List<Workout> workouts, LocalDate date) {
        return workouts.stream()
                .filter(workout -> date.equals(workout.getDate()))
                .findFirst()
                .orElse(null);
    }


    public static List<Workout> filterByMonthAndYear(List<Workout> workouts, int month, int year) {
        return workouts.stream()
                .filter(workout -> {
                    LocalDate date = workout.getDate();
                    return date.getYear() == year && date.getMonthValue() == month;
                })
                .collect(Collectors.toList());

    }

    public static List<Workout> filterByYear(List<Workout> workouts, int year) {
        return workouts.stream()
                .filter(workout -> {
                    LocalDate date = workout.getDate();
                    return date.getYear() == year;
                })
                .collect(Collectors.toList());

    }

    public static List<Workout> filterByMonth(List<Workout> workouts, int month) {
        return workouts.stream()
                .filter(workout -> {
                    LocalDate date = workout.getDate();
                    return date.getMonthValue() == month;
                })
                .collect(Collectors.toList());

    }


}
