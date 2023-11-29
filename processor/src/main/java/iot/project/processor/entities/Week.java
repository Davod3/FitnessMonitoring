package iot.project.processor.entities;

import java.time.LocalDate;

public class Week {

    private LocalDate start;
    private LocalDate end;

    public Week(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
