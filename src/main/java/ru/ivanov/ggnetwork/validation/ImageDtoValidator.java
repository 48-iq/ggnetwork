package ru.ivanov.ggnetwork.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.dto.image.ImageDto;

@Component
public class ImageDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ImageDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var imageDto = (ImageDto) target;
        if (imageDto == null) {
            errors.reject("imageDto","image dto is null");
            return;
        }
        if (imageDto.getImage() == null)
            errors.reject("image", "image is null");
        else
            if (imageDto.getImage().getSize() > 1024 * 1024 * 100)
                errors.reject("image", "image size > 100MB");
    }
}
