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
        String email = "email@manager.com";
        String password = "password";
        ManagerAccount manager = new ManagerAccount(email, password);

        // Act
        manager = repo.save(manager);
        ManagerAccount managerFromDb = repo.findManagerAccountByStaffId(manager.getStaffId());

        // Assert
        assertNotNull(managerFromDb);
        assertEquals(manager.getStaffId(), managerFromDb.getStaffId());
        assertEquals(email, managerFromDb.getEmail());
        assertEquals(password, managerFromDb.getPassword());
    }
}
