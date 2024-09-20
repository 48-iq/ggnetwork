package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_post_grades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPostGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private GroupPost groupPost;
    @Enumerated(EnumType.STRING)
    private GradeType type;
}
