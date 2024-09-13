package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.dto.user.UserInfoDto;
import ru.ivanov.ggnetwork.dto.user.UserUpdateDto;
import ru.ivanov.ggnetwork.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        var user =  userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable Integer userId) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        var user = userService.getUserInfoById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer userId,
                                              @RequestBody UserUpdateDto userUpdateDto) {

        var user = userService.updateUser(userId, userUpdateDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        System.out.println();
        userService.deleteUser(userId);
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
