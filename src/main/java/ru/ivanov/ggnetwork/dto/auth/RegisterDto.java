package ru.ivanov.ggnetwork.dto.auth;

import lombok.*;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.RegisterDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ValidatedBy(RegisterDtoValidator.class)
public class RegisterDto {
    private String username;
    private String password;
    private String adminPassword;
    private String role;
    private String name;
    private String surname;
    private String status;
    private String email;
}
