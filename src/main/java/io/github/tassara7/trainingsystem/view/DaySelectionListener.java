package io.github.tassara7.trainingsystem.view;

import java.io.IOException;
import java.time.LocalDate;

public interface DaySelectionListener {
    void onDaySelected(LocalDate date);
    void onDayDeselected(LocalDate date);
    void onDayClicked(LocalDate date) throws IOException;
}
