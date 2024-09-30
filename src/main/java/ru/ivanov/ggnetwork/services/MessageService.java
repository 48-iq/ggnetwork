package ru.ivanov.ggnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.message.MessageCreateDto;
import ru.ivanov.ggnetwork.dto.message.MessageDto;
import ru.ivanov.ggnetwork.entities.Chat;
import ru.ivanov.ggnetwork.entities.Message;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.ChatRepository;
import ru.ivanov.ggnetwork.repositories.MessageRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(MessageCreateDto messageCreateDto, Integer creatorId, Integer chatId) {
        var chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with id " + chatId + " not found"));
        var creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + creatorId + " not found"));
        var message = Message.builder()
                .chat(chat)
                .creator(creator)
                .time(LocalDateTime.now())
                .content(messageCreateDto.getContent())
                .build();
        messageRepository.save(message);
    }

    public List<MessageDto> getMessagesByChat(Integer chatId) {
        return messageRepository.findMessagesByChat(chatId)
                .stream().map(MessageDto::from)
                .toList();
    }

    private PageDto<MessageDto> pageDtoFrom(Page<Message> messages) {
        var pageDto = new PageDto<MessageDto>();
        pageDto.setPage(messages.getNumber());
        pageDto.setSize(messages.getSize());
        pageDto.setTotal(messages.getTotalPages());
        pageDto.setData(messages.get().map(MessageDto::from).toList());
        return pageDto;
    }

    public PageDto<MessageDto> getMessagesByChat(Integer chatId, Integer page, Integer size) {
        return pageDtoFrom(
                messageRepository.findMessagesByChat(chatId, PageRequest.of(page, size))
        );
    }


}
