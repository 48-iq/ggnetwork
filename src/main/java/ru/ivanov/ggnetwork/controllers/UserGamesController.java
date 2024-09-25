package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.services.UserGameService;

@RestController
@RequestMapping("/api/users/{userId}/games")
public class UserGamesController {
    @Autowired
    private UserGameService userGamesService;

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/plays/{gameId}")
    public ResponseEntity<Void> addGameToUserHasPlays(@PathVariable @ResourceId Integer userId,
                                                      @PathVariable Integer gameId) {
        userGamesService.addGameToUserPlays(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/played/{gameId}")
    public ResponseEntity<Void> addGameToUserHasPlayed(@PathVariable @ResourceId Integer userId,
                                                       @PathVariable Integer gameId) {

        userGamesService.addGameToUserHasPlayed(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @AuthorizedBy(UserAuthorizer.class)
    @DeleteMapping("/plays/{gameId}")
    public ResponseEntity<Void> deleteGameFromPlays(@PathVariable @ResourceId Integer userId,
                                                    @PathVariable Integer gameId) {

        userGamesService.removeGameFromUserPlays(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @AuthorizedBy(UserAuthorizer.class)
    @DeleteMapping("/played/{gameId}")
    public ResponseEntity<Void> deleteGameFromHasPlayed(@PathVariable @ResourceId Integer userId,
                                                        @PathVariable Integer gameId) {
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
