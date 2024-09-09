package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.user.UserDto;
import ru.ivanov.ggnetwork.dto.user.UserInfoDto;
import ru.ivanov.ggnetwork.dto.user.UserUpdateDto;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserImageService userImageService;

    public UserDto getUserByUsername(String username) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with " + username + " not found");
        return UserDto.from(userOptional.get());
    }

    public UserInfoDto getUserInfoByUsername(String username) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with " + username + " not found");
        var user = userOptional.get();
        var userInfoDto = new UserInfoDto();
        userInfoDto.setUsername(username);
        userInfoDto.setIcon(user.getIcon());
        userInfoDto.setName(user.getName());
        userInfoDto.setSurname(user.getSurname());
        userInfoDto.setStatus(user.getStatus());
        userInfoDto.setEmail(user.getEmail());
        userInfoDto.setImages(userImageService.getImagesByUser(user.getUsername()));
        return userInfoDto;
    }


    @Transactional
    public UserDto updateUser(String username, UserUpdateDto userUpdateDto) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("user with username " + username + " not found");
        var user = userOptional.get();
        user.setName(userUpdateDto.getName());
        user.setSurname(userUpdateDto.getSurname());
        user.setEmail(userUpdateDto.getEmail());
        user.setStatus(userUpdateDto.getStatus());
        var updatedUser = userRepository.save(user);
        return UserDto.from(updatedUser);
    }

    @Transactional
    public void deleteUser(String username) {
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            userImageService.removeAllImages(username);
            userRepository.removeUserRelations(user.getUsername());
            userRepository.deleteById(user.getId());
        }
    }

    public List<UserDto> findUsersByQuery(String query) {
        return userRepository.findByQuery(query)
                .stream()
                .map(UserDto::from)
                .toList();
    }

    public PageDto<UserDto> findUsersByQuery(String query, Integer page, Integer size) {
        var userPage = userRepository.findByQuery(query, PageRequest.of(page, size));
        var responseData = userPage.get().map(UserDto::from).toList();
        var responseTotal = userPage.getTotalPages();
        var responsePage  = userPage.getNumber();
        var responseSize = userPage.getSize();
        return new PageDto<>(
                responsePage,
                responseSize,
                responseTotal,
                responseData
        );
    }

    public PageDto<UserDto> findUsers(Integer page, Integer size) {
        var userPage = userRepository.findAll(PageRequest.of(page, size));
        var responseData = userPage.get().map(UserDto::from).toList();
        var responseTotal = userPage.getTotalPages();
        var responsePage  = userPage.getNumber();
        var responseSize = userPage.getSize();
        return new PageDto<>(
                responsePage,
                responseSize,
                responseTotal,
                responseData
        );
    }

    public List<UserDto> findUsers() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

}
