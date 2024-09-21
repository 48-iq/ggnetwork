package ru.ivanov.ggnetwork.dto.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.validation.GroupCreateDtoValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidatedBy(GroupCreateDtoValidator.class)
public class GroupCreateDto {
    private String title;
    private String description;
    private MultipartFile icon;
}
