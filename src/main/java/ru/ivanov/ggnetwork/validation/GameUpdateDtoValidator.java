package ru.ivanov.ggnetwork.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.game.GameCreateDto;
import ru.ivanov.ggnetwork.dto.game.GameUpdateDto;

import java.util.regex.Pattern;

@Component
public class GameUpdateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GameUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var gameUpdateDto = (GameUpdateDto) target;
        if (gameUpdateDto == null){
            errors.reject("game update dto is null");
            return;
        }
        if (gameUpdateDto.getIcon() != null) {
            if (gameUpdateDto.getIcon().getSize() > 1024 * 1024 * 100)
                errors.reject("icon", "icon size > 100MB");
        }
        if (gameUpdateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(gameUpdateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
        }
        if (gameUpdateDto.getDescription() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(gameUpdateDto.getDescription());
            if (!matcher.matches())
                errors.reject("description", "description is invalid");
        }
    }
}
