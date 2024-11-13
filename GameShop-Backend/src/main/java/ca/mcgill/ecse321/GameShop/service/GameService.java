package ca.mcgill.ecse321.GameShop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    /**
     * Create a new game directly.
     * 
     * @param gameRequestDto the game request data transfer object containing game
     *                       details
     * @return the created game as a Game
     * @throws GameShopException if a game with the same name already exists
     */
    @Transactional
    public Game createGame(GameRequestDto gameRequestDto) {
        if (gameRepository.findGameByName(gameRequestDto.getName()) != null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
        }

        Game game = new Game();
        game.setName(gameRequestDto.getName());
        game.setDescription(gameRequestDto.getDescription());
        game.setImageURL(gameRequestDto.getImageURL());
        game.setIsAvailable(true);
        game.setPrice(gameRequestDto.getPrice());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        gameRequestDto.getCategoryIds().stream()
                .map(categoryId -> gameCategoryRepository.findGameCategoryByCategoryId(categoryId))
                .forEach(game::addCategory);

        return gameRepository.save(game);
    }

    /**
     * Update an existing game.
     * 
     * @param gameId the ID of the game to update
     * @param game   the game data transfer object containing updated game details
     * @return the updated game as a Game
     * @throws GameShopException if game is not found or if updated name conflicts
     *                           with existing game
     */
    @Transactional
    public Game updateGame(Integer gameId, Game game) {
        Game existingGame = gameRepository.findGameByGameEntityId(gameId);
        if (existingGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        // Update fields if provided
        if (game.getName() != null) {
            Game existingGameByName = gameRepository.findGameByName(game.getName());
            if (existingGameByName != null && existingGameByName.getGameEntityId() != gameId) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Game with this name already exists");
            }
            existingGame.setName(game.getName());
        }
        if (game.getDescription() != null) {
            existingGame.setDescription(game.getDescription());
        }
        if (game.getImageURL() != null) {
            existingGame.setImageURL(game.getImageURL());
        }
        existingGame.setIsAvailable(game.getIsAvailable());
        if (game.getPrice() >= 0) {
            existingGame.setPrice(game.getPrice());
        }
        if (game.getQuantityInStock() >= 0) {
            existingGame.setQuantityInStock(game.getQuantityInStock());
        }

        return gameRepository.save(existingGame);
    }

    /**
     * Archive a game.
     * 
     * @param gameId the ID of the game to archive
     * @return the archived game as a Game
     * @throws GameShopException if game is not found
     */
    @Transactional
    public Game archiveGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        game.setIsAvailable(false);
        return gameRepository.save(game);
    }

    /**
     * View all archived games.
     * 
     * @return a list of archived games as Game objects
     */
    @Transactional
    public List<Game> viewArchivedGames() {
        List<Game> games = (List<Game>) gameRepository.findAll();

        return games.stream()
                .filter(game -> !game.getIsAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Reactivate an archived game.
     * 
     * @param gameId the ID of the game to reactivate
     * @return the reactivated game as a Game
     * @throws GameShopException if game is not found or if game is already active
     */
    @Transactional
    public Game reactivateArchivedGame(Integer gameId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (game.getIsAvailable()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game is already active");
        }

        game.setIsAvailable(true);
        return gameRepository.save(game);
    }

    /**
     * Browse games based on optional filters: category, minimum price, and maximum
     * price.
     * 
     * @param category the category to filter games by (optional)
     * @param minPrice the minimum price to filter games by (optional)
     * @param maxPrice the maximum price to filter games by (optional)
     * @return a list of Game objects that match the provided filters
     */
    @Transactional
    public List<Game> browseGames(String category, Double minPrice, Double maxPrice) {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return games.stream()
                .filter(Game::getIsAvailable)
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Search for games based on a query and optional filters: category, minimum
     * price, and maximum price.
     * 
     * @param query    the search query to filter games by name or description
     *                 (required)
     * @param category the category to filter games by (optional)
     * @param minPrice the minimum price to filter games by (optional)
     * @param maxPrice the maximum price to filter games by (optional)
     * @return a list of Game objects that match the provided search query and
     *         filters
     */
    @Transactional
    public List<Game> searchGames(String query, String category, Double minPrice, Double maxPrice) {
        List<Game> games = (List<Game>) gameRepository.findAll();
        return games.stream()
                .filter(Game::getIsAvailable)
                .filter(game -> game.getName().toLowerCase().contains(query.toLowerCase()) ||
                        game.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().toLowerCase().contains(query.toLowerCase())))
                .filter(game -> category == null ||
                        game.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category)))
                .filter(game -> minPrice == null || game.getPrice() >= minPrice)
                .filter(game -> maxPrice == null || game.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Add a game to a category.
     * 
     * @param gameId     the ID of the game to add to the category
     * @param categoryId the ID of the category to add the game to
     * @return the updated game as a Game
     * @throws GameShopException if game or category is not found, or if game is
     *                           already in category
     */
    @Transactional
    public Game addGameToCategory(Integer gameId, Integer categoryId) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Category not found");
        }

        if (game.getCategories().contains(category)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game already in this category");
        }

        game.addCategory(category);
        return gameRepository.save(game);
    }

    /**
     * Update the stock of a game.
     * 
     * @param gameId the ID of the game to update
     * @param stock  the new stock quantity
     * @return the updated game as a Game
     * @throws GameShopException if game is not found or if stock is negative
     */
    @Transactional
    public Game updateGameStock(Integer gameId, Integer stock) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (stock < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Stock cannot be negative");
        }

        game.setQuantityInStock(stock);
        return gameRepository.save(game);
    }

    /**
     * Update the price of a game.
     * 
     * @param gameId the ID of the game to update
     * @param price  the new price
     * @return the updated game as a Game
     * @throws GameShopException if game is not found or if price is not positive
     */
    @Transactional
    public Game updateGamePrice(Integer gameId, Double price) {
        Game game = gameRepository.findGameByGameEntityId(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (price <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Price must be positive");
        }

        game.setPrice(price);
        return gameRepository.save(game);
    }
}