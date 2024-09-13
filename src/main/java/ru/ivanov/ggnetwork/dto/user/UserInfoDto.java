package ru.ivanov.ggnetwork.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ivanov.ggnetwork.entities.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private Integer id;
    private String username;
    private String name;
    private String surname;
    private Integer icon;
    private List<Integer> images;
    private String status;
    private String email;

    public static UserInfoDto from(User user, List<Integer> images) {
        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .status(user.getStatus())
                .icon(user.getIcon())
                .images(images)
                .build();
    }
}
