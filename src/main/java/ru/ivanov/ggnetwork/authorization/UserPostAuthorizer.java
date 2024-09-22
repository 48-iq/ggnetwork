package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.UserPostRepository;

@Component
public class UserPostAuthorizer implements Authorizer {
    @Autowired
    private UserPostRepository userPostRepository;
    @Override
    public boolean checkAuthorize(User user, Integer entityId) {
        return userPostRepository.checkOnBelong(user.getId(), entityId);
    }
}
