package ru.ivanov.ggnetwork.dto.game;

import lombok.*;
import ru.ivanov.ggnetwork.entities.Game;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GameDto {
    private String title;
    private String description;
    private String icon;

    public static GameDto from(Game game) {
        return GameDto.builder()
                .title(game.getTitle())
                .icon(game.getIcon())
                .description(game.getDescription())
                .build();
    }
}
