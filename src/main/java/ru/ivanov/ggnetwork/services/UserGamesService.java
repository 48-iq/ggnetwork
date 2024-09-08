package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.entities.Game;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GameRepository;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.util.List;

@Service
public class UserGamesService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    private void checkOnExists(String username, String title) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username " + username + " not found");
        if (!gameRepository.existsByTitle(title))
            throw new EntityNotFoundException("game with title " + title + " not found");
    }

    @Transactional
    public void addGameToUserHasPlayed(String username, String title) {
        checkOnExists(username, title);
        userRepository.addGameToGamesUserHasPlayed(username, title);
    }

    @Transactional
    public void addGameToUserPlays(String username, String title) {
        checkOnExists(username, title);
        userRepository.addGameToGamesUserPlays(username, title);
    }

    @Transactional
    public void removeGameFromUserHasPlayed(String username, String title) {
        checkOnExists(username, title);
        userRepository.removeGameFromGamesUserHasPlayed(username, title);
    }

    @Transactional
    public void removeGameFromUserPlays(String username, String title) {
        checkOnExists(username, title);
        userRepository.removeGameFromGamesUserPlays(username, title);
    }

    public List<GameDto> getGamesUserPlays(String username) {
        return gameRepository.findGamesUserPlays(username)
                .stream().map(GameDto::from)
                .toList();
    }

    public List<GameDto> getGamesUserHasPlayed(String username) {
        return gameRepository.findGamesUserHasPlayed(username)
                .stream().map(GameDto::from)
                .toList();
    }

    private PageDto<GameDto> pageFrom(Page<Game> gamesPage) {
        var pageDto = new PageDto<GameDto>();
        pageDto.setData(gamesPage.get().map(GameDto::from).toList());
        pageDto.setPage(gamesPage.getNumber());
        pageDto.setSize(gamesPage.getSize());
        pageDto.setTotal(gamesPage.getTotalPages());
        return pageDto;
    }

    public PageDto<GameDto> getGamesUserPlays(String username, Integer page, Integer size) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username " + username + "not found");
        var gamesPage = gameRepository.findGamesUserPlays(username, PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }

    public PageDto<GameDto> getGamesUserHasPlayed(String username, Integer page, Integer size) {
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("user with username " + username + "not found");
        var gamesPage = gameRepository.findGamesUserHasPlayed(username, PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }

}
