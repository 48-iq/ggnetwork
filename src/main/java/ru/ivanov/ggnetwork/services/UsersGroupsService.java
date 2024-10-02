package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.group.GroupDto;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.entities.Group;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GroupRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class UsersGroupsService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    private void checkUser(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with userId " + userId + " not found");
    }

    private void checkGroup(Integer groupId) {
        if (!groupRepository.existsById(groupId))
            throw new EntityNotFoundException("group with id " + groupId + " not found");
    }

    private PageDto<GroupDto> groupPageDtoFrom(Page<Group> page) {
        var pageDto = new PageDto<GroupDto>();
        pageDto.setPage(page.getNumber());
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setData(page.get().map(GroupDto::from).toList());
        return pageDto;
    }

    private PageDto<UserDto> userPageDtoFrom(Page<User> page) {
        var pageDto = new PageDto<UserDto>();
        pageDto.setPage(page.getNumber());
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setData(page.get().map(UserDto::from).toList());
        return pageDto;
    }
    @Transactional
    public void subscribe(Integer userId, Integer groupId) {
        checkGroup(groupId);
        checkUser(userId);
        groupRepository.subscribe(userId, groupId);
    }

    @Transactional
    public void unsubscribe(Integer userId, Integer groupId) {
        checkGroup(groupId);
        checkUser(userId);
        groupRepository.unsubscribe(userId, groupId);

    }

    public List<GroupDto> findGroupsByUser(Integer userId) {
        return groupRepository.findGroupsByUser(userId)
                .stream().map(GroupDto::from).toList();
    }

    public PageDto<GroupDto> findGroupsByUser(Integer userId, Integer page, Integer size) {
        return groupPageDtoFrom(groupRepository
                .findGroupsByUser(userId, PageRequest.of(page, size)));
    }

    public List<UserDto> findUsersByGroup(Integer groupId) {
        return groupRepository.findUsersByGroup(groupId)
                .stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findUsersByGroup(Integer groupId, Integer page, Integer size) {
        return userPageDtoFrom(groupRepository
                .findUsersByGroup(groupId, PageRequest.of(page, size)));
    }

    public List<GroupDto> findGroupsByOwner(Integer userId) {
        return groupRepository.findGroupsByOwner(userId)
                .stream().map(GroupDto::from).toList();
    }

    public PageDto<GroupDto> findGroupsByOwner(Integer userId, Integer page, Integer size) {
        return groupPageDtoFrom(groupRepository.findGroupsByOwner(userId, PageRequest.of(page, size)));
    }



}
