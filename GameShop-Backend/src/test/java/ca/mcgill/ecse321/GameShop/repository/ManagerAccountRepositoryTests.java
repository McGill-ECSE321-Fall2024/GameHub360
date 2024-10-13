package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.model.ManagerAccount;

@SpringBootTest
public class ManagerAccountRepositoryTests {
    @Autowired
    private ManagerAccountRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveManagerAccount() {
        // Arrange
        ManagerAccount manager = new ManagerAccount("email@manager.com", "password");

        // Act
        manager = repo.save(manager);

        // Assert
        assertNotNull(repo.findManagerAccountByStaffId(manager.getStaffId()));
        assertEquals("email@manager.com", manager.getEmail());
    }
}
