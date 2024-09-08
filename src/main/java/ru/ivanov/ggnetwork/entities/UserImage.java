package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String image;
}
