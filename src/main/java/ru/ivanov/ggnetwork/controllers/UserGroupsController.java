package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.services.GroupService;
import ru.ivanov.ggnetwork.services.UsersGroupsService;

@RestController
@RequestMapping("/api/users/{userId}")
public class UserGroupsController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersGroupsService usersGroupsService;


    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/subscribe-to-group/{groupId}")
    public ResponseEntity<?> subscribe(@PathVariable Integer groupId,
                                       @PathVariable @ResourceId Integer userId) {
        usersGroupsService.subscribe(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/unsubscribe-from-group/{groupId}")
    public ResponseEntity<?> unsubscribe(@PathVariable Integer groupId,
                                         @PathVariable @ResourceId Integer userId) {
        usersGroupsService.unsubscribe(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/groups")
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

    @GetMapping("/groups-owned")
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
