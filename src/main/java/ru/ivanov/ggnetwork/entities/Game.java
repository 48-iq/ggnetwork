package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String title;
    private String description;
    private Integer icon;


    @ManyToMany(mappedBy = "gamesUserHasPlayed")
    private List<User> usersHasPlayed;

    @ManyToMany(mappedBy = "gamesUserPlays")
    private List<User> usersPlays;

}
