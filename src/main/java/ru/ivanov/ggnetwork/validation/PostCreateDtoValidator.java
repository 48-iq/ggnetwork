package ru.ivanov.ggnetwork.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;

import java.util.regex.Pattern;

@Component
public class PostCreateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PostCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var postCreateDto = (PostCreateDto) target;
        if (postCreateDto == null) {
            errors.reject("post create dto is null");
            return;
        }
        if (postCreateDto.getImage() != null) {
            if (postCreateDto.getImage().getSize() > 1024 * 1024 * 100)
                errors.reject("image", "image size > 100MB");
        }
        if (postCreateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(postCreateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
        }
        if (postCreateDto.getContent() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(postCreateDto.getContent());
            if (!matcher.matches())
                errors.reject("content", "content is invalid");
        }

    }
}
