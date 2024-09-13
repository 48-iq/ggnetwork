package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private Integer image;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
    private boolean isEdited;
}
