package ru.ivanov.ggnetwork.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivanov.ggnetwork.entities.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String status;
    private String email;
    private String icon;

    public static UserDto from(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .icon(user.getIcon())
                .name(user.getName())
                .surname(user.getSurname())
                .status(user.getStatus())
                .build();
    }
}

