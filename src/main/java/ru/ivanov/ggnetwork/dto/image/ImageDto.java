package ru.ivanov.ggnetwork.dto.image;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.ImageDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidatedBy(ImageDtoValidator.class)
public class ImageDto {
    private MultipartFile image;
}
