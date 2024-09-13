package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.services.UserGamesService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/games")
public class UserGamesController {
    @Autowired
    private UserGamesService userGamesService;

    @PostMapping("/plays")
    public ResponseEntity<Void> addGameToUserHasPlays(@PathVariable Integer userId,
                                        @RequestParam Integer gameId) {

        userGamesService.addGameToUserPlays(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/played")
    public ResponseEntity<Void> addGameToUserHasPlayed(@PathVariable Integer userId,
                                                @RequestParam Integer gameId) {

        userGamesService.addGameToUserHasPlayed(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/plays")
    public ResponseEntity<Void> deleteGameFromPlays(@PathVariable Integer userId,
                                                    @RequestParam Integer gameId) {

        userGamesService.removeGameFromUserPlays(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/played")
    public ResponseEntity<Void> deleteGameFromHasPlayed(@PathVariable Integer userId,
                                                    @RequestParam Integer gameId) {

        userGamesService.removeGameFromUserHasPlayed(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plays")
    public ResponseEntity<?> getGamesUserPlays(@PathVariable Integer userId,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page == null)
            return ResponseEntity.ok(userGamesService.getGamesUserPlays(userId));
        else
            return ResponseEntity.ok(userGamesService.getGamesUserPlays(userId, page, size));
    }

    @GetMapping("/played")
    public ResponseEntity<?> getGamesUserHasPlayed(@PathVariable Integer userId,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page == null)
            return ResponseEntity.ok(userGamesService.getGamesUserHasPlayed(userId));
        else
            return ResponseEntity.ok(userGamesService.getGamesUserHasPlayed(userId, page, size));
    }
}
