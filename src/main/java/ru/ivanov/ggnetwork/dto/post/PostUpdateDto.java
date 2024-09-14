package ru.ivanov.ggnetwork.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostUpdateDto {
    private String title;
    private String content;
    private MultipartFile image;
}
