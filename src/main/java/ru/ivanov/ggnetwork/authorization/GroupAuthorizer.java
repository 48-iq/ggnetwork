package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

@Component
public class GroupAuthorizer implements Authorizer {
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public boolean checkAuthorize(User user, Integer entityId) {
        return groupRepository.checkOnBelong(user.getId(), entityId);
    }
}
