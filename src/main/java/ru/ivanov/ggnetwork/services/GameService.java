package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.game.GameCreateDto;
import ru.ivanov.ggnetwork.dto.game.GameDto;
import ru.ivanov.ggnetwork.dto.game.GameUpdateDto;
import ru.ivanov.ggnetwork.entities.Game;
import ru.ivanov.ggnetwork.exceptions.EntityNotFoundException;
import ru.ivanov.ggnetwork.repositories.GameRepository;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ImageService imageService;

    public GameDto findGameByTitle(String title) {
        var gameOptional = gameRepository.findByTitle(title);
        if (gameOptional.isEmpty())
            throw new EntityNotFoundException("game with title " + title + " not found");
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
    public GameDto updateGame(String title, GameUpdateDto gameDto) {
        var gameOptional = gameRepository.findByTitle(title);
        if (gameOptional.isEmpty())
            throw new EntityNotFoundException("game with title " + title + " not found");
        var game = gameOptional.get();
        if (gameDto.getIcon() != null) {
            imageService.update(gameDto.getIcon(), game.getIcon());
        }
        game.setDescription(gameDto.getDescription());
        gameRepository.save(game);
        return GameDto.from(game);
    }

    @Transactional
    public void deleteGame(String title) {
        var gameOptional = gameRepository.findByTitle(title);
        if (gameOptional.isPresent()) {
            var game = gameOptional.get();
            if (game.getIcon() != null)
                imageService.delete(game.getIcon());
            gameRepository.deleteById(game.getId());
        }
    }
}
