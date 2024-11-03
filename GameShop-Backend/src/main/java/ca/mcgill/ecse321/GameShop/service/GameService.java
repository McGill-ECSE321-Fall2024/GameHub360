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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    /**
     * Submits a new game request.
     *
     * @param gameRequestDto The request object containing game details.
     * @return A response containing the submitted game's details.
     */
    @Transactional
    public GameResponseDto submitGameRequest(GameRequestDto gameRequestDto) {
        Game game = new Game();
        game.setName(gameRequestDto.getName());
        game.setPrice(gameRequestDto.getPrice());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        game.setIsAvailable(false);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Approves or rejects a game request.
     *
     * @param requestId The ID of the game request.
     * @param approval  Boolean indicating approval or rejection.
     * @return A response containing the updated game request details.
     */
    @Transactional
    public GameResponseDto approveOrRejectGameRequest(Integer requestId, boolean approval) {
        Game game = gameRepository.findById(requestId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game request not found."));
        game.setIsAvailable(approval);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Creates a new game directly.
     *
     * @param gameRequestDto The request object containing game details.
     * @return A response containing the newly created game's details.
     */
    @Transactional
    public GameResponseDto createGame(GameRequestDto gameRequestDto) {
        // Check if a game with the same title already exists
        Game existingGame = gameRepository.findGameByName(gameRequestDto.getName());
        if (existingGame != null) {
            throw new GameException(HttpStatus.CONFLICT, "Game with this title already exists.");
        }

        Game game = new Game();
        game.setName(gameRequestDto.getName());
        game.setPrice(gameRequestDto.getPrice());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        game.setIsAvailable(true);

        // Save the game
        game = gameRepository.save(game);

        return new GameResponseDto(game);
    }

    /**
     * Updates an existing game.
     *
     * @param gameId         The ID of the game to update.
     * @param gameRequestDto The request object containing updated game details.
     * @return A response containing the updated game's details.
     */
    @Transactional
    public GameResponseDto updateGame(Integer gameId, GameRequestDto gameRequestDto) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        game.setName(gameRequestDto.getName());
        game.setPrice(gameRequestDto.getPrice());
        game.setQuantityInStock(gameRequestDto.getQuantityInStock());
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Archives a game.
     *
     * @param gameId The ID of the game to archive.
     * @return A response containing the archived game's details.
     */
    @Transactional
    public GameResponseDto archiveGame(Integer gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        game.setIsAvailable(false);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Views all archived games.
     *
     * @return A list of archived games.
     */
    @Transactional
    public List<GameResponseDto> viewArchivedGames() {
        return gameRepository.findAll().stream()
                .filter(game -> !game.getIsAvailable())
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Reactivates an archived game.
     *
     * @param gameId The ID of the game to reactivate.
     * @return A response containing the reactivated game's details.
     */
    @Transactional
    public GameResponseDto reactivateArchivedGame(Integer gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Archived game not found."));
        game.setIsAvailable(true);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Browses all available games.
     *
     * @return A list of available games.
     */
    @Transactional
    public List<GameResponseDto> browseGames() {
        return gameRepository.findAll().stream()
                .filter(Game::getIsAvailable)
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Searches for games by a query.
     *
     * @param query The search query.
     * @return A list of games matching the search query.
     */
    @Transactional
    public List<GameResponseDto> searchGames(String query) {
        return gameRepository.findAll().stream()
                .filter(game -> game.getName().toLowerCase().contains(query.toLowerCase()))
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Adds a game to a category.
     *
     * @param gameId     The ID of the game.
     * @param categoryId The ID of the category.
     * @return A response containing the updated game's details.
     */
    @Transactional
    public GameResponseDto addGameToCategory(Integer gameId, Integer categoryId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        GameCategory category = gameCategoryRepository.findById(categoryId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Category not found."));

        // Add the game to the category and vice versa
        if (!category.getGames().contains(game)) {
            category.addGame(game);
            game.getCategories().add(category);
        }

        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Updates the stock of a game.
     *
     * @param gameId The ID of the game.
     * @param stock  The new stock quantity.
     * @return A response containing the updated game's details.
     */
    @Transactional
    public GameResponseDto updateGameStock(Integer gameId, int stock) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        game.setQuantityInStock(stock);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    /**
     * Updates the price of a game.
     *
     * @param gameId The ID of the game.
     * @param price  The new price.
     * @return A response containing the updated game's details.
     */
    @Transactional
    public GameResponseDto updateGamePrice(Integer gameId, double price) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        game.setPrice(price);
        game = gameRepository.save(game);
        return new GameResponseDto(game);
    }

    public void deleteGame(int gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            gameRepository.delete(game.get());
        } else {
            throw new GameException(HttpStatus.NOT_FOUND, "Game not found.");
        }
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param gameId The ID of the game.
     * @return A response containing the game's details.
     */
    @Transactional
    public GameResponseDto getGameById(int gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(
                () -> new GameException(HttpStatus.NOT_FOUND, "Game not found."));
        return new GameResponseDto(game);
    }

    /**
     * Retrieves all games.
     *
     * @return A list of all games.
     */
    @Transactional
    public List<GameResponseDto> findAllGames() {
        return gameRepository.findAll().stream()
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }
}