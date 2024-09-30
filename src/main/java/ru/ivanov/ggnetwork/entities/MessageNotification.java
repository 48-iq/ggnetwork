package ru.ivanov.ggnetwork.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private User user;


}
