package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Game;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByTitle(String title);
    boolean existsByTitle(String title);
}
