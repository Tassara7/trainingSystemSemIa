package io.github.tassara7.trainingsystem.view;

import io.github.tassara7.trainingsystem.model.Workout;
import io.github.tassara7.trainingsystem.persistence.AppData;
import io.github.tassara7.trainingsystem.service.WorkoutFilter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

public class CalendarView extends GridPane {
    private int year;
    private int month;
    private Set<LocalDate> workoutDays;

    private DaySelectionListener daySelectionListener;

    public CalendarView(int year, int month, Set<LocalDate> workoutDays, int circleSize, int fontSize, int gap) {
        this.year = year;
        this.month = month;
        this.workoutDays = workoutDays;
        setHgap(gap);
        setVgap(gap);
        setPadding(new Insets(10));
        createCalendar(circleSize, fontSize);
    }

    public CalendarView() {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));
        //createCalendar();
    }

    public void setDaySelectionListener(DaySelectionListener listener) {
        this.daySelectionListener = listener;
    }

    public void createCalendar(int circleSize, int fontSize) {
        this.setAlignment(Pos.TOP_CENTER); // Garante que o conteúdo fique no topo
        this.setPrefHeight((circleSize * 2 + getVgap()) * 6 + 20); // Altura para até 6 semanas
        this.setMinHeight(this.getPrefHeight());

        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        int firstDayOfWeek = LocalDate.of(year, month, 1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        int col = 0;
        int row = 0;

        // Preenche os dias antes do primeiro do mês com espaços
        for (int i = 0; i < firstDayOfWeek - 1; i++) {
            StackPane spacer = new StackPane();
            spacer.setMinSize(circleSize * 2, circleSize * 2);
            spacer.setPrefSize(circleSize * 2, circleSize * 2);
            add(spacer, col, row);
            col++;
        }

        for (int day = 1; day <= totalDays; day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);

            StackPane dayPane = getDayCircle(currentDate, day, circleSize, fontSize);
            dayPane.getStyleClass().add("day-pane");


            dayPane.setOnMouseClicked(e -> {
                if (daySelectionListener != null) {
                    try {
                        daySelectionListener.onDayClicked(currentDate);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            dayPane.setOnMouseEntered(e -> {
                if (daySelectionListener != null) {
                    daySelectionListener.onDaySelected(currentDate);
                }
            });
            dayPane.setOnMouseExited(e -> {
                if (daySelectionListener != null) {
                    daySelectionListener.onDayDeselected(currentDate);
                }
            });

            add(dayPane, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    public void createCalendar(int year, int month, int circleSize, int fontSize) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        int firstDayOfWeek = LocalDate.of(year, month, 1).getDayOfWeek().getValue();
        int rowsNeeded = (int) Math.ceil((firstDayOfWeek - 1 + totalDays) / 7.0);
        int row = (rowsNeeded == 6) ? 7 : 0 ; // Arrumar alinhamento dos meses com semanas
        int col = firstDayOfWeek - 1;

        for (int day = 1; day <= totalDays; day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);

            StackPane dayPane = getDayCircle(currentDate, day, circleSize, fontSize);
            dayPane.getStyleClass().add("day-pane");

            dayPane.setOnMouseClicked(e ->{
                if (daySelectionListener != null) {
                    try {
                        daySelectionListener.onDayClicked(currentDate);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            });
            dayPane.setOnMouseEntered(e -> {
                if(daySelectionListener != null) {
                    daySelectionListener.onDaySelected(currentDate);
                }
            });
            dayPane.setOnMouseExited(e -> {
                if(daySelectionListener != null) {
                    daySelectionListener.onDayDeselected(currentDate);
                }

            })
            ;


            add(dayPane, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    private StackPane getDayCircle(LocalDate currentDate, int day, int circleSize, int fontSize) {
        Color dayColor = AppData.getInstance().getCurrentUser().getSkin().getDayColor();
        Color workoutDayColor = AppData.getInstance().getCurrentUser().getSkin().getWorkoutDayColor();
        //Cor do círculo
        Color fillColor = (workoutDays != null && workoutDays.contains(currentDate))
                ? workoutDayColor
                : dayColor;
        Circle circle = new Circle(circleSize, fillColor);

        //Cor da borda
        Color strokeColor = (workoutDays != null && workoutDays.contains(currentDate))
                ? workoutDayColor
                : dayColor;

        circle.setStroke(strokeColor);


        Label dayLabel = new Label(String.valueOf(day));
        dayLabel.getStyleClass().add("day-label");
        dayLabel.setStyle("-fx-font-weight: bold;");
        StackPane stackPane = new StackPane(circle, dayLabel);
        stackPane.setCursor(Cursor.HAND);

        return stackPane;
    }

    public void reloadCalendar() {
        this.getChildren().clear(); // remove todos os dias do calendário atual
        //createCalendar(); // desenha de novo com a nova skin
    }



}
