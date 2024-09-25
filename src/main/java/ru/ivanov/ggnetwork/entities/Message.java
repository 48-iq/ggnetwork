package ru.ivanov.ggnetwork.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;


public class Message {
    private Integer id;
    private String content;
    private LocalDateTime time;
    private User creator;
    private Chat chat;
}
