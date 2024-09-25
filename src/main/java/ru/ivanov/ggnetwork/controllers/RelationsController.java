package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.authorization.UserAuthorizer;
import ru.ivanov.ggnetwork.services.RelationsService;

@RestController
@RequestMapping("/api/users/{userId}")
public class RelationsController {
    @Autowired
    private RelationsService relationsService;

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@PathVariable Integer userId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {

        if ((page == null && size != null) || (page != null && size == null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findFriends(userId, page, size));
        else
            return ResponseEntity.ok(relationsService.findFriends(userId));
    }

    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/subscribe-to-user/{subscriptionUserId}")
    public ResponseEntity<?> subscribe(@PathVariable @ResourceId Integer userId,
                                       @PathVariable Integer subscriptionUserId) {
        relationsService.subscribe(userId, subscriptionUserId);
        return ResponseEntity.ok().build();
    }
    @AuthorizedBy(UserAuthorizer.class)
    @PostMapping("/unsubscribe-from-user/{subscriptionUserId}")
    public ResponseEntity<?> unsubscribe(@PathVariable @ResourceId Integer userId,
                                         @RequestParam Integer subscriptionUserId) {
        relationsService.unsubscribe(userId, subscriptionUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptions(@PathVariable Integer userId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {

        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findSubscriptions(userId, page, size));
        else
            return ResponseEntity.ok(relationsService.findSubscriptions(userId));
    }

    @GetMapping("/subscribers")
    public ResponseEntity<?> getSubscribers(@PathVariable Integer userId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {

        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findSubscribers(userId, page, size));
        else
            return ResponseEntity.ok(relationsService.findSubscribers(userId));
    }

    @GetMapping("/check/subscription/{subscriptionUserId}")
    public ResponseEntity<?> checkSubscribe(@PathVariable Integer userId,
                                            @PathVariable Integer subscriptionUserId) {
        return ResponseEntity.ok(relationsService.isSubscriber(userId, subscriptionUserId));
    }

    @GetMapping("/check/friend/{friendUserId}")
    public ResponseEntity<?> checkFriend(@PathVariable Integer userId,
                                         @PathVariable Integer friendUserId) {
        return ResponseEntity.ok(relationsService.isFriends(userId, friendUserId));
    }



}
