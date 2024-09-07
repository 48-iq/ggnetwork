package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
                    "where (users.username) like ('%' || ?1 || '%')")
    Page<User> findByQuery(String query, Pageable pageable);


}
