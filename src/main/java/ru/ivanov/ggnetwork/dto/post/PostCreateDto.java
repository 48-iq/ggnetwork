package ru.ivanov.ggnetwork.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.PostCreateDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidatedBy(PostCreateDtoValidator.class)
public class PostCreateDto {
    private String title;
    private String content;
    private MultipartFile image;

}
