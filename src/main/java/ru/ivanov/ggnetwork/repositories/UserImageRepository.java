package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Image;
import ru.ivanov.ggnetwork.entities.UserImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    @Query("select i from UserImage i join i.user u where u.username = :username")
    List<UserImage> findImagesByUsername(String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into user_images(user_id, image) " +
                    "values((select id from users where username = ?1), ?2)")
    void addImageToUser(String username, String image);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_images where " +
                    "user_id = (select id from users where username = ?1) " +
                    "and " +
                    "image = ?2")
    void removeImageFromUser(String username, String image);

    @Query(nativeQuery = true,
            value = "select image from user_images where " +
                    "user_id = (select id from users where username = ?1)")
    List<String> getAllUserImages(String username);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from user_images where " +
                    "user_id = (select id from users where username = ?1)")
    void removeAllUserImages(String username);

    Optional<UserImage> findByImage(String image);


}
