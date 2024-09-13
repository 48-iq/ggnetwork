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

    private void checkOnExists(Integer userId, Integer gameId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + " not found");
        if (!gameRepository.existsById(gameId))
            throw new EntityNotFoundException("game with id " + gameId + " not found");
    }

    @Transactional
    public void addGameToUserHasPlayed(Integer userId, Integer gameId) {
        checkOnExists(userId, gameId);
        userRepository.addGameToGamesUserHasPlayed(userId, gameId);
    }

    @Transactional
    public void addGameToUserPlays(Integer userId, Integer gameId) {
        checkOnExists(userId, gameId);
        userRepository.addGameToGamesUserPlays(userId, gameId);
    }

    @Transactional
    public void removeGameFromUserHasPlayed(Integer userId, Integer gameId) {
        checkOnExists(userId, gameId);
        userRepository.removeGameFromGamesUserHasPlayed(userId, gameId);
    }

    @Transactional
    public void removeGameFromUserPlays(Integer userId, Integer gameId) {
        checkOnExists(userId, gameId);
        userRepository.removeGameFromGamesUserPlays(userId, gameId);
    }

    public List<GameDto> getGamesUserPlays(Integer userId) {
        return gameRepository.findGamesUserPlays(userId)
                .stream().map(GameDto::from)
                .toList();
    }

    public List<GameDto> getGamesUserHasPlayed(Integer userId) {
        return gameRepository.findGamesUserHasPlayed(userId)
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

    public PageDto<GameDto> getGamesUserPlays(Integer userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + "not found");
        var gamesPage = gameRepository.findGamesUserPlays(userId, PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }

    public PageDto<GameDto> getGamesUserHasPlayed(Integer userId, Integer page, Integer size) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("user with id " + userId + "not found");
        var gamesPage = gameRepository.findGamesUserHasPlayed(userId, PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }

}
