package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime time;
    @Column(unique = true)
    private String uuid;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Group creator;


}
