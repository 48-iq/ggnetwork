package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.game.GameCreateDto;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.dto.game.GameUpdateDto;
import ru.ivanov.ggnetwork.entities.Game;
import ru.ivanov.ggnetwork.services.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/{title}")
    public ResponseEntity<GameDto> getGame(@PathVariable String title) {
        var game = gameService.findGameByTitle(title);
        return ResponseEntity.ok(game);
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@ModelAttribute GameCreateDto gameDto) {
        var game = gameService.createGame(gameDto);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/{title}")
    public ResponseEntity<GameDto> updateGame(@ModelAttribute GameUpdateDto gameDto,
                                              @PathVariable String title) {
        var game = gameService.updateGame(title, gameDto);
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteGame(@PathVariable String title) {
        gameService.deleteGame(title);
        return ResponseEntity.ok().build();
    }
}
