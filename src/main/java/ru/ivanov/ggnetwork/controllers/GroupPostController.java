package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.EntityId;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.authorization.GroupAuthorizer;
import ru.ivanov.ggnetwork.authorization.GroupPostAuthorizer;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;
import ru.ivanov.ggnetwork.entities.GroupPost;
import ru.ivanov.ggnetwork.services.GroupPostService;

@RestController
@RequestMapping("/api/group-posts")
public class GroupPostController {
    @Autowired
    private GroupPostService groupPostService;

    @AuthorizedBy(GroupAuthorizer.class)
    @PostMapping("/{groupId}")
    public ResponseEntity<PostDto> createPost(@ModelAttribute @UseValidator PostCreateDto postCreateDto,
                                             @PathVariable @EntityId Integer groupId) {

        var post = groupPostService.createPost(groupId, postCreateDto);
        return ResponseEntity.ok(post);
    }

    @AuthorizedBy(GroupPostAuthorizer.class)
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@ModelAttribute @UseValidator PostUpdateDto postUpdateDto,
                                              @PathVariable @EntityId Integer postId) {
        var post = groupPostService.updatePost(postId, postUpdateDto);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> findPosts(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size,
                                       @PathVariable Integer groupId) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null) {
            return ResponseEntity.ok(groupPostService.getPostsByGroup(groupId, page, size));
        } else {
            return ResponseEntity.ok(groupPostService.getPostsByGroup(groupId));
        }
    }

}
