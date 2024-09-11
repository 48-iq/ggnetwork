package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.dto.group.GroupDto;
import ru.ivanov.ggnetwork.dto.group.GroupUpdateDto;
import ru.ivanov.ggnetwork.services.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/{title}")
    public ResponseEntity<GroupDto> findGroupByTitle(@PathVariable String title) {
        return ResponseEntity.ok(groupService.findGroupByTitle(title));
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

    @PutMapping("/{title}")
    public ResponseEntity<?> updateGroup(@PathVariable String title,
                                         @ModelAttribute GroupUpdateDto groupUpdateDto) {
        return ResponseEntity.ok(groupService.updateGroup(title, groupUpdateDto));
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<?> deleteGroup(@PathVariable String title) {
        groupService.deleteGroup(title);
        return ResponseEntity.ok().build();
    }


}
