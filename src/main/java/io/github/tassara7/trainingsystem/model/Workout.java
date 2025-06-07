package io.github.tassara7.trainingsystem.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Workout implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private LocalTime time;
    private List<BodyParts> bodyParts;

    public Workout(List<BodyParts> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public Workout(List<BodyParts> bodyParts, LocalDate date) {
        this.bodyParts = bodyParts;
        this.date = date;
    }


    public Workout(LocalDate date, LocalTime time, List<BodyParts> bodyParts) {
        this.date = date;
        this.time = time;
        this.bodyParts = bodyParts;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<BodyParts> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<BodyParts> bodyParts) {
        this.bodyParts = bodyParts;
    }
}
