package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.GameCategory;

@SpringBootTest
public class GameCategoryRepositoryTests {
    @Autowired
    private GameCategoryRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveGameCategory() {
        // Arrange
        String name = "Adventure";
        boolean isAvailable = true;
        GameCategory category = new GameCategory(isAvailable, name);

        // Act
        category = repo.save(category);
        GameCategory categoryFromDb = repo.findGameCategoryByCategoryId(category.getCategoryId());

        // Assert
        assertNotNull(categoryFromDb);
        assertEquals(name, categoryFromDb.getName());
        assertEquals(isAvailable, categoryFromDb.getIsAvailable());
    }
}
