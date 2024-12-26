package corp.narayan.journalApp.service;

import corp.narayan.journalApp.entity.JournalEntry;
import corp.narayan.journalApp.entity.Users;
import corp.narayan.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    JournalEntryRepository journalEntryRepository;
    @Autowired
    UserService userService;

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry journal = journalEntryRepository.save(journalEntry);

        Users user = userService.findByUserName(username).get();
        user.getJournalEntries().add(journal);
        userService.saveEntry(user);

    }

    public List<JournalEntry> getAllEntriesByUserName(String username) {
        return userService.findByUserName(username).get().getJournalEntries();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String username) {
        Users user = userService.findByUserName(username).get();
        user.getJournalEntries().removeIf(entry -> id.equals(entry.getId()));
        journalEntryRepository.deleteById(id);
        userService.saveEntry(user);
    }
}
