package corp.narayan.journalApp.controller;

import corp.narayan.journalApp.entity.JournalEntry;
import corp.narayan.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        List<JournalEntry> journalEntries = journalEntryService.getAll();
        if(!isEmpty(journalEntries)) {
            return new ResponseEntity(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> findJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> removeJournalEntryById(@PathVariable ObjectId id) {
        try {
            journalEntryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry updatedJournalEntry) {
            JournalEntry journalEntry = journalEntryService.findById(id).orElse(null);
            if (journalEntry != null) {
                journalEntry.setTitle(hasText(updatedJournalEntry.getTitle()) ? updatedJournalEntry.getTitle() : journalEntry.getTitle());
                journalEntry.setContent(hasText(updatedJournalEntry.getContent()) ? updatedJournalEntry.getContent() : journalEntry.getContent());
                journalEntryService.saveEntry(journalEntry);
                return new ResponseEntity<>(journalEntry, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
