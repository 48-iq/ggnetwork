package ru.ivanov.ggnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ivanov.ggnetwork.entities.MessageNotification;

@Repository
public interface MessageNotificationRepository extends JpaRepository<MessageNotification, Integer> {
}
