package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameResponseDto;
import ca.mcgill.ecse321.GameShop.exception.GameException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    /**
     * Create a new game directly
     */
    @Transactional
    public GameResponseDto createGame(GameRequestDto gameRequestDto) {
        // Validate category
        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(gameRequestDto.getCategoryId());
        if (category == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
        }

        // Check if game with same name exists
        if (gameRepository.findGameByName(gameRequestDto.getName()) != null) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
        }

        // Create new game
        Game game = new Game();
        game.setName(gameRequestDto.getName());
        game.setDescription(gameRequestDto.getDescription());
        game.setImageURL(gameRequestDto.getImageUrl());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        game.setPrice(gameRequestDto.getPrice());
        game.setIsAvailable(true);
        game.addCategory(category);

        return new GameResponseDto(gameRepository.save(game));
    }

    /**
     * Update an existing game
     */
    @Transactional
    public GameResponseDto updateGame(Integer gameId, GameRequestDto gameRequestDto) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Update fields if provided
        if (gameRequestDto.getName() != null) {
            Game existingGame = gameRepository.findGameByName(gameRequestDto.getName());
            if (existingGame != null && existingGame.getGameEntityId() != gameId) {
                throw new GameException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
            }
            game.setName(gameRequestDto.getName());
        }
        if (gameRequestDto.getDescription() != null)
            game.setDescription(gameRequestDto.getDescription());
        if (gameRequestDto.getImageUrl() != null)
            game.setImageURL(gameRequestDto.getImageUrl());
        if (gameRequestDto.getQuantityInStock() != null)
            game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        if (gameRequestDto.getPrice() != null)
            game.setPrice(gameRequestDto.getPrice());

        return new GameResponseDto(gameRepository.save(game));
    }

    /**
     * Archive a game
     */
    @Transactional
    public GameResponseDto archiveGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        game.setIsAvailable(false);
        return new GameResponseDto(gameRepository.save(game));
    }

    /**
     * View all archived games
     */
    @Transactional
    public List<GameResponseDto> viewArchivedGames() {
        return gameRepository.findAll().stream()
                .filter(game -> !game.getIsAvailable())
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Reactivate an archived game
     */
    @Transactional
    public GameResponseDto reactivateArchivedGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getIsAvailable()) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Game is already active");
        }

        game.setIsAvailable(true);
        return new GameResponseDto(gameRepository.save(game));
    }

    /**
     * Browse games with optional filters
     */
    @Transactional
    public List<GameResponseDto> browseGames(String category, Double minPrice, Double maxPrice) {
        return gameRepository.findAll().stream()
                .filter(Game::getIsAvailable)
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Search games with filters
     */
    @Transactional
    public List<GameResponseDto> searchGames(String query, String category, Double minPrice, Double maxPrice) {
        return gameRepository.findAll().stream()
                .filter(Game::getIsAvailable)
                .filter(game -> game.getName().toLowerCase().contains(query.toLowerCase()) ||
                        game.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Add game to category
     */
    @Transactional
    public GameResponseDto addGameToCategory(Integer gameId, Integer categoryId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (category == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
        }

        if (game.getCategories().contains(category)) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Game already in this category");
        }

        game.addCategory(category);
        return new GameResponseDto(gameRepository.save(game));
    }

    /**
     * Update game stock
     */
    @Transactional
    public GameResponseDto updateGameStock(Integer gameId, Integer stock) {
        // Find the game first
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Validate stock
        if (stock < 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");
        }

        // Update and save
        try {
            game.setQuantityInStock(stock);
            return new GameResponseDto(gameRepository.save(game));
        } catch (Exception e) {
            throw new GameException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating game stock: " + e.getMessage());
        }
    }

    /**
     * Update game price
     */
    @Transactional
    public GameResponseDto updateGamePrice(Integer gameId, Double price) {
        // Find the game first
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Validate price
        if (price <= 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Price must be positive");
        }

        // Update and save
        try {
            game.setPrice(price);
            return new GameResponseDto(gameRepository.save(game));
        } catch (Exception e) {
            throw new GameException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating game price: " + e.getMessage());
        }
    }
}