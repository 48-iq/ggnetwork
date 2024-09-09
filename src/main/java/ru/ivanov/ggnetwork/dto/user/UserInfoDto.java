package ru.ivanov.ggnetwork.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private String username;
    private String name;
    private String surname;
    private String icon;
    private List<String> images;
    private String status;
    private String email;
}
