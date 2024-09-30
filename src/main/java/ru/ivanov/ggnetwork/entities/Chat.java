package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany
    @JoinTable(name = "users_chats",
    joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private Integer icon;
    private String title;
}
