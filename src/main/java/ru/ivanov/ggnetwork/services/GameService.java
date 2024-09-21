package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.PageDto;
import ru.ivanov.ggnetwork.dto.game.GameCreateDto;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.dto.game.GameUpdateDto;
import ru.ivanov.ggnetwork.entities.Game;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GameRepository;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ImageService imageService;

    public GameDto findGameById(Integer gameId) {
        var gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty())
            throw new EntityNotFoundException("game with id " + gameId + " not found");
        var game = gameOptional.get();
        return GameDto.from(game);
    }

    @Transactional
    public GameDto createGame(GameCreateDto gameDto) {
        var game = Game.builder()
                .title(gameDto.getTitle())
                .description(gameDto.getDescription())
                .build();
        if (gameDto.getIcon() != null) {
            game.setIcon(imageService.save(gameDto.getIcon()));
        }
        var savedGame = gameRepository.save(game);
        return GameDto.from(savedGame);
    }

    @Transactional
    public GameDto updateGame(Integer gameId, GameUpdateDto gameDto) {
        var gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isEmpty())
            throw new EntityNotFoundException("game with title " + gameId + " not found");
        var game = gameOptional.get();
        if (gameDto.getIcon() != null) {
            imageService.update(gameDto.getIcon(), game.getIcon());
        }
        game.setTitle(gameDto.getTitle());
        game.setDescription(gameDto.getDescription());
        gameRepository.save(game);
        return GameDto.from(game);
    }

    @Transactional
    public void deleteGame(Integer gameId) {
        var gameOptional = gameRepository.findById(gameId);
        if (gameOptional.isPresent()) {
            var game = gameOptional.get();
            if (game.getIcon() != null)
                imageService.delete(game.getIcon());
            gameRepository.removeGameRelations(game.getId());
            gameRepository.deleteById(game.getId());
        }
    }


    public List<GameDto> findGamesByQuery(String query) {
        return gameRepository.findGamesByQuery(query)
                .stream().map(GameDto::from).toList();
    }


    public List<GameDto> findAllGames() {
        return gameRepository.findAll()
                .stream().map(GameDto::from).toList();
    }

    private PageDto<GameDto> pageFrom(Page<Game> gamesPage) {
        var pageDto = new PageDto<GameDto>();
        pageDto.setData(gamesPage.get().map(GameDto::from).toList());
        pageDto.setPage(gamesPage.getNumber());
        pageDto.setSize(gamesPage.getSize());
        pageDto.setTotal(gamesPage.getTotalPages());
        return pageDto;
    }

    public PageDto<GameDto> findGamesByQuery(String query, Integer page, Integer size) {
        var gamesPage = gameRepository.findGamesByQuery(query, PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }

    public PageDto<GameDto> findAllGames(Integer page, Integer size) {
        var gamesPage = gameRepository.findAll(PageRequest.of(page, size));
        return pageFrom(gamesPage);
    }


}
