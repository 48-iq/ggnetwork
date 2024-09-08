package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.image.ImageDto;
import ru.ivanov.ggnetwork.services.UserImageService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/images")
public class UserImagesController {
    @Autowired
    private UserImageService userImageService;

    @GetMapping
    public ResponseEntity<List<String>> getImagesByUser(@PathVariable String username) {
        var images = userImageService.getImagesByUser(username);
        return ResponseEntity.ok(images);
    }

    @PostMapping
    public ResponseEntity<String> addImage(@PathVariable String username,
                                           @ModelAttribute ImageDto imageDto) {
        var image = userImageService.addImage(username, imageDto.getFile());
        return ResponseEntity.ok(image);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImage(@PathVariable String username,
                                            @RequestParam String image) {

        userImageService.removeImage(username, image);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/icon")
    public ResponseEntity<Void> setIcon(@PathVariable String username,
                                        @RequestParam String image) {

        userImageService.setIcon(username, image);
        return ResponseEntity.ok().build();
    }
}
