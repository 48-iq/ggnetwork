package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.services.RelationsService;

@RestController
@RequestMapping("/api/users/{userId}/relations")
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

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Integer userId,
                                       @RequestParam Integer subscriptionUserId) {
        relationsService.subscribe(userId, subscriptionUserId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable Integer userId,
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

    @GetMapping("/check/subscription")
    public ResponseEntity<?> checkSubscribe(@PathVariable Integer userId,
                                            @RequestParam Integer subscriptionUserId) {
        return ResponseEntity.ok(relationsService.isSubscriber(userId, subscriptionUserId));
    }

    @GetMapping("/check/friend")
    public ResponseEntity<?> checkFriend(@PathVariable Integer userId,
                                         @RequestParam Integer friendUserId) {
        return ResponseEntity.ok(relationsService.isFriends(userId, friendUserId));
    }



}
