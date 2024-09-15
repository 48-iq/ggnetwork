package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.GroupPost;
import ru.ivanov.ggnetwork.entities.UserPost;

import java.util.List;

@Repository
public interface GroupPostRepository extends JpaRepository<GroupPost, Integer> {
    @Query(nativeQuery = true,
            value = "select * from group_posts where " +
                    "creator_id = ?1 " +
                    "order by time")
    List<GroupPost> findPostsByGroup(Integer groupId);


    @Query(nativeQuery = true,
            value = "select * from user_posts where " +
                    "creator_id = ?1 " +
                    "order by time",
            countQuery = "select count(*) from ( " +
                    "select * from user_posts where " +
                    "creator_id = ?1 " +
                    "order by time " +
                    ")")
    Page<UserPost> findPostsByUser(Integer userId, Pageable pageable);


    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_post_grades where " +
                    "post_id = ?1; " +
                    "delete from viewed_user_posts where post_id = ?1;")
    void removePostAssociations(Integer postId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into viewed_user_posts(user_id, post_id) " +
                    "values (?1, ?2) on conflict(user_id, post_id) do nothing")
    void viewPost(Integer userId, Integer postId);
}
