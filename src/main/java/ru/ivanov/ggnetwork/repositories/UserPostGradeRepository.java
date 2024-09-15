package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.UserPostGrade;

import java.util.Optional;

@Repository
public interface UserPostGradeRepository extends JpaRepository<UserPostGrade, Integer> {
    @Query(nativeQuery = true,
            value = "select * from user_post_grades where user_id = ?1 " +
                    "and post_id = ?2")
    Optional<UserPostGrade> findGrade(Integer userId, Integer postId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into user_post_grades(user_id, post_id, type)" +
                    "values (?1, ?2, 'LIKE') on conflict(user_id, post_id) update")
    void like(Integer userId, Integer postId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into user_post_grades(user_id, post_id, type)" +
                    "values (?1, ?2, 'DISLIKE') on conflict(user_id, post_id) update")
    void dislike(Integer userId, Integer postId);



}
