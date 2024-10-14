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
        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Test Name", "Test Description", "http://image.url", requestDate, employee, category);
        repo.save(gameRequest);

        GameRequest retrievedRequest = repo.findById(gameRequest.getGameEntityId()).orElse(null);
        assertNotNull(retrievedRequest);
        assertEquals("Test Name", retrievedRequest.getName());
        assertEquals("Test Description", retrievedRequest.getDescription());
        assertEquals(requestDate, retrievedRequest.getRequestDate());
        assertEquals(employee.getStaffId(), retrievedRequest.getRequestPlacer().getStaffId());
        assertTrue(retrievedRequest.getCategories().contains(category));
    }

    @Test
    public void testUpdateGameRequest() {
        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Initial Name", "Initial Description", "http://image.url", requestDate, employee, category);
        repo.save(gameRequest);

        gameRequest.setRequestStatus(RequestStatus.APPROVED);
        repo.save(gameRequest);

        GameRequest updatedRequest = repo.findById(gameRequest.getGameEntityId()).orElse(null);
        assertNotNull(updatedRequest);
        assertEquals(RequestStatus.APPROVED, updatedRequest.getRequestStatus());
    }
    @Test
    public void testDeleteGameRequest() {
        Date requestDate = Date.valueOf("2024-10-13");
        GameCategory category = new GameCategory(true, "Action");
        GameRequest gameRequest = new GameRequest("Test Name", "Test Description", "http://image.url", requestDate, employee, category);
        repo.save(gameRequest);

        repo.delete(gameRequest);

        GameRequest deletedRequest = repo.findById(gameRequest.getGameEntityId()).orElse(null);
        assertNull(deletedRequest);
    }

    @Test
    public void testFindAllGameRequests() {
        Date requestDate1 = Date.valueOf("2024-10-13");
        GameCategory category1 = new GameCategory(true, "Action");
        GameRequest gameRequest1 = new GameRequest("First Request", "First Description", "http://image1.url", requestDate1, employee, category1);
        repo.save(gameRequest1);

        Date requestDate2 = Date.valueOf("2024-10-14");
        GameCategory category2 = new GameCategory(true, "Comedy");
        GameRequest gameRequest2 = new GameRequest("Second Request", "Second Description", "http://image2.url", requestDate2, employee, category2);
        repo.save(gameRequest2);

        List<GameRequest> requests = new ArrayList<>();
        repo.findAll().forEach(requests::add);
        assertEquals(2, requests.size());
    }
}
