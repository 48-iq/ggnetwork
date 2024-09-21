package ru.ivanov.ggnetwork.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.user.UserUpdateDto;
import ru.ivanov.ggnetwork.repositories.UserImageRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.regex.Pattern;

@Component
public class UserUpdateDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var userUpdateDto = (UserUpdateDto) target;
        if (userUpdateDto == null) {
            errors.reject("register dto is null");
            return;
        }
        var namePattern = Pattern.compile("^.{3,64}$");
        if (userUpdateDto.getName() != null) {
            var matcher = namePattern.matcher(userUpdateDto.getName());
            if (!matcher.matches())
                errors.reject("name", "name is invalid");
        }
        if (userUpdateDto.getSurname() != null) {
            var matcher = namePattern.matcher(userUpdateDto.getSurname());
            if (!matcher.matches())
                errors.reject("surname", "surname is invalid");
        }
        if (userUpdateDto.getEmail() != null) {
            var pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
            var matcher = pattern.matcher(userUpdateDto.getEmail());
            if (!matcher.matches())
                errors.reject("email", "email is invalid");
        }
        if (userUpdateDto.getStatus() != null) {
            var pattern = Pattern.compile("^.{0,256}$");
            var matcher = pattern.matcher(userUpdateDto.getStatus());
            if (!matcher.matches())
                errors.reject("status", "status is invalid");
        }
    }
}
