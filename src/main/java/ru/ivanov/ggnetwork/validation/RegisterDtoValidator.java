package ru.ivanov.ggnetwork.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.auth.RegisterDto;
import ru.ivanov.ggnetwork.entities.Role;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.regex.Pattern;

@Component
public class RegisterDtoValidator implements Validator {
    @Value("${app.admin.password}")
    private String adminPassword;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var registerDto = (RegisterDto) target;
        if (registerDto == null) {
            errors.reject("registerDto", "register dto is null");
            return;
        }
        boolean roleIsValid = false;
        for (Role role: Role.values())
            if (role.name().equals(registerDto.getRole())) roleIsValid = true;
        if (!roleIsValid)
            errors.reject("role", "role is invalid");
        if (registerDto.getUsername() == null)
            errors.reject("username", "username is null");
        else {
            var pattern = Pattern.compile("^[a-zA-Z0-9_!?.,()]{3,64}$");
            var matcher = pattern.matcher(registerDto.getUsername());
            if (!matcher.matches())
                errors.reject("username", "username is invalid");
            else if (userRepository.existsByUsername(registerDto.getUsername()))
                errors.reject("username", "username already exists");
        }
        if (registerDto.getPassword() == null)
            errors.reject("password", "password is null");
        else {
            var pattern = Pattern.compile("^.{8,64}$");
            var matcher = pattern.matcher(registerDto.getPassword());
            if (!matcher.matches())
                errors.reject("password", "password is invalid");
        }
        var namePattern = Pattern.compile("^.{3,64}$");
        if (registerDto.getName() != null) {
            var matcher = namePattern.matcher(registerDto.getName());
            if (!matcher.matches())
                errors.reject("name", "name is invalid");
        }
        if (registerDto.getSurname() != null) {
            var matcher = namePattern.matcher(registerDto.getSurname());
            if (!matcher.matches())
                errors.reject("surname", "surname is invalid");
        }
        if (registerDto.getEmail() != null) {
            var pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
            var matcher = pattern.matcher(registerDto.getEmail());
            if (!matcher.matches())
                errors.reject("email", "email is invalid");
        }
        if (registerDto.getStatus() != null) {
            var pattern = Pattern.compile("^.{0,256}$");
            var matcher = pattern.matcher(registerDto.getStatus());
            if (!matcher.matches())
                errors.reject("status", "status is invalid");
        }
        if (Role.ROLE_ADMIN.name().equals(registerDto.getRole()))
            if (adminPassword != null && !adminPassword.equals(registerDto.getAdminPassword()))
                errors.reject("adminPassword", "role is admin admin password is invalid");
    }
}
