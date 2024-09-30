package ru.ivanov.ggnetwork.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.entities.Chat;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatInfoDto {
    private Integer id;
    private String title;
    private Integer icon;
    private List<UserDto> users;

    public static ChatInfoDto from(Chat chat) {
        List<UserDto> users = Collections.emptyList();
        if (chat.getUsers() != null)
            users = chat.getUsers().stream().map(UserDto::from).toList();
        return ChatInfoDto.builder()
                .id(chat.getId())
                .icon(chat.getIcon())
                .title(chat.getTitle())
                .users(users)
                .build();
    }
}
