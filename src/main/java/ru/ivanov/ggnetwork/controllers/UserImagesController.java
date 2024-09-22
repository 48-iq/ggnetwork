package ru.ivanov.ggnetwork.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.EntityId;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.dto.image.ImageDto;
import ru.ivanov.ggnetwork.services.UserImageService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/images")
public class UserImagesController {
    @Autowired
    private UserImageService userImageService;

    @GetMapping
    public ResponseEntity<List<Integer>> getImagesByUser(@PathVariable Integer userId) {
        var images = userImageService.getImagesByUser(userId);
        return ResponseEntity.ok(images);
    }

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping
    public ResponseEntity<Integer> addImage(@PathVariable @EntityId Integer userId,
                                            @ModelAttribute @UseValidator ImageDto imageDto) {
        var image = userImageService.addImage(userId, imageDto.getImage());
        return ResponseEntity.ok(image);
    }

    @AuthorizedBy(UserAuthorizer.class)
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable @EntityId Integer userId,
                                            @PathVariable Integer imageId) {

        userImageService.removeImage(userId, imageId);
        return ResponseEntity.ok().build();
    }

}
