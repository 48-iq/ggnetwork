package ru.ivanov.ggnetwork.authorization;

import ru.ivanov.ggnetwork.entities.User;

import java.util.Map;

public interface Authorizer {
    boolean checkAuthorize(User user, Map<String, Integer> resources);
}
