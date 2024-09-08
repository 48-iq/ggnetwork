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
    void removeUserRelations(String username);
}
