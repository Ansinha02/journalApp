package corp.narayan.journalApp.controller;

import corp.narayan.journalApp.entity.JournalEntry;
import corp.narayan.journalApp.service.JournalEntryService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JournalEntryController {

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping("{username}")
    public ResponseEntity<List<JournalEntry>> getAllEntriesByUserName(@PathVariable String username) {
        List<JournalEntry> journalEntries = journalEntryService.getAllEntriesByUserName(username);
        if(!isEmpty(journalEntries)) {
            return new ResponseEntity(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry,
                                                    @PathVariable String username) {
        try {
            journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.toString());
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

    @DeleteMapping("/id/{username}/{id}")
    public ResponseEntity<?> removeJournalEntryById(@PathVariable ObjectId id, @PathVariable String username) {
        try {
            journalEntryService.deleteById(id, username);
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
