package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;
import ru.ivanov.ggnetwork.services.UserPostService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/posts")
public class UserPostsController {
    @Autowired
    private UserPostService userPostService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@PathVariable Integer userId,
                                             @ModelAttribute @UseValidator PostCreateDto postCreateDto) {
        var post = userPostService.createPost(userId, postCreateDto);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Integer postId,
                                              @ModelAttribute @UseValidator PostUpdateDto postUpdateDto) {
        var post = userPostService.updatePost(postId, postUpdateDto);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
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
