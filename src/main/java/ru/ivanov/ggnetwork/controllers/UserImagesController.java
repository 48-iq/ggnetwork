package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.authorization.UserImageAuthorizer;
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
    public ResponseEntity<Integer> addImage(@PathVariable @ResourceId Integer userId,
                                            @ModelAttribute @UseValidator ImageDto imageDto) {
        var image = userImageService.addImage(userId, imageDto.getImage());
        return ResponseEntity.ok(image);
    }

    @AuthorizedBy(UserAuthorizer.class)
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable @ResourceId Integer userId,
                                            @PathVariable Integer imageId) {

        userImageService.removeImage(userId, imageId);
        return ResponseEntity.ok().build();
    }

    @AuthorizedBy(UserImageAuthorizer.class)
    @PutMapping("/icon/{imageId}")
    public ResponseEntity<Void> setIcon(@PathVariable @ResourceId Integer userId,
                                        @PathVariable @ResourceId Integer imageId) {
        userImageService.setIcon(userId, imageId);
        return ResponseEntity.ok().build();
    }

}
