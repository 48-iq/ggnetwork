package ru.ivanov.ggnetwork.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.game.GameCreateDto;
import ru.ivanov.ggnetwork.repositories.GameRepository;

import java.util.regex.Pattern;

@Component
public class GameCreateDtoValidator implements Validator {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return GameCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var gameCreateDto = (GameCreateDto) target;
        if (gameCreateDto == null){
            errors.reject("gameCreateDto", "game create dto is null");
            return;
        }
        if (gameCreateDto.getIcon() != null) {
            if (gameCreateDto.getIcon().getSize() > 1024 * 1024 * 100)
                errors.reject("icon", "icon size > 100MB");
        }
        if (gameCreateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(gameCreateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
            else if (gameRepository.existsByTitle(gameCreateDto.getTitle()))
                errors.reject("title", "title already exists");
        }
        if (gameCreateDto.getDescription() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(gameCreateDto.getDescription());
            if (!matcher.matches())
                errors.reject("description", "description is invalid");
        }
    }
}
