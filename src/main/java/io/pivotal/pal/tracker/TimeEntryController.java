package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("time-entries")
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);
        return (timeEntry != null) ? ResponseEntity.ok(timeEntry) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") long id, @RequestBody TimeEntry timeEntry) {
        TimeEntry result = timeEntryRepository.update(id, timeEntry);
        return (result != null) ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id")  long id) {
        timeEntryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
