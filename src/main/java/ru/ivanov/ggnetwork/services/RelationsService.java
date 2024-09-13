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

    private void throwEntityNotFoundException(Integer userId) {
        throw new EntityNotFoundException("user with userId " + userId + " not found");
    }


    private PageDto<UserDto> pageDtoFrom(Page<User> page) {
        var pageDto = new PageDto<UserDto>();
        pageDto.setData(page.get().map(UserDto::from).toList());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setSize(page.getSize());
        pageDto.setPage(page.getNumber());
        return pageDto;
    }

    public List<UserDto> findFriends(Integer userId) {
        if (!userRepository.existsById(userId))
           throwEntityNotFoundException(userId);
        var users = userRepository.findFriends(userId);
        return users.stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findFriends(Integer userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return pageDtoFrom(userRepository.findFriends(userId, PageRequest.of(page, size)));
    }

    @Transactional
    public void subscribe(Integer subscribedUserId, Integer userId) {
        if (!userRepository.existsById(subscribedUserId))
            throwEntityNotFoundException(userId);
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        userRepository.subscribe(subscribedUserId, userId);
    }

    @Transactional
    public void unsubscribe(Integer subscribedUseId, Integer userId) {
        if (!userRepository.existsById(subscribedUseId))
            throwEntityNotFoundException(userId);
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);

        userRepository.unsubscribe(subscribedUseId, userId);
    }

    public boolean isSubscriber(Integer subscribedUserId, Integer userId) {
        if (!userRepository.existsById(subscribedUserId))
            throwEntityNotFoundException(userId);
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return userRepository.isSubscriber(subscribedUserId, userId);
    }


    public List<UserDto> findSubscriptions(Integer userId) {
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return userRepository.findSubscriptions(userId).stream()
                .map(UserDto::from).toList();
    }

    public PageDto<UserDto> findSubscriptions(Integer userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return pageDtoFrom(userRepository.findSubscriptions(userId, PageRequest.of(page,size)));
    }

    public List<UserDto> findSubscribers(Integer userId) {
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return userRepository.findSubscribers(userId)
                .stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findSubscribers(Integer userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId))
            throwEntityNotFoundException(userId);
        return pageDtoFrom(userRepository.findSubscribers(userId, PageRequest.of(page, size)));
    }

    public boolean isFriends(Integer firstUserId, Integer secondUserId) {
        if (!userRepository.existsById(firstUserId))
            throwEntityNotFoundException(firstUserId);
        if (!userRepository.existsById(secondUserId))
            throwEntityNotFoundException(secondUserId);
        return userRepository.isFriends(firstUserId, secondUserId);
    }



}
