package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Game;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByTitle(String title);
    boolean existsByTitle(String title);

    @Query("select g from User u join u.gamesUserHasPlayed g where u.username = :username")
    List<Game> findGamesUserHasPlayed(String username);
    @Query("select g from User u join u.gamesUserPlays g where u.username = :username")
    List<Game> findGamesUserPlays(String username);

    @Query("select g from User u join u.gamesUserHasPlayed g where u.username = :username")
    Page<Game> findGamesUserHasPlayed(String username, Pageable pageable);
    @Query("select g from User u join u.gamesUserPlays g where u.username = :username")
    Page<Game> findGamesUserPlays(String username, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from games_user_plays " +
                    "where game_id = (select id from games where title = ?1);" +
                    "delete from games_user_has_played " +
                    "where game_id = (select id from games where title = ?1)"
    )
    void removeGameRelations(String title);

    @Query(nativeQuery = true,
            value = "select * from games where " +
                    "title like '%' || ?1 || '%'")
    List<Game> findGamesByQuery(String query);

    @Query(nativeQuery = true,
            value = "select * from games where" +
                    "title like '%' || ?1 || '%'",
            countQuery = "select count(*) from (" +
                    "select * from games where " +
                    "title like '%' || ?1 '%'" +
                    ")")
    Page<Game> findGamesByQuery(String query, Pageable pageable);
}
