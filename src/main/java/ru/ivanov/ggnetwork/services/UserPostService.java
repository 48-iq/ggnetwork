package ru.ivanov.ggnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.entities.UserPost;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserPostRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserPostService {
    @Autowired
    private UuidService uuidService;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    public PostDto createPost(Integer userId, PostCreateDto postCreateDto) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with username " + userId + " not found");
        var time = LocalDateTime.now();
        var user = userOptional.get();
        var post = UserPost.builder()
                .creator(user)
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .time(time)
                .build();
        if (postCreateDto.getImage() != null)
            post.setImage(imageService.save(postCreateDto.getImage()));
        var savedPost = userPostRepository.save(post);
        return PostDto.from(savedPost);
    }

    public void deletePost(Integer id) {
        userPostRepository.deleteById(id);
    }

    public List<PostDto> findPostsByUser(Integer userId) {
        return userPostRepository.findPostsByUser(userId)
                .stream().map(PostDto::from).toList();
    }

    private PageDto<PostDto> pageDtoFrom(Page<UserPost> page) {
        var pageDto = new PageDto<PostDto>();
        pageDto.setData(page.get().map(PostDto::from).toList());
        pageDto.setPage(page.getNumber());
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotalPages());
        return pageDto;
    }

    public PageDto<PostDto> findPostsByUser(Integer userId, Integer page, Integer size) {
        return pageDtoFrom(userPostRepository.findPostsByUser(userId, PageRequest.of(page, size)));
    }


}
