package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostDto>> getPosts(@PathVariable Integer userId,
                                                  @RequestParam Integer count) {

        return ResponseEntity.ok(postService.getNotViewedPosts(userId, count));
    }
}
