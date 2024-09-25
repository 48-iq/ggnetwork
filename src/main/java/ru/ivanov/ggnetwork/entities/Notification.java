package ru.ivanov.ggnetwork.entities;


import java.time.LocalDateTime;

public class Notification {
    private Integer id;
    private String content;
    private LocalDateTime time;
    private NotificationType type;
}
