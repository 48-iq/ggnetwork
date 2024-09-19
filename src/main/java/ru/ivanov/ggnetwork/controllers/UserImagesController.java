package ru.ivanov.ggnetwork.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<Integer> addImage(@PathVariable Integer userId,
                                           @ModelAttribute @Valid ImageDto imageDto) {
        var image = userImageService.addImage(userId, imageDto.getImage());
        return ResponseEntity.ok(image);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@PathVariable Integer userId,
                                            @RequestParam Integer imageId) {

        userImageService.removeImage(userId, imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/icon")
    public ResponseEntity<Void> setIcon(@PathVariable Integer userId,
                                        @RequestParam Integer imageId) {

        userImageService.setIcon(userId, imageId);
        return ResponseEntity.ok().build();
    }
}
