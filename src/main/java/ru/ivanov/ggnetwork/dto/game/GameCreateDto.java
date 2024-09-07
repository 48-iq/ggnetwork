package ru.ivanov.ggnetwork.dto.game;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.validation.annotations.NewGameTitleConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GameCreateDto {
    @NewGameTitleConstraint
    @NotEmpty
    @NotNull
    private String title;
    private String description;
    private MultipartFile icon;
}
