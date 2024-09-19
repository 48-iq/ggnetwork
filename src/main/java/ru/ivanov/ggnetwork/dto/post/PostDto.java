package ru.ivanov.ggnetwork.dto.post;

import lombok.*;
import ru.ivanov.ggnetwork.entities.GroupPost;
import ru.ivanov.ggnetwork.entities.UserPost;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostDto {
    public static final String USER_POST = "USER_POST";
    public static final String GROUP_POST = "GROUP_POST";

    public static final String LIKE = "LIKE";
    public static final String DISLIKE = "DISLIKE";

    private Integer id;
    private String title;
    private Integer image;
    private String content;
    private String type;
    private Integer creator;
    private String date;
    private Integer likes;
    private Integer dislikes;
    private String grade;
    private boolean isEdited;

    public static PostDto from(UserPost post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        return PostDto.builder()
                .id(post.getId())
                .image(post.getImage())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(post.getLikes())
                .dislikes(post.getDislikes())
                .creator(post.getCreator().getId())
                .date(formatter.format(post.getTime()))
                .type(USER_POST)
                .build();
    }

    public static PostDto from(GroupPost post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        return PostDto.builder()
                .id(post.getId())
                .image(post.getImage())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(post.getLikes())
                .dislikes(post.getDislikes())
                .creator(post.getId())
                .date(formatter.format(post.getTime()))
                .type(GROUP_POST)
                .build();
    }
}
