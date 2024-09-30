package ru.ivanov.ggnetwork.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatCreateDto {
    private List<Integer> users;
    private String title;
    private MultipartFile icon;
}
