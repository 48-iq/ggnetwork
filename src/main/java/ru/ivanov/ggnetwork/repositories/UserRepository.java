package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);

    @Query(nativeQuery = true,
            value = "select * from users " +
                    "where (users.surname || ' ' || users.name) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.name || ' ' || users.surname) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.username) like ('%' || ?1 || '%')")
    List<User> findByQuery(String query);

    @Query(nativeQuery = true,
            value = "select * from users " +
                    "where (users.surname || ' ' || users.name) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.name || ' ' || users.surname) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.username) like ('%' || ?1 || '%')",
            countQuery = "select count(*) from (" +
                    "select * from users " +
                    "where (users.surname || ' ' || users.name) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.name || ' ' || users.surname) like ('%' || ?1 || '%') " +
                    "union " +
                    "select * from users " +
                    "where (users.username) like ('%' || ?1 || '%')" +
                    ")")
    Page<User> findByQuery(String query, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into games_user_has_played(user_id, game_id) " +
                    "values((select id from users where username = ?1), " +
                    "(select id from games where title = ?2)) " +
                    "on conflict(user_id, game_id) " +
                    "do nothing"
    )
    void addGameToGamesUserHasPlayed(String username, String title);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into games_user_plays(user_id, game_id) " +
                    "values((select id from users where username = ?1), " +
                    "(select id from games where title = ?2)) " +
                    "on conflict(user_id, game_id) " +
                    "do nothing"
    )
    void addGameToGamesUserPlays(String username, String title);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_plays " +
                    "where user_id = (select id from users where username = ?1) " +
                    "and " +
                    "game_id = (select id from games where title = ?2)"
    )
    void removeGameFromGamesUserPlays(String username, String title);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_has_played " +
                    "where user_id = (select id from users where username = ?1) " +
                    "and " +
                    "game_id = (select id from games where title = ?2)"
    )
    void removeGameFromGamesUserHasPlayed(String username, String title);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_plays " +
                    "where user_id = (select id from users where username = ?1);" +
                    "delete from games_user_has_played " +
                    "where user_id = (select id from users where username = ?1)"
    )
    void removeUserGamesAssociations(String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from groups_users " +
                    "where user_id = (select id from users where username = ?1); " +
                    "delete from groups_users " +
                    "where group_id in (select id from groups " +
                    "where owner_id = (select id from users where username = ?1)); " +
                    "delete from groups " +
                    "where id in (select id from groups " +
                    "where owner_id = (select id from users where username = ?1))")
    void removeUserGroupsAndUsersGroupsAssociations(String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from subscriptions " +
                    "where subscribed_user_id = (select id from users where username = ?1) " +
                    "or " +
                    "user_id = (select id from users where username = ?1)")
    void removeUsersUsersAssociations(String username);

    @Query(nativeQuery = true,
        value = "select * from subscriptions as s1 " +
                "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                "join users as u on s1.user_id = u.id " +
                "where s1.subscribed_user_id = (select id from users where username = ?1) " +
                "and s2.user_id = (select id from users where username = ?1)")
    List<User> findFriends(String username);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s1 " +
                    "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                    "join users as u on s1.user_id = u.id " +
                    "where s1.subscribed_user_id = (select id from users where username = ?1) " +
                    "and s2.user_id = (select id from users where username = ?1)",
            countQuery = "select count(*) from (" +
                    "select * from subscriptions as s1 " +
                    "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                    "join users as u on s1.user_id = u.id " +
                    "where s1.subscribed_user_id = (select id from users where username = ?1) " +
                    "and s2.user_id = (select id from users where username = ?1)" +
                    ")"
    )
    Page<User> findFriends(String username, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into subscriptions(subscribed_user_id, user_id) values" +
                    "((select id from users where username = ?1), " +
                    "(select id from users where username = ?2))")
    void subscribe(String subscribedUsername, String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from subscriptions where " +
                    "subscribed_user_id = (select id from users where username = ?1) " +
                    "and " +
                    "user_id = (select id from users where username = ?2)")
    void unsubscribe(String subscribedUsername, String username);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = (select id from users where username = ?1)")
    List<User> findSubscriptions(String username);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = (select id from users where username = ?1)",
            countQuery = "select count(*) from (" +
                    "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = (select id from users where username = ?1)" +
                    ")")
    Page<User> findSubscriptions(String username, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select exists(select 1 from subscriptions where " +
                    "subscribed_user_id = (select id from users where username = ?1) " +
                    "and " +
                    "user_id = (select id from users where username = ?2)" +
                    ")")
    boolean isSubscriber(String subscribedUsername, String username);

    @Query(nativeQuery = true,
            value =  "select " +
                    "exists(select 1 from subscriptions where " +
                    "subscribed_user_id = (select id from users where username = ?1) and " +
                    "user_id = (select id from users where username = ?2))" +
                    "and " +
                    "exists(select 1 from subscriptions where " +
                    "subscribed_user_id = (select id from users where username = ?2) and " +
                    "user_id = (select id from users where username = ?1))")
    boolean isFriends(String firstUsername, String secondUsername);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = (select id from users where username = ?1)")
    List<User> findSubscribers(String username);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = (select id from users where username = ?1)",
            countQuery = "select (*) from (" +
                    "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = (select id from users where username = ?1)" +
                    ")")
    Page<User> findSubscribers(String username, Pageable pageable);


}
