package ru.ivanov.ggnetwork.dto.image;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {
    @NotNull
    private MultipartFile image;
}
