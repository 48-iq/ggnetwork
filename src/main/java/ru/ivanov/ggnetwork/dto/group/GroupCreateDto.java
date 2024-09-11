package ru.ivanov.ggnetwork.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.validation.annotations.NewGroupTitleConstraint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCreateDto {
    @NewGroupTitleConstraint
    private String title;
    private String description;
    private MultipartFile icon;
}
