package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.GameShop.model.*;
import jakarta.transaction.Transactional;

@SpringBootTest
public class GameRepositoryTests {
    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameCategoryRepository gameCategoryRepo;

    @Autowired
    private CustomerAccountRepository customerAccountRepo;

    @Autowired
    private OrderGameRepository orderGameRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerAccountRepo.deleteAll();
        orderGameRepo.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistAndLoadGame() {
        // Arrange
        GameCategory category = new GameCategory(true, "Action");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);

        Game game = new Game("GameTitle", "Description", "ImageURL", 10, true, 19.99, category);
        game = gameRepo.save(game);

        CustomerAccount customer1 = new CustomerAccount("customer1", "customer1@example.com");
        CustomerAccount customer2 = new CustomerAccount("customer2", "customer2@example.com");
        customerAccountRepo.save(customer1);
        customerAccountRepo.save(customer2);

        game.addWishList(customer1);
        game.addWishList(customer2);

        // Act
        game = gameRepo.save(game);
        int gameId = game.getGameEntityId();

        Game gameFromDb = gameRepo.findGameByGameEntityId(gameId);

        // Assert - Basic Game attributes
        assertNotNull(gameFromDb);
        assertEquals(gameId, gameFromDb.getGameEntityId());
        assertEquals("GameTitle", gameFromDb.getName());
        assertEquals("Description", gameFromDb.getDescription());
        assertEquals("ImageURL", gameFromDb.getImageURL());
        assertEquals(10, gameFromDb.getQuantityInStock());
        assertTrue(gameFromDb.isIsAvailable());
        assertEquals(19.99, gameFromDb.getPrice());

        // Assert - Game Categories
        assertNotNull(gameFromDb.getCategories());
        assertEquals(game.getCategories().size(), gameFromDb.getCategories().size());
        assertEquals(category.getCategoryId(), gameFromDb.getCategories().get(0).getCategoryId());

        // Assert - WishLists
        assertNotNull(gameFromDb.getWishLists());
        assertEquals(game.getWishLists().size(), gameFromDb.getWishLists().size());
        assertTrue(gameFromDb.getWishLists().contains(customer1));
        assertTrue(gameFromDb.getWishLists().contains(customer2));
    }
}
