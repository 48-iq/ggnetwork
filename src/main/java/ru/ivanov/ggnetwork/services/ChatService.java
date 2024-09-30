package ru.ivanov.ggnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.chat.ChatDto;
import ru.ivanov.ggnetwork.dto.chat.ChatInfoDto;
import ru.ivanov.ggnetwork.dto.chat.GroupChatCreateDto;
import ru.ivanov.ggnetwork.entities.Chat;
import ru.ivanov.ggnetwork.entities.ChatType;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.ChatRepository;
import ru.ivanov.ggnetwork.repositories.ImageRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;

    public ChatInfoDto createPersonalChat(Integer userId1, Integer userId2) {
        var user1 = userRepository.findById(userId1).orElseThrow(
                () -> new EntityNotFoundException("user with id " + userId1 + " not found"));
        var user2 = userRepository.findById(userId2).orElseThrow(
                () -> new EntityNotFoundException("user with id " + userId2 + " not found"));
        var chat = Chat.builder()
                .chatType(ChatType.PERSONAL_CHAT)
                .users(List.of(user1, user2))
                .build();
        var savedChat = chatRepository.save(chat);
        return ChatInfoDto.from(savedChat);
    }

    public ChatInfoDto createGroupChat(GroupChatCreateDto groupChatCreateDto) {
        var users = userRepository.findByIds(groupChatCreateDto.getUsers());
        if (users.size() < groupChatCreateDto.getUsers().size())
            throw new EntityNotFoundException("users with ids " +
                    groupChatCreateDto.getUsers() + " not found");
        var icon = imageService.save(groupChatCreateDto.getIcon());
        var chat = Chat.builder()
                .title(groupChatCreateDto.getTitle())
                .users(users)
                .icon(icon)
                .build();
        var savedChat = chatRepository.save(chat);
        return ChatInfoDto.from(savedChat);
    }

    public ChatInfoDto createPostChat() {
        var chat = Chat.builder()
                .build();
        var savedChat = chatRepository.save(chat);
        return ChatInfoDto.from(savedChat);
    }

    public List<ChatDto> findChatsByUser(Integer userId) {
        return chatRepository.findChatsByUser(userId)
                .stream().map(ChatDto::from).toList();
    }

    private PageDto<ChatDto> pageDtoFrom(Page<Chat> chats) {
        var pageDto = new PageDto<ChatDto>();
        pageDto.setPage(chats.getNumber());
        pageDto.setSize(chats.getSize());
        pageDto.setTotal(chats.getTotalPages());
        pageDto.setData(chats.get().map(ChatDto::from).toList());
        return pageDto;
    }

    public PageDto<ChatDto> findChatsByUser(Integer userId, Integer page, Integer size) {
        return pageDtoFrom(
                chatRepository.findChatsByUser(userId, PageRequest.of(page, size))
        );
    }

    public void deleteChat(Integer chatId) {
        var chat = chatRepository.findById(chatId)
                .orElse(null);
        if (chat != null) {
            imageService.delete(chat.getIcon());
            chatRepository.removeChatAssociations(chat.getId());
            chatRepository.deleteById(chat.getId());
        }
    }
}
