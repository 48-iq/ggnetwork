package ru.ivanov.ggnetwork.dto.auth;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.validation.annotations.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterDto {
    @UsernameConstraint
    @NewUsernameConstraint
    private String username;
    @PasswordConstraint
    private String password;
    @RoleConstraint
    private String role;
    private String name;
    private String surname;
    private String status;
    @EmailConstraint
    private String email;
    private MultipartFile icon;
}
