package ru.ivanov.ggnetwork.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageNotificationDto {
    private Integer id;
    private String time;
    private MessageDto message;
}
