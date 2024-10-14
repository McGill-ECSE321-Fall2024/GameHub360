package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.GameRequest.RequestStatus;
import ca.mcgill.ecse321.GameShop.model.GameCategory;


@SpringBootTest
@Transactional
public class GameRequestRepositoryTests {
    @Autowired
    private GameRequestRepository repo;
    @Autowired
    private EmployeeAccountRepository employeeRepo;

    private EmployeeAccount employee;
    private EmployeeAccount employee2;
    @BeforeEach
    public void setUp() {
        employee = new EmployeeAccount("email@example.com", "password123", true);
        employeeRepo.save(employee);
    }
        @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        employeeRepo.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveGameRequest() {
        // Arrange
        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Test Name", "Test Description", "http://image.url", requestDate, employee, category);
        // Act
        repo.save(gameRequest);
        // Assert
        GameRequest retrievedRequest = repo.findGameRequestByGameEntityId(gameRequest.getGameEntityId());
        assertNotNull(retrievedRequest);
        assertEquals("Test Name", retrievedRequest.getName());
        assertEquals("Test Description", retrievedRequest.getDescription());
        assertEquals(requestDate, retrievedRequest.getRequestDate());
        assertEquals(employee.getStaffId(), retrievedRequest.getRequestPlacer().getStaffId());
        assertTrue(retrievedRequest.getCategories().contains(category));
    }

    @Test
    public void testUpdateGameRequest() {
        // Arrange
        employee2 = new EmployeeAccount("email2@example.com", "password1234", true);
        Date requestDate1 = Date.valueOf("2024-10-13");
        Date requestDate2 = Date.valueOf("2024-10-14");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Initial Name", "Initial Description", "http://image.url", requestDate1, employee, category);
        // Act
        repo.save(gameRequest);
        employeeRepo.save(employee2);
        gameRequest.setRequestStatus(RequestStatus.APPROVED);
        gameRequest.setRequestPlacer(employee2);
        gameRequest.setRequestDate(requestDate2);
        repo.save(gameRequest);
        // Assert
        GameRequest updatedRequest = repo.findGameRequestByGameEntityId(gameRequest.getGameEntityId());
        assertNotNull(updatedRequest);
        assertEquals(RequestStatus.APPROVED, updatedRequest.getRequestStatus());
        assertEquals(requestDate2, updatedRequest.getRequestDate());
        assertEquals(employee2.getStaffId(), updatedRequest.getRequestPlacer().getStaffId());
    }
    @Test
    public void testDeleteGameRequest() {
        // Arrange
        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Test Name", "Test Description", "http://image.url", requestDate, employee, category);
        // Act
        repo.save(gameRequest);
        repo.delete(gameRequest);
        employeeRepo.delete(employee);
        // Assert
        GameRequest deletedRequest = repo.findGameRequestByGameEntityId(gameRequest.getGameEntityId());
        assertNull(deletedRequest);
    }
}
