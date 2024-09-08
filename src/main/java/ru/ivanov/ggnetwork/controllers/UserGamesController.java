package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.services.UserGamesService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{username}/games")
public class UserGamesController {
    @Autowired
    private UserGamesService userGamesService;

    @PostMapping("/plays")
    public ResponseEntity<Void> addGameToUserHasPlays(@PathVariable String username,
                                        @RequestParam String title) {

        userGamesService.addGameToUserPlays(username, title);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/played")
    public ResponseEntity<Void> addGameToUserHasPlayed(@PathVariable String username,
                                                @RequestParam String title) {

        userGamesService.addGameToUserHasPlayed(username, title);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/plays")
    public ResponseEntity<Void> deleteGameFromPlays(@PathVariable String username,
                                                    @RequestParam String title) {

        userGamesService.removeGameFromUserPlays(username, title);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/played")
    public ResponseEntity<Void> deleteGameFromHasPlayed(@PathVariable String username,
                                                    @RequestParam String title) {

        userGamesService.removeGameFromUserHasPlayed(username, title);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plays")
    public ResponseEntity<?> getGamesUserPlays(@PathVariable String username,
                                                           @RequestParam(required = false) Integer page,
                                                           @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page == null)
            return ResponseEntity.ok(userGamesService.getGamesUserPlays(username));
        else
            return ResponseEntity.ok(userGamesService.getGamesUserPlays(username, page, size));
    }

    @GetMapping("/played")
    public ResponseEntity<?> getGamesUserHasPlayed(@PathVariable String username,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size) {
        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page == null)
            return ResponseEntity.ok(userGamesService.getGamesUserHasPlayed(username));
        else
            return ResponseEntity.ok(userGamesService.getGamesUserHasPlayed(username, page, size));
    }
}
