package ru.ivanov.ggnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivanov.ggnetwork.services.RelationsService;

@RestController
@RequestMapping("/api/users/{username}/relations")
public class RelationsController {
    @Autowired
    private RelationsService relationsService;

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@PathVariable String username,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size) {

        if ((page == null && size != null) || (page != null && size == null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findFriends(username, page, size));
        else
            return ResponseEntity.ok(relationsService.findFriends(username));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable String username,
                                       @RequestParam String subscriptionUsername) {
        relationsService.subscribe(username, subscriptionUsername);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@PathVariable String username,
                                       @RequestParam String subscriptionUsername) {
        relationsService.unsubscribe(username, subscriptionUsername);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptions(@PathVariable String username,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {

        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findSubscriptions(username, page, size));
        else
            return ResponseEntity.ok(relationsService.findSubscriptions(username));
    }

    @GetMapping("/subscribers")
    public ResponseEntity<?> getSubscribers(@PathVariable String username,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {

        if ((page != null && size == null) || (page == null && size != null))
            return ResponseEntity.badRequest().body("param page must be declared with param size");
        if (page != null)
            return ResponseEntity.ok(relationsService.findSubscribers(username, page, size));
        else
            return ResponseEntity.ok(relationsService.findSubscribers(username));
    }

    @GetMapping("/check/subscription")
    public ResponseEntity<?> checkSubscribe(@PathVariable String username,
                                            @RequestParam String subscriptionUsername) {
        return ResponseEntity.ok(relationsService.isSubscriber(username, subscriptionUsername));
    }

    @GetMapping("/check/friend")
    public ResponseEntity<?> checkFriend(@PathVariable String username,
                                         @RequestParam String friendUsername) {
        return ResponseEntity.ok(relationsService.isFriends(username, friendUsername));
    }



}
