package ru.ivanov.ggnetwork.dto.game;

import lombok.*;
import ru.ivanov.ggnetwork.entities.Game;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GameDto {
    private Integer id;
    private String title;
    private String description;
    private Integer icon;

    public static GameDto from(Game game) {
        return GameDto.builder()
                .id(game.getId())
                .title(game.getTitle())
                .icon(game.getIcon())
                .description(game.getDescription())
                .build();
    }
}
