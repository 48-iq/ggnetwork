package ru.ivanov.ggnetwork.dto.image;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {
    private MultipartFile image;
    {
        String a = "a" + null;
    }
}
