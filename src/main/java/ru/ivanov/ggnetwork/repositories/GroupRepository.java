package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Group;
import ru.ivanov.ggnetwork.entities.User;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    boolean existsByTitle(String title);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "title like '%' || ?1 || '%'")
    List<Group> findGroupsByQuery(String query);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "title like '%' || ?1 || '%'",
            countQuery = "select count(*) (select * from groups where " +
                    "title like '%' || ?1 || '%' " +
                    ")")
    Page<Group> findGroupsByQuery(String query, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from groups as g " +
                    "join users_groups as ug on g.id = ug.group_id " +
                    "where ug.user_id = ?1")
    List<Group> findGroupsByUser(Integer id);

    @Query(nativeQuery = true,
            value = "select * from groups as g " +
                    "join users_groups as ug on g.id = ug.group_id " +
                    "where ug.user_id = ?1",
            countQuery = "select count(*) from ( " +
                    "select * from groups as g " +
                    "join users_groups as ug on g.id = ug.group_id " +
                    "where ug.user_id = ?1 " +
                    ")")
    Page<Group> findGroupsByUser(Integer id, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from users as u " +
                    "join users_groups as ug on u.id = ug.user_id " +
                    "where ug.group_id = ?1")
    List<User> findUsersByGroup(Integer id);

    @Query(nativeQuery = true,
            value = "select * from users as u " +
                    "join users_groups as ug on u.id = ug.user_id " +
                    "where ug.group_id = ?1",
            countQuery = "select count(*) from ( " +
                    "select * from users as u " +
                    "join users_groups as ug on u.id = ug.user_id " +
                    "where ug.group_id = ?1 " +
                    ")")
    Page<User> findUsersByGroup(Integer id, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into users_groups(user_id, group_id) " +
                    "values ( " +
                    "?1, " +
                    "?2 " +
                    ") on conflict(user_id, group_id) do nothing")
    void subscribe(Integer userId, Integer groupId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from users_groups " +
                    "where group_id = ?2 " +
                    "and user_id = ?1 ")
    void unsubscribe(Integer userId, Integer groupId);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "owner_id = ?1 ")
    List<Group> findGroupsByOwner(Integer userId);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "owner_id = ?1",
            countQuery = "select count(*) from ( " +
                    "select * from groups where " +
                    "owner_id = ?1 " +
                    ")")
    Page<Group> findGroupsByOwner(Integer userId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from users_groups where " +
                    "group_id = ?1")
    void removeUsersAssociations(Integer groupId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from viewed_group_posts where " +
                    "post_id in (select id from posts where creator_id = ?1); " +
                    "delete from group_post_grades where " +
                    "post_id in (select id from posts where creator_id = ?1); " +
                    "delete from group_posts where creator_id = ?1")
    void removePostsAssociations(Integer groupId);


}
