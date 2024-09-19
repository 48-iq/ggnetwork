package ru.ivanov.ggnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;
import ru.ivanov.ggnetwork.entities.GroupPost;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GroupPostRepository;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupPostService {
    @Autowired
    private GroupPostRepository groupPostRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ImageService imageService;

    public PostDto createPost(Integer groupId, PostCreateDto postCreateDto) {
        var groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty())
            throw new EntityNotFoundException("group with id " + groupId + " not found");
        var group = groupOptional.get();
        var post = GroupPost.builder()
                .likes(0)
                .dislikes(0)
                .creator(group)
                .time(LocalDateTime.now())
                .content(postCreateDto.getContent())
                .title(postCreateDto.getTitle())
                .isEdited(false)
                .build();
        if (postCreateDto.getImage() != null)
            post.setImage(imageService.save(postCreateDto.getImage()));
        var savedPost = groupPostRepository.save(post);
        return PostDto.from(savedPost);
    }

    public PostDto updatePost(Integer postId, PostUpdateDto postUpdateDto) {
        var postOptional = groupPostRepository.findById(postId);
        if (postOptional.isEmpty())
            throw new EntityNotFoundException("post with id " + postId + "not found");
        var post = postOptional.get();
        post.setContent(postUpdateDto.getContent());
        post.setTitle(postUpdateDto.getTitle());
        post.setEdited(true);
        if (postUpdateDto.getImage() != null)
            imageService.update(postUpdateDto.getImage(), post.getImage());
        var savedPost = groupPostRepository.save(post);
        return PostDto.from(savedPost);
    }

    public PostDto getPostById(Integer postId) {
        return PostDto.from(groupPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post with id " + postId + "not found"))
        );
    }

    public List<PostDto> getPostsByGroup(Integer groupId) {
        return groupPostRepository.findPostsByGroup(groupId)
                .stream().map(PostDto::from).toList();
    }

    private PageDto<PostDto> pageDtoFrom(Page<GroupPost> page) {
        var pageDto = new PageDto<PostDto>();
        pageDto.setSize(page.getSize());
        pageDto.setData(page.get().map(PostDto::from).toList());
        pageDto.setTotal(page.getTotalPages());
        pageDto.setPage(page.getNumber());
        return pageDto;
    }
    public PageDto<PostDto> getPostsByGroup(Integer groupId, Integer page, Integer size) {
        return pageDtoFrom(groupPostRepository
                .findPostsByGroup(groupId, PageRequest.of(page, size)));
    }

}
