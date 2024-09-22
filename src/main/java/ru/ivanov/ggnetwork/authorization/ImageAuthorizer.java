package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.UserImageRepository;

@Component
public class ImageAuthorizer implements Authorizer {
    @Autowired
    private UserImageRepository userImageRepository;

    @Override
    public boolean checkAuthorize(User user, Integer entityId) {
        return userImageRepository.checkOnBelong(user.getId(), entityId);
    }
}
