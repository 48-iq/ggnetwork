package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.authorization.GroupAuthorizer;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.dto.group.GroupDto;
import ru.ivanov.ggnetwork.dto.group.GroupUpdateDto;
import ru.ivanov.ggnetwork.services.GroupService;
import ru.ivanov.ggnetwork.services.UsersGroupsService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UsersGroupsService usersGroupsService;

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/{userId}")
    public ResponseEntity<?> createGroup(@ModelAttribute @UseValidator GroupCreateDto groupCreateDto,
                                         @PathVariable @ResourceId Integer userId) {
        var group = groupService.createGroup(groupCreateDto, userId);
        usersGroupsService.subscribe(userId, group.getId());
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> findGroupByTitle(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupService.findGroupById(groupId));
    }

    @GetMapping
    public ResponseEntity<?> findAllGroups(@RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String query) {

        if ((page == null && size != null) || (page != null && size == null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");

        if (page == null) {
            if (query != null)
                return ResponseEntity.ok(groupService.findGroupsByQuery(query));
            else
                return ResponseEntity.ok(groupService.findAllGroups());
        }
        else {
            if (query != null)
                return ResponseEntity.ok(groupService.findGroupsByQuery(query, page, size));
            else
                return ResponseEntity.ok(groupService.findAllGroups(page,size));
        }
    }

    @AuthorizedBy(GroupAuthorizer.class)
    @PutMapping("/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable @ResourceId Integer groupId,
                                         @ModelAttribute @UseValidator GroupUpdateDto groupUpdateDto) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, groupUpdateDto));
    }

    @AuthorizedBy(GroupAuthorizer.class)
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable @ResourceId Integer groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }


}
