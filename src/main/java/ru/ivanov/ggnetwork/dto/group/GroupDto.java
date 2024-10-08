package ru.ivanov.ggnetwork.dto.group;

import lombok.*;
import ru.ivanov.ggnetwork.entities.Group;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupDto {
    private Integer id;
    private String title;
    private String description;
    private Integer icon;
    private Integer subscribersCount;
    private Integer owner;

    public static GroupDto from(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .title(group.getTitle())
                .description(group.getDescription())
                .owner(group.getOwner().getId())
                .icon(group.getIcon())
                .subscribersCount(group.getSubscribersCount())
                .build();
    }
}
