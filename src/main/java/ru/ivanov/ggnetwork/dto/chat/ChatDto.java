package ru.ivanov.ggnetwork.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivanov.ggnetwork.entities.Chat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    private Integer id;
    private String title;
    private Integer icon;

    public static ChatDto from(Chat chat) {
        return ChatDto.builder()
                .id(chat.getId())
                .title(chat.getTitle())
                .icon(chat.getIcon())
                .build();
    }
}
