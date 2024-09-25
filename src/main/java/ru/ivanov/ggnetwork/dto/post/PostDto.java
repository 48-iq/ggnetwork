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
    //private Integer icon  icon: 1
    //private String name   name: "ivanov ilya" / "game groups"
    private String grade;
    private boolean isEdited;
    private String time;
    private Integer likes;
    private Integer dislikes;

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
                .time(formatter.format(post.getTime()))
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
                .time(formatter.format(post.getTime()))
                .type(GROUP_POST)
                .build();
    }
}
