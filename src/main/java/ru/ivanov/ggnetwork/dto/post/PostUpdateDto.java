package ru.ivanov.ggnetwork.dto.post;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostUpdateDto {
    private String title;
    private String content;
    private String MultipartFile;
}
