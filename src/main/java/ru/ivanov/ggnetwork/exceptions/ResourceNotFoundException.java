package ru.ivanov.ggnetwork.exceptions;

public class ResourceNotFoundException extends AuthorizerException {
    public ResourceNotFoundException(String resourceName) {
        super("resource " + resourceName + " not found");
    }
}
