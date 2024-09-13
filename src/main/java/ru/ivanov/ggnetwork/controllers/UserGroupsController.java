package ru.ivanov.ggnetwork.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.services.GroupService;
import ru.ivanov.ggnetwork.services.UsersGroupsService;

@RestController
@RequestMapping("/api/users/{userId}/groups")
public class UserGroupsController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersGroupsService usersGroupsService;

    @PostMapping
    public ResponseEntity<?> createGroup(@ModelAttribute @Valid GroupCreateDto groupCreateDto,
                                         @PathVariable Integer userId) {
        var group = groupService.createGroup(groupCreateDto, userId);
        usersGroupsService.subscribe(userId, group.getId());
        return ResponseEntity.ok(group);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam Integer groupId,
                                       @PathVariable Integer userId) {
        usersGroupsService.subscribe(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam Integer groupId,
                                         @PathVariable Integer userId) {
        usersGroupsService.unsubscribe(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getGroups(@PathVariable Integer userId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(usersGroupsService.findGroupsByUser(userId, page, size));
        else
            return ResponseEntity.ok(usersGroupsService.findGroupsByUser(userId));
    }
    @GetMapping("/owned")
    public ResponseEntity<?> getGroupsByOwner(@PathVariable Integer userId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(usersGroupsService.findGroupsByOwner(userId, page, size));
        else
            return ResponseEntity.ok(usersGroupsService.findGroupsByOwner(userId));
    }


}
