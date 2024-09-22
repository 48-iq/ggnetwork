package ru.ivanov.ggnetwork.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.post.PostCreateDto;
import ru.ivanov.ggnetwork.dto.post.PostUpdateDto;

import java.util.regex.Pattern;

@Component
public class PostUpdateDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PostUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var postUpdateDto = (PostUpdateDto) target;
        if (postUpdateDto == null) {
            errors.reject("postUpdateDto","post update dto is null");
            return;
        }
        if (postUpdateDto.getImage() != null) {
            if (postUpdateDto.getImage().getSize() > 1024 * 1024 * 100)
                errors.reject("image", "image size > 100MB");
        }
        if (postUpdateDto.getTitle() == null)
            errors.reject("title", "title is null");
        else {
            var pattern = Pattern.compile("^.{0,64}$");
            var matcher = pattern.matcher(postUpdateDto.getTitle());
            if (!matcher.matches())
                errors.reject("title", "title is invalid");
        }
        if (postUpdateDto.getContent() != null) {
            var pattern = Pattern.compile("^.{0,255}$");
            var matcher = pattern.matcher(postUpdateDto.getContent());
            if (!matcher.matches())
                errors.reject("content", "content is invalid");
        }
    }
}
