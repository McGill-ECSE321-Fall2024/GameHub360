package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        // Arrange
        GameCategory category = new GameCategory(true, "Action");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);

        Game game = new Game("GameTitle", "Description", "ImageURL", 10, true, 19.99, category);

        // Act
        gameRepo.save(game);
        int gameId = game.getGameEntityId();

        Game gameFromDb = gameRepo.findById(gameId).orElse(null);
        GameCategory categoryFromDb = gameCategoryRepo.findById(category.getCategoryId()).orElse(null);

        // Assert
        assertNotNull(categoryFromDb);
        assertTrue(categoryFromDb.getGames().contains(gameFromDb));
        assertNotNull(gameFromDb);
        assertEquals("GameTitle", gameFromDb.getName());
        assertEquals("Description", gameFromDb.getDescription());
        assertEquals("ImageURL", gameFromDb.getImageURL());
        assertEquals(10, gameFromDb.getQuantityInStock());
        assertEquals(true, gameFromDb.isIsAvailable());
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
