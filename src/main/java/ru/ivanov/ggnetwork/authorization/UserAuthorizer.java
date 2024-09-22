package ru.ivanov.ggnetwork.authorization;

import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;

@Component
public class UserAuthorizer implements Authorizer {
    @Override
    public boolean checkAuthorize(User user, Integer entityId) {
        return (user.getId().equals(entityId));
    }
}
