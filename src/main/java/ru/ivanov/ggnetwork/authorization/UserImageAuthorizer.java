package ru.ivanov.ggnetwork.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;

import java.util.Map;

@Component
public class UserImageAuthorizer implements Authorizer{
    @Autowired
    private UserAuthorizer userAuthorizer;
    @Autowired
    private ImageAuthorizer imageAuthorizer;


    @Override
    public boolean checkAuthorize(User user, Map<String, Integer> resources) {
        return (userAuthorizer.checkAuthorize(user, resources)
        && imageAuthorizer.checkAuthorize(user, resources));
    }
}
