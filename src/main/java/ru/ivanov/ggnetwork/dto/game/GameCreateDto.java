package ru.ivanov.ggnetwork.dto.game;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.GameCreateDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@ValidatedBy(GameCreateDtoValidator.class)
public class GameCreateDto {
    private String title;
    private String description;
    private MultipartFile icon;
}
