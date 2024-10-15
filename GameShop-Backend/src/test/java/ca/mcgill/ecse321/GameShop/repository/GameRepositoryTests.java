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

import java.sql.Date;
import java.util.List;

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

    @Autowired
    private CustomerOrderRepository customerOrderRepo;

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
        // Arrange - Set up a GameCategory
        GameCategory category = new GameCategory(true, "Action");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        category = gameCategoryRepo.save(category);

        // Set up the Game
        Game game = new Game("GameTitle", "Description", "ImageURL", 10, true, 19.99, category);
        game = gameRepo.save(game);

        // Set up CustomerAccount and associate with Game (wishList)
        CustomerAccount customer1 = new CustomerAccount("customer1", "customer1@example.com");
        CustomerAccount customer2 = new CustomerAccount("customer2", "customer2@example.com");
        customerAccountRepo.save(customer1);
        customerAccountRepo.save(customer2);

        game.addWishList(customer1);
        game.addWishList(customer2);
        game = gameRepo.save(game);

        // Create a CustomerOrder
        CustomerOrder order = new CustomerOrder(new Date(System.currentTimeMillis()), null, null);
        // Save the order to persist it to the database
        order = customerOrderRepo.save(order); // Assuming customerO

        // Set up OrderGame and associate with Game
        OrderGame order1 = new OrderGame(order, game);
        OrderGame order2 = new OrderGame(new CustomerOrder(), game);
        orderGameRepo.save(order1);
        orderGameRepo.save(order2);

        // Act - Retrieve the Game from the database
        int gameId = game.getGameEntityId();
        Game gameFromDb = gameRepo.findById(gameId).orElse(null);

        // Assert - Basic Game attributes
        assertNotNull(gameFromDb);
        assertEquals("GameTitle", gameFromDb.getName());
        assertEquals("Description", gameFromDb.getDescription());
        assertEquals("ImageURL", gameFromDb.getImageURL());
        assertEquals(10, gameFromDb.getQuantityInStock());
        assertTrue(gameFromDb.isIsAvailable());
        assertEquals(19.99, gameFromDb.getPrice());

        // Assert - Game Categories
        assertEquals(game.getCategories().size(), gameFromDb.getCategories().size());
            GameCategory expectedCategory = game.getCategories().get(0);
            GameCategory actualCategory = gameFromDb.getCategories().get(0);
            assertEquals(expectedCategory.getName(), actualCategory.getName());
            assertEquals(expectedCategory.getCategoryType(), actualCategory.getCategoryType());
            assertEquals(expectedCategory.isIsAvailable(), actualCategory.isIsAvailable());
        // Assert - WishLists (Many-to-Many with CustomerAccount)
        List<CustomerAccount> wishListFromDb = gameFromDb.getWishLists();
        assertEquals(2, wishListFromDb.size());
        assertTrue(wishListFromDb.contains(customer1));
        assertTrue(wishListFromDb.contains(customer2));

        // Assert - Orders (One-to-Many with OrderGame)
        List<OrderGame> ordersFromDb = gameFromDb.getOrders();
        assertEquals(2, ordersFromDb.size());
        assertTrue(ordersFromDb.contains(order1));
        assertTrue(ordersFromDb.contains(order2));
    }
}
