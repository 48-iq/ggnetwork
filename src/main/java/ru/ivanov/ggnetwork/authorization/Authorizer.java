package ru.ivanov.ggnetwork.authorization;

import ru.ivanov.ggnetwork.entities.User;

public interface Authorizer {
    boolean checkAuthorize(User user, Integer entityId);
}
