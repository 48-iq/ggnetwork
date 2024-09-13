package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.services.UserPostService;

@RestController
@RequestMapping("/api/users/{userId}/posts")
public class UserPostsController {
    @Autowired
    private UserPostService userPostService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@PathVariable Integer userId,
                                             @ModelAttribute PostCreateDto postCreateDto) {
        var post = userPostService.createPost(userId, postCreateDto);
        return ResponseEntity.ok(post);
    }

}
