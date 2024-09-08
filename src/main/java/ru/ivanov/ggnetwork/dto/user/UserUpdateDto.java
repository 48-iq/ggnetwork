package ru.ivanov.ggnetwork.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    private String name;
    private String surname;
    private String email;
    private String status;
}
