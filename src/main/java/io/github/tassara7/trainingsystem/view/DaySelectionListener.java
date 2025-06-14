package io.github.tassara7.trainingsystem.view;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;

public interface DaySelectionListener {
    void onDaySelected(LocalDate date, MouseEvent event);
    void onDayDeselected(LocalDate date);
    void onDayClicked(LocalDate date) throws IOException;
}
