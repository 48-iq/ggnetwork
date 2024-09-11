package ru.ivanov.ggnetwork.dto.group;

import lombok.*;
import ru.ivanov.ggnetwork.entities.Group;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupDto {
    private String title;
    private String description;
    private String icon;

    public static GroupDto from(Group group) {
        return GroupDto.builder()
                .title(group.getTitle())
                .description(group.getDescription())
                .icon(group.getIcon())
                .build();
    }
}
