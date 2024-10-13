package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import jakarta.transaction.Transactional;

@SpringBootTest
public class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistAndLoadGame() {
        GameCategory category = new GameCategory(true, "Action");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);

        // Create a new game
        Game game = new Game("GameTitle", "Description", "ImageURL", 10, true, 19.99, category);

        // Save the game
        gameRepo.save(game);
        int gameId = game.getGameEntityId();

        // Read the game from the database
        Game gameFromDb = gameRepo.findById(gameId).orElse(null);

        // Assertions
        assertNotNull(gameFromDb);
        assertEquals("GameTitle", gameFromDb.getName());
        assertEquals(game.getCategories().size(), gameFromDb.getCategories().size());
        for (int i = 0; i < game.getCategories().size(); i++) {
            GameCategory expectedCategory = game.getCategories().get(i);
            GameCategory actualCategory = gameFromDb.getCategories().get(i);
            assertEquals(expectedCategory.getName(), actualCategory.getName());
            assertEquals(expectedCategory.getCategoryType(), actualCategory.getCategoryType());
            assertEquals(expectedCategory.isIsAvailable(), actualCategory.isIsAvailable());
        }
        assertEquals(19.99, gameFromDb.getPrice());
    }
}
