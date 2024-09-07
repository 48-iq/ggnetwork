package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_chat_messages")
public class GroupChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    @Column(nullable = false)
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private User creator;
    @ManyToOne
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id", nullable = false)
    private GroupChat groupChat;
}
