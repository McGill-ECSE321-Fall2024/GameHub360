package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
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
     * Create a new game directly.
     * 
     * @param gameRequestDto the game request data transfer object containing game details
     * @return the created game as a GameDto
     */
    @Transactional
    public GameDto createGame(GameRequestDto gameRequestDto) {
        // Check if a game with the same name exists
        if (gameRepository.findGameByName(gameRequestDto.getName()) != null) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
        }

        // Create new game
        Game game = new Game();
        game.setName(gameRequestDto.getName());
        game.setDescription(gameRequestDto.getDescription());
        game.setImageURL(gameRequestDto.getImageUrl());
        game.setIsAvailable(true);
        game.setPrice(gameRequestDto.getPrice());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());

        return new GameDto(gameRepository.save(game));
    }

    /**
     * Update an existing game.
     * 
     * @param gameId the ID of the game to update
     * @param gameDto the game data transfer object containing updated game details
     * @return the updated game as a GameDto
     */
    @Transactional
    public GameDto updateGame(Integer gameId, GameDto gameDto) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Update fields if provided
        if (gameDto.getName() != null) {
            Game existingGame = gameRepository.findGameByName(gameDto.getName());
            if (existingGame != null && existingGame.getGameEntityId() != gameId) {
                throw new GameException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
            }
            game.setName(gameDto.getName());
        }
        if (gameDto.getDescription() != null) {
            game.setDescription(gameDto.getDescription());
        }
        if (gameDto.getImageUrl() != null) {
            game.setImageURL(gameDto.getImageUrl());
        }
        game.setIsAvailable(gameDto.getIsAvailable());
        game.setPrice(gameDto.getPrice());
        game.setQuantityInStock(gameDto.getQuantityInStock());

        return new GameDto(gameRepository.save(game));
    }

    /**
     * Archive a game.
     * 
     * @param gameId the ID of the game to archive
     * @return the archived game as a GameDto
     */
    @Transactional
    public GameDto archiveGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        game.setIsAvailable(false);
        return new GameDto(gameRepository.save(game));
    }

    /**
     * View all archived games.
     * 
     * @return a list of archived games as GameDto objects
     */
    @Transactional
    public List<GameDto> viewArchivedGames() {
        return gameRepository.findAll().stream()
                .filter(game -> !game.getIsAvailable())
                .map(GameDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Reactivate an archived game.
     * 
     * @param gameId the ID of the game to reactivate
     * @return the reactivated game as a GameDto
     */
    @Transactional
    public GameDto reactivateArchivedGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getIsAvailable()) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Game is already active");
        }

        game.setIsAvailable(true);
        return new GameDto(gameRepository.save(game));
    }

    /**
     * Browse games based on optional filters: category, minimum price, and maximum price.
     * 
     * @param category the category to filter games by (optional)
     * @param minPrice the minimum price to filter games by (optional)
     * @param maxPrice the maximum price to filter games by (optional)
     * @return a list of GameDto objects that match the provided filters
     */
    @Transactional
    public List<GameDto> browseGames(String category, Double minPrice, Double maxPrice) {
        List<Game> games = gameRepository.findAll().stream()
                .filter(Game::getIsAvailable)
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        return games.stream().map(GameDto::new).collect(Collectors.toList());
    }

    /**
     * Search for games based on a query and optional filters: category, minimum price, and maximum price.
     * 
     * @param query the search query to filter games by name or description (required)
     * @param category the category to filter games by (optional)
     * @param minPrice the minimum price to filter games by (optional)
     * @param maxPrice the maximum price to filter games by (optional)
     * @return a list of GameDto objects that match the provided search query and filters
     */
    @Transactional
    public List<GameDto> searchGames(String query, String category, Double minPrice, Double maxPrice) {
        return gameRepository.findAll().stream()
                .filter(Game::getIsAvailable)
                .filter(game -> game.getName().toLowerCase().contains(query.toLowerCase()) ||
                        game.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .map(GameDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Add a game to a category.
     * 
     * @param gameId the ID of the game to add to the category
     * @param categoryId the ID of the category to add the game to
     * @return the updated game as a GameDto
     */
    @Transactional
    public GameDto addGameToCategory(Integer gameId, Integer categoryId) {
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
        return new GameDto(gameRepository.save(game));
    }

    /**
     * Update the stock of a game.
     * 
     * @param gameId the ID of the game to update
     * @param stock the new stock quantity
     * @return the updated game as a GameDto
     */
    @Transactional
    public GameDto updateGameStock(Integer gameId, Integer stock) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (stock < 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");
        }

        game.setQuantityInStock(stock);
        return new GameDto(gameRepository.save(game));
    }

    /**
     * Update the price of a game.
     * 
     * @param gameId the ID of the game to update
     * @param price the new price
     * @return the updated game as a GameDto
     */
    @Transactional
    public GameDto updateGamePrice(Integer gameId, Double price) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (price <= 0) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Price must be positive");
        }

        game.setPrice(price);
        return new GameDto(gameRepository.save(game));
    }
}