package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class RelationsService {

    @Autowired
    private UserRepository userRepository;

    private void throwEntityNotFoundException(String username) {
        throw new EntityNotFoundException("user with username " + username + " not found");
    }


    private PageDto<UserDto> pageDtoFrom(Page<User> page) {
        var pageDto = new PageDto<UserDto>();
        pageDto.setData(page.get().map(UserDto::from).toList());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setSize(page.getSize());
        pageDto.setPage(page.getNumber());
        return pageDto;
    }

    public List<UserDto> findFriends(String username) {
        if (!userRepository.existsByUsername(username))
           throwEntityNotFoundException(username);
        var users = userRepository.findFriends(username);
        return users.stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findFriends(String username, Integer page, Integer size) {
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return pageDtoFrom(userRepository.findFriends(username, PageRequest.of(page, size)));
    }

    @Transactional
    public void subscribe(String subscribedUsername, String username) {
        if (!userRepository.existsByUsername(subscribedUsername))
            throwEntityNotFoundException(username);
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        if (!isSubscriber(subscribedUsername, username))
            userRepository.subscribe(subscribedUsername, username);
    }

    @Transactional
    public void unsubscribe(String subscribedUsername, String username) {
        if (!userRepository.existsByUsername(subscribedUsername))
            throwEntityNotFoundException(username);
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);

        userRepository.unsubscribe(subscribedUsername, username);
    }

    public boolean isSubscriber(String subscribedUsername, String username) {
        if (!userRepository.existsByUsername(subscribedUsername))
            throwEntityNotFoundException(username);
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return userRepository.isSubscriber(subscribedUsername, username);
    }


    public List<UserDto> findSubscriptions(String username) {
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return userRepository.findSubscriptions(username).stream()
                .map(UserDto::from).toList();
    }

    public PageDto<UserDto> findSubscriptions(String username, Integer page, Integer size) {
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return pageDtoFrom(userRepository.findSubscriptions(username, PageRequest.of(page,size)));
    }

    public List<UserDto> findSubscribers(String username) {
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return userRepository.findSubscribers(username)
                .stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findSubscribers(String username, Integer page, Integer size) {
        if (!userRepository.existsByUsername(username))
            throwEntityNotFoundException(username);
        return pageDtoFrom(userRepository.findSubscribers(username, PageRequest.of(page, size)));
    }

    public boolean isFriends(String firstUsername, String secondUsername) {
        if (!userRepository.existsByUsername(firstUsername))
            throwEntityNotFoundException(firstUsername);
        if (!userRepository.existsByUsername(secondUsername))
            throwEntityNotFoundException(secondUsername);
        return userRepository.isFriends(firstUsername, secondUsername);
    }



}
