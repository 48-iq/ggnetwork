package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private Role role;
    private String email;
    private String name;
    private String surname;
    private String status;
    private String icon;

    @ManyToMany
    @JoinTable(name = "games_user_has_played",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"}),
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"))
    private List<Game> gamesUserHasPlayed;

    @ManyToMany
    @JoinTable(name = "games_user_plays",
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"}),
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"))
    private List<Game> gamesUserPlays;

    @ManyToMany
    @JoinTable(name = "subscriptions",
            uniqueConstraints = @UniqueConstraint(columnNames = {"subscribed_user_id", "user_id"}),
            joinColumns = @JoinColumn(name = "subscribed_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> subscriptions;

    @ManyToMany(mappedBy = "users")
    private List<Group> groups;

}
