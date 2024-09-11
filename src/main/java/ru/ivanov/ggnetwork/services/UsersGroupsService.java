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

    private void checkUser(String username) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username " + username + " not found");
    }

    private void checkGroup(String title) {
        if (!groupRepository.existsByTitle(title))
            throw new EntityNotFoundException("group with title " + title + " not found");
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
    public void subscribe(String username, String title) {
        checkGroup(title);
        checkUser(username);
        groupRepository.subscribe(title, username);
    }

    @Transactional
    public void unsubscribe(String username, String title) {
        checkGroup(title);
        checkUser(username);
        groupRepository.unsubscribe(title, username);
    }

    public List<GroupDto> findGroupsByUser(String username) {
        return groupRepository.findGroupsByUser(username)
                .stream().map(GroupDto::from).toList();
    }

    public PageDto<GroupDto> findGroupsByUser(String username, Integer page, Integer size) {
        return groupPageDtoFrom(groupRepository
                .findGroupsByUser(username, PageRequest.of(page, size)));
    }

    public List<UserDto> findUsersByGroup(String title) {
        return groupRepository.findUsersByGroup(title)
                .stream().map(UserDto::from).toList();
    }

    public PageDto<UserDto> findUsersByGroup(String title, Integer page, Integer size) {
        return userPageDtoFrom(groupRepository
                .findUsersByGroup(title, PageRequest.of(page, size)));
    }

    public List<GroupDto> findGroupsByOwner(String username) {
        return groupRepository.findGroupsByOwner(username)
                .stream().map(GroupDto::from).toList();
    }

    public PageDto<GroupDto> findGroupsByOwner(String username, Integer page, Integer size) {
        return groupPageDtoFrom(groupRepository.findGroupsByOwner(username, PageRequest.of(page, size)));
    }


}
