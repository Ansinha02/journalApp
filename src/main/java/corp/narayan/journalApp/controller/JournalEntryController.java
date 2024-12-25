package corp.narayan.journalApp.controller;

import corp.narayan.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public void createEntry(@RequestBody JournalEntry journalEntry) {
        journalEntries.put(journalEntry.getId(), journalEntry);
    }

    @GetMapping("/id/{id}")
    public JournalEntry findJournalEntryById(@PathVariable Long id) {
        return journalEntries.getOrDefault(id, null);
    }

    @DeleteMapping("/id/{id}")
    public JournalEntry removeJournalEntryById(@PathVariable Long id) {
        return journalEntries.remove(id);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable Long id, @RequestBody JournalEntry journalEntry) {
        return journalEntries.put(id, journalEntry);
    }
}
