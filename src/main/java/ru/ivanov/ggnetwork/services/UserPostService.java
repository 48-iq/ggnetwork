package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;
import ru.ivanov.ggnetwork.entities.GradeType;
import ru.ivanov.ggnetwork.entities.UserPost;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserPostGradeRepository;
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
    @Autowired
    private UserPostGradeRepository userPostGradeRepository;

    @Transactional
    public PostDto createPost(Integer userId, PostCreateDto postCreateDto) {
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with id " + userId + " not found");
        var time = LocalDateTime.now();
        var user = userOptional.get();
        var post = UserPost.builder()
                .creator(user)
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .likes(0)
                .dislikes(0)
                .isEdited(false)
                .time(time)
                .build();
        if (postCreateDto.getImage() != null)
            post.setImage(imageService.save(postCreateDto.getImage()));
        var savedPost = userPostRepository.save(post);
        return PostDto.from(savedPost);
    }

    @Transactional
    public void like(Integer userId, Integer postId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + " not found");
        var gradeOptional = userPostGradeRepository.findGrade(userId, postId);
        var post = userPostRepository.findById(postId).
                orElseThrow(() -> new EntityNotFoundException("post with id " + postId + " not found"));
        if (gradeOptional.isEmpty() || gradeOptional.get().getType() == GradeType.DISLIKE) {
            userPostGradeRepository.like(userId, postId);
            post.setLikes(post.getLikes() + 1);
            if (gradeOptional.isPresent())
                post.setDislikes(post.getDislikes() - 1);
            userPostRepository.save(post);
        }
    }

    @Transactional
    public void dislike(Integer userId, Integer postId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + " not found");
        var gradeOptional = userPostGradeRepository.findGrade(userId, postId);
        var post = userPostRepository.findById(postId).
                orElseThrow(() -> new EntityNotFoundException("post with id " + postId + " not found"));
        if (gradeOptional.isEmpty() || gradeOptional.get().getType() == GradeType.LIKE) {
            userPostGradeRepository.dislike(userId, postId);
            post.setDislikes(post.getDislikes() + 1);
            if (gradeOptional.isPresent())
                post.setLikes(post.getLikes() - 1);
            userPostRepository.save(post);
        }
    }

    @Transactional
    public void deleteGrade(Integer userId, Integer postId) {
        var gradeOptional = userPostGradeRepository.findGrade(userId, postId);
        var postOptional = userPostRepository.findById(postId);
        if (gradeOptional.isPresent()) {
            var grade = gradeOptional.get();
            if (postOptional.isPresent()) {
                var post = postOptional.get();
                if (grade.getType() == GradeType.LIKE) {
                    post.setLikes(post.getLikes() - 1);
                }
                else {
                    post.setDislikes(post.getDislikes() - 1);
                }
                userPostRepository.save(post);
            }
            userPostGradeRepository.deleteById(grade.getId());
        }
    }

    public String getGrade(Integer userId, Integer postId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + " not found");
        if (!userPostRepository.existsById(postId))
            throw new EntityNotFoundException("post with id " + postId + " not found");
        var gradeOptional = userPostGradeRepository.findGrade(userId, postId);
        return gradeOptional.map(userPostGrade -> userPostGrade.getType().name()).orElse(null);
    }

    @Transactional
    public void deletePost(Integer postId) {
        var postOptional = userPostRepository.findById(postId);
        if (postOptional.isPresent()) {
            var post = postOptional.get();
            if (post.getImage() != null)
                imageService.delete(post.getImage());
            userPostRepository.removePostAssociations(postId);
            userPostRepository.deleteById(postId);
        }
    }

    @Transactional
    public PostDto updatePost(Integer postId, PostUpdateDto postUpdateDto) {
        var postOptional = userPostRepository.findById(postId);
        if (postOptional.isEmpty())
            throw new EntityNotFoundException("post with id " + postId + " not found");
        var post = postOptional.get();
        post.setContent(postUpdateDto.getContent());
        post.setTitle(postUpdateDto.getTitle());
        post.setEdited(true);
        if (postUpdateDto.getImage() != null)
            imageService.update(postUpdateDto.getImage(), post.getImage());
        var savedPost = userPostRepository.save(post);
        return PostDto.from(savedPost);
    }

    public List<PostDto> findPostsByUser(Integer userId) {
        return userPostRepository.findPostsByUser(userId)
                .stream().map(PostDto::from).toList();
    }

    public PostDto findPostById(Integer postId) {
        return PostDto.from(userPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post with id " + postId + " not found"))
        );
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
