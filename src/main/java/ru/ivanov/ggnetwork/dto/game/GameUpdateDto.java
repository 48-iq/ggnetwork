package ru.ivanov.ggnetwork.dto.game;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.GameUpdateDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidatedBy(GameUpdateDtoValidator.class)
public class GameUpdateDto {
    private String title;
    private String description;
    private MultipartFile icon;
}
