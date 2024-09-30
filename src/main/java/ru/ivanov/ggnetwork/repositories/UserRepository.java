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
                    "values(?1, ?2) " +
                    "on conflict(user_id, game_id) " +
                    "do nothing"
    )
    void addGameToGamesUserHasPlayed(Integer userId, Integer gameId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into games_user_plays(user_id, game_id) " +
                    "values(?1, ?2) " +
                    "on conflict(user_id, game_id) " +
                    "do nothing"
    )
    void addGameToGamesUserPlays(Integer user_id, Integer gameId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_plays " +
                    "where user_id = ?1 " +
                    "and " +
                    "game_id = ?2"
    )
    void removeGameFromGamesUserPlays(Integer userId, Integer game_id);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_has_played " +
                    "where user_id = ?1 " +
                    "and " +
                    "game_id = ?2"
    )
    void removeGameFromGamesUserHasPlayed(Integer userId, Integer game_id);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_plays " +
                    "where user_id = ?1; " +
                    "delete from games_user_has_played " +
                    "where user_id = ?1"
    )
    void removeUserGamesAssociations(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from users_groups " +
                    "where user_id = ?1; " +
                    "delete from users_groups " +
                    "where group_id in (select id from groups " +
                    "where owner_id = ?1); " +
                    "delete from viewed_group_posts where " +
                    "post_id in (select gp.id from group_posts as gp join groups as g on gp.creator_id = g.id " +
                    "where g.id in (select id from groups where creator_id = ?1)); " +
                    "delete from group_post_grades where " +
                    "post_id in (select gp.id from group_posts as gp join groups as g on gp.creator_id = g.id " +
                    "where g.id in (select id from groups where creator_id = ?1)); " +
                    "delete from groups " +
                    "where id in (select id from groups " +
                    "where owner_id = ?1)")
    void removeUserGroupsAndUsersGroupsAssociations(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from subscriptions " +
                    "where subscribed_user_id = ?1 " +
                    "or " +
                    "user_id = ?1")
    void removeUsersUsersAssociations(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from viewed_user_posts " +
                    "where user_id = ?1; " +
                    "delete from viewed_user_posts where " +
                    "post_id in (select id from user_posts where creator_id = ?1); " +
                    "delete from user_post_grades where " +
                    "user_id = ?1; " +
                    "delete from user_post_grades where " +
                    "post_id in (select id from user_posts where creator_id = ?1); " +
                    "delete from user_posts where " +
                    "creator_id = ?1; " +
                    "delete from viewed_group_posts " +
                    "where user_id = ?1; " +
                    "delete from group_post_grades where " +
                    "user_id = ?1 "
                    )
    void removeUsersPostsAssociations(Integer userId);


    @Query(nativeQuery = true,
    value = "remove from chats_users where user_id = ?1")
    void removeUsersChatsAssociations(Integer userId);


    @Query(nativeQuery = true,
        value = "select * from subscriptions as s1 " +
                "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                "join users as u on s1.user_id = u.id " +
                "where s1.subscribed_user_id = ?1 " +
                "and s2.user_id = ?1")
    List<User> findFriends(Integer userId);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s1 " +
                    "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                    "join users as u on s1.user_id = u.id " +
                    "where s1.subscribed_user_id = ?1 " +
                    "and s2.user_id = ?1",
            countQuery = "select count(*) from ( " +
                    "select * from subscriptions as s1 " +
                    "join subscriptions as s2 on s1.user_id = s2.subscribed_user_id " +
                    "join users as u on s1.user_id = u.id " +
                    "where s1.subscribed_user_id = ?1 " +
                    "and s2.user_id = ?1 " +
                    ")"
    )
    Page<User> findFriends(Integer userId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into subscriptions(subscribed_user_id, user_id) " +
                    "values (?1, ?2) on conflict(subscribed_user_id, user_id) do nothing")
    void subscribe(Integer subscribedUserId, Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from subscriptions where " +
                    "subscribed_user_id = ?1 " +
                    "and " +
                    "user_id = ?2")
    void unsubscribe(Integer subscribedUserId, Integer userId);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = ?1")
    List<User> findSubscriptions(Integer userId);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = ?1",
            countQuery = "select count(*) from ( " +
                    "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.user_id = u.id " +
                    "where s.subscribed_user_id = ?1" +
                    ")")
    Page<User> findSubscriptions(Integer userId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select exists(select 1 from subscriptions where " +
                    "subscribed_user_id = ?1 " +
                    "and " +
                    "user_id = ?2 " +
                    ")")
    boolean isSubscriber(Integer subscribedUserId, Integer userId);

    @Query(nativeQuery = true,
            value =  "select " +
                    "exists(select 1 from subscriptions where " +
                    "subscribed_user_id = ?1 and " +
                    "user_id = ?2) " +
                    "and " +
                    "exists(select 1 from subscriptions where " +
                    "subscribed_user_id = ?2 and " +
                    "user_id = ?1)")
    boolean isFriends(Integer firstUserId, Integer secondUserId);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = ?1")
    List<User> findSubscribers(Integer userId);

    @Query(nativeQuery = true,
            value = "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = ?1",
            countQuery = "select (*) from (" +
                    "select * from subscriptions as s " +
                    "join users as u " +
                    "on s.subscribed_user_id = u.id " +
                    "where s.user_id = ?1" +
                    ")")
    Page<User> findSubscribers(Integer userId, Pageable pageable);

    @Query("select u from User u where u.id in :usersIds")
    List<User> findByIds(List<Integer> usersIds);




}
