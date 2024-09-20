package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.group.GroupCreateDto;
import ru.ivanov.ggnetwork.dto.group.GroupDto;
import ru.ivanov.ggnetwork.dto.group.GroupUpdateDto;
import ru.ivanov.ggnetwork.entities.Group;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GroupRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserRepository userRepository;

    private void throwEntityNotFoundException(Integer groupId) {
        throw new EntityNotFoundException("group with id " + groupId + " not found");
    }


    public GroupDto findGroupById(Integer groupId) {
        var group = groupRepository.findById(groupId);
        if (group.isEmpty())
            throwEntityNotFoundException(groupId);
        return GroupDto.from(group.get());
    }

    public List<GroupDto> findGroupsByQuery(String query) {
        return groupRepository.findGroupsByQuery(query)
                .stream().map(GroupDto::from).toList();
    }

    private PageDto<GroupDto> pageFrom(Page<Group> page) {
        var pageDto = new PageDto<GroupDto>();
        pageDto.setPage(page.getNumber());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setSize(page.getSize());
        pageDto.setData(page.get().map(GroupDto::from).toList());
        return pageDto;
    }

    public PageDto<GroupDto> findGroupsByQuery(String query, Integer page, Integer size) {
        return pageFrom(groupRepository.findGroupsByQuery(query, PageRequest.of(page, size)));
    }

    @Transactional
    public GroupDto createGroup(GroupCreateDto groupCreateDto, Integer userId) {
        var group = Group.builder()
                .title(groupCreateDto.getTitle())
                .description(groupCreateDto.getDescription())
                .build();
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with userId " + userId + " not found");
        group.setOwner(userOptional.get());
        if (groupCreateDto.getIcon() != null) {
            group.setIcon(imageService.save(groupCreateDto.getIcon()));
        }
        var savedGroup = groupRepository.save(group);
        return GroupDto.from(group);
    }

    @Transactional
    public GroupDto updateGroup(Integer groupId, GroupUpdateDto groupUpdateDto) {
        var groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty())
            throwEntityNotFoundException(groupId);
        var group = groupOptional.get();
        if (groupUpdateDto.getIcon() != null)
            imageService.update(groupUpdateDto.getIcon(), group.getIcon());
        group.setDescription(groupUpdateDto.getDescription());
        var savedGroup = groupRepository.save(group);
        return GroupDto.from(savedGroup);
    }

    @Transactional
    public void deleteGroup(Integer groupId) {
        var groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            var group = groupOptional.get();
            if (group.getIcon() != null)
                imageService.delete(group.getIcon());
            groupRepository.removeUsersAssociations(group.getId());
            groupRepository.deleteById(group.getId());
        }
    }

    public List<GroupDto> findAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupDto::from).toList();
    }

    public PageDto<GroupDto> findAllGroups(Integer page, Integer size) {
        return pageFrom(groupRepository.findAll(PageRequest.of(page, size)));
    }


}
