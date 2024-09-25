package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.authorization.UserPostAuthorizer;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;
import ru.ivanov.ggnetwork.services.UserPostService;

@RestController
@RequestMapping("/api/user-posts")
public class UserPostsController {
    @Autowired
    private UserPostService userPostService;

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/{userId}")
    public ResponseEntity<PostDto> createPost(@PathVariable @ResourceId Integer userId,
                                              @ModelAttribute @UseValidator PostCreateDto postCreateDto) {
        var post = userPostService.createPost(userId, postCreateDto);
        return ResponseEntity.ok(post);
    }

    @AuthorizedBy(UserPostAuthorizer.class)
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable @ResourceId Integer postId,
                                              @ModelAttribute @UseValidator PostUpdateDto postUpdateDto) {
        var post = userPostService.updatePost(postId, postUpdateDto);
        return ResponseEntity.ok(post);
    }

    @AuthorizedBy(UserPostAuthorizer.class)
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable @ResourceId Integer postId) {
        userPostService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> findPostsByUser(@PathVariable Integer userId,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page == null)
            return ResponseEntity.ok(userPostService.findPostsByUser(userId));
        else
            return ResponseEntity.ok(userPostService.findPostsByUser(userId, page, size));
    }

}
