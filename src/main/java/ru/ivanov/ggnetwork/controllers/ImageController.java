package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivanov.ggnetwork.services.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable Integer imageId) {
        var imageResource = imageService.get(imageId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
    }
}
