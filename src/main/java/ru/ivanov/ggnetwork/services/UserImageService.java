package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.entities.UserImage;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserImageRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class UserImageService {

    @Autowired
    private UserImageRepository userImageRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserRepository userRepository;


    public List<String> getImagesByUser(String username) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username " + username + " not found");
        return userImageRepository.findImagesByUsername(username)
                .stream().map(UserImage::getImage).toList();
    }

    @Transactional
    public String addImage(String username, MultipartFile file) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username" + username + " not found");
        var image = imageService.save(file);
        userImageRepository.addImageToUser(username, image);
        return image;
    }

    @Transactional
    public void removeImage(String username, String image) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with username " + username + " not found");
        var user = userOptional.get();
        if (image.equals(user.getIcon()))
            user.setIcon(null);
        userImageRepository.removeImageFromUser(username, image);
        imageService.delete(image);
    }

    @Transactional
    public void removeAllImages(String username) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with username " + username + " not found");
        var images = userImageRepository.getAllUserImages(username);
        userImageRepository.removeAllUserImages(username);
        for (var image: images) {
            imageService.delete(image);
        }
    }

    @Transactional
    public void setIcon(String username, String image) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw  new EntityNotFoundException("user with username " + username + " not found");
        var userImage = userImageRepository.findByImage(image);
        if (userImage.isEmpty() || !username.equals(userImage.get().getUser().getUsername()))
            throw new EntityNotFoundException("image with name " + image + " not found or not belong you");
        var user = userOptional.get();
        user.setIcon(image);
    }
}
