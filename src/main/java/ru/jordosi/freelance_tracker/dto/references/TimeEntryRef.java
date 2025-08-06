package ru.jordosi.freelance_tracker.dto.references;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.jordosi.freelance_tracker.model.TimeEntry;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeEntryRef {
    private Long id;
    private Integer timeSpent;
    private LocalDateTime entryDate;

    public TimeEntryRef(TimeEntry timeEntry) {
        this.id = timeEntry.getId();
        this.timeSpent = timeEntry.getTimeSpent();
        this.entryDate = timeEntry.getEntryDate();
    }
}
