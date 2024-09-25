package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.entities.Image;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.ImageRepository;
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
    @Autowired
    private ImageRepository imageRepository;


    public List<Integer> getImagesByUser(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + " not found");
        return  userImageRepository.findImagesByUser(userId);
    }

    @Transactional
    public Integer addImage(Integer userId, MultipartFile file) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id" + userId + " not found");
        var image = imageService.save(file);
        userImageRepository.addUserImageAssociation(userId, image);
        return image;
    }

    @Transactional
    public void removeImage(Integer userId, Integer imageId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with id " + userId + " not found");
        var user = userOptional.get();
        if (imageId.equals(user.getIcon()))
            user.setIcon(null);
        userImageRepository.removeUserImageAssociation(userId, imageId);
        imageService.delete(imageId);
    }

    @Transactional
    public void removeAllImages(Integer userId) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with id " + userId + " not found");
        var images = userImageRepository.getAllUserImages(userId);
        userImageRepository.removeAllUserImagesAssociations(userId);
        for (var image: images) {
            imageService.delete(image.getId());
        }
    }

    @Transactional
    public void setIcon(Integer userId, Integer imageId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + userId + " not found"));
        user.setIcon(imageId);
        userRepository.save(user);
    }

}
