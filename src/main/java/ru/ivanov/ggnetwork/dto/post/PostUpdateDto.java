package ru.ivanov.ggnetwork.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.PostUpdateDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ValidatedBy(PostUpdateDtoValidator.class)
public class PostUpdateDto {
    private String title;
    private String content;
    private MultipartFile image;
}
