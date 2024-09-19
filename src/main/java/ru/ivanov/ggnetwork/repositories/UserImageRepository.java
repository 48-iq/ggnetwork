package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Image;
import ru.ivanov.ggnetwork.entities.User;

import java.util.List;

@Repository
public interface UserImageRepository extends JpaRepository<User, Integer> {
    @Query(nativeQuery = true,
            value = "select image_id from users_images " +
                    "where user_id = ?1")
    List<Integer> findImagesByUser(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into users_images(user_id, image_id) " +
                    "values(?1, ?2)")
    void addUserImageAssociation(Integer userId, Integer imageId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from users_images where " +
                    "user_id = ?1 " +
                    "and " +
                    "image_id = ?2")
    void removeUserImageAssociation(Integer userId, Integer imageId);

    @Query(nativeQuery = true,
            value = "select * from images where " +
                    "id in (select id from users_images where user_id = ?1)")
    List<Image> getAllUserImages(Integer userId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from users_images where " +
                    "user_id = ?1")
    void removeAllUserImagesAssociations(Integer userId);

    @Query(nativeQuery = true,
            value = "select exist( " +
                    "select 1 from users_images where user_id = ?1 and image_id = ?2 " +
                    ")")
    boolean checkOnBelong(Integer userId, Integer imageId);



}
