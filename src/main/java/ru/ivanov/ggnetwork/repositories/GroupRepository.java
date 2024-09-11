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
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByTitle(String title);
    boolean existsByTitle(String title);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "title like '%' || ?1 || '%'")
    List<Group> findGroupsByQuery(String query);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "title like '%' || ?1 || '%'",
            countQuery = "select count(*) (select * from groups where " +
                    "title like '%' || ?1 || '%'" +
                    ")")
    Page<Group> findGroupsByQuery(String query, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from groups as g " +
                    "join groups_users as gu on g.id = gu.group_id " +
                    "where gu.user_id = (select id from users where username = ?1)")
    List<Group> findGroupsByUser(String username);

    @Query(nativeQuery = true,
            value = "select * from groups as g " +
                    "join groups_users as gu on g.id = gu.group_id " +
                    "where gu.user_id = (select id from users where username = ?1)",
            countQuery = "select count(*) from (" +
                    "select * from groups as g " +
                    "join groups_users as gu on g.id = gu.group_id " +
                    "where gu.user_id = (select id from users where username = ?1)" +
                    ")")
    Page<Group> findGroupsByUser(String username, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from users as u " +
                    "join groups_users as gu on u.id = gu.user_id " +
                    "where gu.group_id = (select id from groups where title = ?1)")
    List<User> findUsersByGroup(String title);

    @Query(nativeQuery = true,
            value = "select * from users as u " +
                    "join groups_users as gu on u.id = gu.user_id " +
                    "where gu.group_id = (select id from groups where title = ?1)",
            countQuery = "select count(*) from (" +
                    "select * from users as u " +
                    "join groups_users as gu on u.id = gu.user_id " +
                    "where gu.group_id = (select id from groups where title = ?1)" +
                    ")")
    Page<User> findUsersByGroup(String title, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into groups_users(group_id, user_id)" +
                    "values (" +
                    "(select id from groups where title = ?1), " +
                    "(select id from users where username = ?2)" +
                    ")")
    void subscribe(String title, String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from groups_users" +
                    "where group_id = (select id from groups where title = ?1)" +
                    "and user_id = (select id from users where username = ?2)")
    void unsubscribe(String title, String username);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "owner_id = (select id from users where username = ?1)")
    List<Group> findGroupsByOwner(String username);

    @Query(nativeQuery = true,
            value = "select * from groups where " +
                    "owner_id = (select id from users where username = ?1)",
            countQuery = "select count(*) from (" +
                    "select * from groups where " +
                    "owner_id = (select id from users where username = ?1)" +
                    ")")
    Page<Group> findGroupsByOwner(String username, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from groups_users where " +
                    "group_id = (select id from groups where title = ?1)")
    void removeGroupsAssociations(String title);

}
