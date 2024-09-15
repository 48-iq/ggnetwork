package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "group_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private Integer image;
    private Integer likes;
    private Integer dislikes;
    private boolean isEdited;
    private LocalDateTime time;
    @Column(unique = true)
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Group creator;
    @ManyToMany
    @JoinTable(name = "viewed_group_posts",
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}),
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> usersWhoViewedPosts;




}
