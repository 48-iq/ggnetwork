package ru.ivanov.ggnetwork.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.services.GroupService;
import ru.ivanov.ggnetwork.services.UsersGroupsService;

@RestController
@RequestMapping("/api/users/{username}/groups")
public class UserGroupsController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersGroupsService usersGroupsService;

    @PostMapping
    public ResponseEntity<?> createGroup(@ModelAttribute @Valid GroupCreateDto groupCreateDto,
                                         @PathVariable String username) {
        var group = groupService.createGroup(groupCreateDto, username);
        usersGroupsService.subscribe(username, group.getTitle());
        return ResponseEntity.ok(group);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam String title,
                                       @PathVariable String username) {
        usersGroupsService.subscribe(username, title);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String title,
                                         @PathVariable String username) {
        usersGroupsService.unsubscribe(username, title);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getGroups(@PathVariable String username,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(usersGroupsService.findGroupsByUser(username, page, size));
        else
            return ResponseEntity.ok(usersGroupsService.findGroupsByUser(username));
    }
    @GetMapping("/owned")
    public ResponseEntity<?> getGroupsByOwner(@PathVariable String username,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(usersGroupsService.findGroupsByOwner(username, page, size));
        else
            return ResponseEntity.ok(usersGroupsService.findGroupsByOwner(username));
    }


}
