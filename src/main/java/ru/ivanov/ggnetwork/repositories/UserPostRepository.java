package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.entities.UserPost;

import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Integer> {

    @Query(nativeQuery = true,
            value = "select * from user_posts where " +
                    "where creator_id = ?1 " +
                    "order by time")
    List<UserPost> findPostsByUser(Integer userId);


    @Query(nativeQuery = true,
            value = "select * from user_posts where " +
                    "where creator_id = ?1 " +
                    "order by time",
            countQuery = "select count(*) from ( " +
                    "select * from user_posts where " +
                    "where creator_id = ?1 " +
                    "order by time " +
                    ")")
    Page<UserPost> findPostsByUser(Integer userId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_posts where " +
                    "creator_id = ?1")
    void removePostsByUser(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_post_grades where " +
                    "post_id = ?1")
    void removePostAssociations(Integer postId);



}
