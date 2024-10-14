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
        GameCategory category = new GameCategory(true, "Adventure");

        // Define variables to avoid hardcoding
        String name = "Adventure";
        boolean isAvailable = true;

        // Act
        category = repo.save(category);

        // Assert
        assertNotNull(repo.findById(category.getCategoryId()));
        assertEquals(name, category.getName());
        assertEquals(isAvailable, category.getIsAvailable());
    }
}
