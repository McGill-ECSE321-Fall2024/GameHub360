package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void testCreateAndRetrieveGameCategory() {
        // Arrange
        GameCategory category = new GameCategory(true, "Adventure");

        // Act
        category = repo.save(category);

        // Assert
        assertNotNull(repo.findById(category.getCategoryId()));
        assertEquals("Adventure", category.getName());
    }

    @Test
    @Transactional
    public void testUpdateGameCategory() {
        // Arrange
        GameCategory category = new GameCategory(true, "Adventure");
        category = repo.save(category);
        
        // Act
        category.setName("Action");
        category = repo.save(category);

        // Assert
        assertEquals("Action", repo.findById(category.getCategoryId()).get().getName());
    }
}
