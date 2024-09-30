package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(nativeQuery = true,
    value = "select * from messages " +
            "where chat_id = ?1")
    List<Message> findMessagesByChat(Integer id);

    @Query(nativeQuery = true,
    value = "select * from messages " +
            "where chat_id = ?1",
    countQuery = "select count(*) from select (" +
            "select 1 from messages " +
            "where chat_id = ?1" +
            ")")
    Page<Message> findMessagesByChat(Integer id, Pageable pageable);


}
