package corp.narayan.journalApp.controller;

import corp.narayan.journalApp.entity.Users;
import corp.narayan.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAll();
        if(!users.isEmpty())  {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody  Users user) {
        try {
            userService.saveEntry(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Users> updateUser(@RequestBody  Users user) {
        Optional<Users> userToBeUpdated = userService.findByUserName(user.getUserName());
        if(userToBeUpdated.isPresent()) {
            Users updatedUser = userToBeUpdated.get();
            updatedUser.setPassword(user.getPassword());
            updatedUser.setUserName(user.getUserName());
            userService.saveEntry(updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
