package ru.ivanov.ggnetwork.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivanov.ggnetwork.dto.chat.ChatDto;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.entities.Message;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Integer id;
    private String content;
    private ChatDto chat;
    private UserDto sender;
    private String time;

    public static MessageDto from(Message message) {
        var dateTimeFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
        String time = null;
        if (message.getTime() != null)
            time = dateTimeFormater.format(message.getTime());
        return MessageDto.builder()
                .id(message.getId())
                .time(time)
                .sender(UserDto.from(message.getCreator()))
                .chat(ChatDto.from(message.getChat()))
                .content(message.getContent())
                .build();
    }
}
