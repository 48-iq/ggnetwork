package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.dto.user.UserUpdateDto;
import ru.ivanov.ggnetwork.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        var user =  userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{username}")
    @PreAuthorize("authentication.principal.username == #username")
    public ResponseEntity<UserDto> updateUser(@PathVariable String username,
                                              @RequestBody UserUpdateDto userUpdateDto) {

        var user = userService.updateUser(username, userUpdateDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("authentication.principal.username == #username")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> findUsersByQuery(@RequestParam(required = false) String query,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (query == null) {
            if (page == null)
                return ResponseEntity.ok(userService.findUsers());
            else
                return ResponseEntity.ok(userService.findUsers(page, size));
        }
        else {
            if (page == null)
                return ResponseEntity.ok(userService.findUsersByQuery(query));
            else
                return ResponseEntity.ok(userService.findUsersByQuery(query, page, size));
        }
    }
}
