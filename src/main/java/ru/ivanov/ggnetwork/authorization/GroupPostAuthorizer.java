package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.GroupPostRepository;

@Component
public class GroupPostAuthorizer implements Authorizer {
    @Autowired
    private GroupPostRepository groupPostRepository;

    @Override
    public boolean checkAuthorize(User user, Integer entityId) {
        return groupPostRepository.checkOnBelong(user.getId(), entityId);
    }
}
