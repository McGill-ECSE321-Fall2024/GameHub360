package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.GameCategory;

@SpringBootTest
public class GameRequestRepositoryTests {
    @Autowired
    private GameRequestRepository repo;
    @Autowired
    private EmployeeAccountRepository employeeRepo;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndRetrieveGameRequest() {
        // Arrange
        EmployeeAccount employee = new EmployeeAccount("email@example.com", "password123", true);
        employeeRepo.save(employee);

        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");

        GameRequest gameRequest = new GameRequest("Test Name", "Test Description", "http://image.url", requestDate,
                employee, category);
    
        // Act
        repo.save(gameRequest);

        GameRequest retrievedRequest = repo.findGameRequestByGameEntityId(gameRequest.getGameEntityId());

        // Assert
        assertNotNull(retrievedRequest);
        assertEquals(gameRequest.getGameEntityId(), retrievedRequest.getGameEntityId());

        assertEquals("Test Name", retrievedRequest.getName());
        assertEquals("Test Description", retrievedRequest.getDescription());
        assertEquals("http://image.url", retrievedRequest.getImageURL());
        assertEquals(requestDate, retrievedRequest.getRequestDate());

        assertNotNull(retrievedRequest.getRequestPlacer());
        assertEquals(employee.getStaffId(), retrievedRequest.getRequestPlacer().getStaffId());
        assertNotNull(retrievedRequest.getCategories());
        assertTrue(retrievedRequest.getCategories().contains(category));
    }
}
