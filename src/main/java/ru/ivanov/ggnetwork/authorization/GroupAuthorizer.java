package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.exceptions.ResourceNotFoundException;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

import java.util.Map;

@Component
public class GroupAuthorizer implements Authorizer {
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public boolean checkAuthorize(User user, Map<String, Integer> resources) {
        Integer groupId = resources.get("groupId");
        if (groupId == null)
            throw new ResourceNotFoundException("groupId");
        return groupRepository.checkOnBelong(user.getId(), groupId);
    }
}
