package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.GroupPostRepository;

import java.util.Map;

@Component
public class GroupPostAuthorizer implements Authorizer {
    @Autowired
    private GroupPostRepository groupPostRepository;

    @Override
    public boolean checkAuthorize(User user, Map<String, Integer> resources) {
        Integer groupPostId = resources.get("postId");
        return groupPostRepository.checkOnBelong(user.getId(), groupPostId);
    }
}
