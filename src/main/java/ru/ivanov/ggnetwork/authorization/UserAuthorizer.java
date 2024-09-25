package ru.ivanov.ggnetwork.authorization;

import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;

import java.util.Map;

@Component
public class UserAuthorizer implements Authorizer {
    @Override
    public boolean checkAuthorize(User user, Map<String, Integer> resources) {
        Integer userId = resources.get("userId");
        return user.getId().equals(userId);
    }
}
