package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query(nativeQuery = true,
    value = "select chats.* from chats as c left join chats_users as cu " +
            "on cu.chat_id = c.id " +
            "where u.id = ?1")
    List<Chat> findChatsByUser(Integer userId);

    @Query(nativeQuery = true,
    value = "select chats.* from chats as c left join chats_users as cu " +
            "on cu.chat_id = c.id " +
            "where u.id = ?1",
    countQuery = "select count(*) from select (" +
            "select 1 from chats as c left join chats_users as cu " +
            "on cu.chat_id = c.id " +
            "where u.id = ?1" +
            ") ")
    Page<Chat> findChatsByUser(Integer userId, Pageable pageable);

    @Query(nativeQuery = true,
    value = "delete from chats_users where chat_id = ?1")
    void removeChatAssociations(Integer chatId);
}
