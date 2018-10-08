package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> timeEntryList;

    public InMemoryTimeEntryRepository() {
        timeEntryList = new ArrayList<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() == 0) {
            timeEntry.setId(nextId());
        }
        timeEntryList.add(timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntryList.stream().filter(te -> te.getId() == id).findFirst().orElse(null);
    }

    public List<TimeEntry> list() {
        return timeEntryList;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry persisted = find(id);
        if (persisted == null) {
            return null;
        }
        timeEntryList.remove(persisted);
        timeEntry.setId(id);
        return create(timeEntry);
    }

    public void delete(long id) {
        TimeEntry persisted = find(id);
        if (persisted == null) {
            return;
        }
        timeEntryList.remove(persisted);
    }

    private Long nextId() {
        return Long.valueOf(this.timeEntryList.size() + 1);
    }


}
