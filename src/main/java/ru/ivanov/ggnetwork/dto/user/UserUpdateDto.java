package ru.ivanov.ggnetwork.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.UserUpdateDtoValidator;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidatedBy(UserUpdateDtoValidator.class)
public class UserUpdateDto {
    private String name;
    private String surname;
    private String email;
    private String status;
}
