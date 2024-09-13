package ru.ivanov.ggnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.entities.Image;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.ImageRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UuidService uuidService;

    public Integer save(MultipartFile file) {
        try {
            if (!Files.exists(Path.of("/images")))
                Files.createDirectory(Path.of("/images"));
            var filename = uuidService.generate();
            while(imageRepository.existsByFilename(filename))
                filename = uuidService.generate();
            var filepath = Path.of("/images/" + filename);
            if (!Files.exists(filepath))
                Files.createFile(filepath);
            try (var outputStream = new FileOutputStream(filepath.toFile())) {
                outputStream.write(file.getBytes());
            }
            var savedImage = imageRepository.save(Image.builder()
                    .filename(filename)
                    .filepath(filepath.toString())
                    .build());
            return savedImage.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(MultipartFile file, Integer id) {
        try {
            var imageOptional = imageRepository.findById(id);
            if (imageOptional.isEmpty())
                throw new EntityNotFoundException("image with id " + id + " not found");
            var filepath = imageOptional.get().getFilepath();
            try (var fileOutputStream = new FileOutputStream(filepath)) {
                fileOutputStream.write(file.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStreamResource get(Integer id) {
        try {
            var imageOptional = imageRepository.findById(id);
            if (imageOptional.isEmpty())
                throw new EntityNotFoundException("image with id " + id + " not found");
            var filepath = imageOptional.get().getFilepath();
            var fileInputStream = new FileInputStream(filepath);
            return new InputStreamResource(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id) {
        try {
            var imageOptional = imageRepository.findById(id);
            if (imageOptional.isPresent()) {
                var image = imageOptional.get();
                if (Files.exists(Path.of(image.getFilepath()))) {
                    Files.delete(Path.of(image.getFilepath()));
                }
                imageRepository.deleteById(image.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
