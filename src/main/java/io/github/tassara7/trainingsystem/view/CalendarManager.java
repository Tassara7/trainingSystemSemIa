package io.github.tassara7.trainingsystem.view; // Sugestão de pacote

import io.github.tassara7.trainingsystem.model.Workout;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarManager {

    private final GridPane calendarContainer;
    private final DaySelectionListener listener;
    private final CalendarAnimation animator;

    public CalendarManager(GridPane calendarContainer, DaySelectionListener listener, CalendarAnimation animator) {
        this.calendarContainer = calendarContainer;
        this.listener = listener;
        this.animator = animator;
    }

    /**
     * Exibe o calendário para um mês específico, sem animação.
     */
    public void displayMonth(LocalDate date, List<Workout> monthWorkouts) {
        CalendarView calendarView = createCalendarView(date, monthWorkouts);
        calendarContainer.getChildren().clear();
        calendarContainer.add(calendarView, 0, 0);
    }

    /**
     * Faz a transição animada para um novo mês.
     */
    public void animateToMonth(LocalDate date, List<Workout> monthWorkouts) {
        CalendarView calendarView = createCalendarView(date, monthWorkouts);
        animator.animateCalendarTransition(calendarView);
    }

    /**
     * Método privado que cria e configura uma nova instância de CalendarView.
     */
    private CalendarView createCalendarView(LocalDate date, List<Workout> monthWorkouts) {
        Set<LocalDate> workoutDays = monthWorkouts.stream()
                .map(Workout::getDate)
                .collect(Collectors.toSet());

        CalendarView calendarView = new CalendarView(date.getYear(), date.getMonthValue(), workoutDays, 30, 20, 10);
        calendarView.setDaySelectionListener(this.listener); // Usa o listener que foi passado (o controller)
        return calendarView;
    }
}