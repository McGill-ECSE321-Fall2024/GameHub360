package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.repository.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameRequestIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    private static Integer VALID_CATEGORY_ID;
    private static Integer VALID_EMPLOYEE_ID;
    private static final String VALID_NAME = "Test Game Request";
    private static final Double VALID_PRICE = 59.99;
    private static final int VALID_STOCK = 50;
    private static final String VALID_DESCRIPTION = "A test game request description";
    private static final String VALID_IMAGE_URL = "http://example.com/game.jpg";

    @BeforeAll
    public void setUp() {
        // Clear repositories
        gameRequestRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        employeeAccountRepository.deleteAll();

        // Create test category
        GameCategory category = new GameCategory(true, "Action Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        VALID_CATEGORY_ID = gameCategoryRepository.save(category).getCategoryId();

        // Create test employee
        EmployeeAccount employee = new EmployeeAccount("testEmployee", "password123", true);
        VALID_EMPLOYEE_ID = employeeAccountRepository.save(employee).getStaffId();
    }

    @AfterAll
    public void cleanUp() {
        gameRequestRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        employeeAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSubmitGameRequest() {
        // Arrange
        GameRequestDto request = new GameRequestDto();
        request.setName(VALID_NAME);
        request.setPrice(VALID_PRICE);
        request.setQuantityInStock(VALID_STOCK);
        request.setDescription(VALID_DESCRIPTION);
        request.setImageUrl(VALID_IMAGE_URL);
        request.setCategoryId(VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameRequestDto> response = client.postForEntity(
                "/requests", request, GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(GameRequest.RequestStatus.SUBMITTED, responseBody.getRequestStatus());
    }

    @Test
    @Order(2)
    public void testEditGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game Request");

        // Act
        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId,
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDto updatedRequest = response.getBody();
        assertNotNull(updatedRequest);
        assertEquals("Updated Game Request", updatedRequest.getName());
    }

    @Test
    @Order(3)
    public void testAddNoteToGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        RequestNoteDto noteRequest = new RequestNoteDto();
        noteRequest.setContent("Test note content");
        noteRequest.setStaffWriterId(VALID_EMPLOYEE_ID);

        // Act
        ResponseEntity<RequestNoteDto> response = client.postForEntity(
                "/games/request/" + requestId + "/note",
                noteRequest,
                RequestNoteDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RequestNoteDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test note content", responseBody.getContent());
        assertEquals(VALID_EMPLOYEE_ID, responseBody.getStaffWriterId());
    }

    @Test
    @Order(4)
    public void testDeleteNoteFromGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        RequestNoteDto noteRequest = new RequestNoteDto();
        noteRequest.setContent("Test note to delete");
        noteRequest.setStaffWriterId(VALID_EMPLOYEE_ID);

        ResponseEntity<RequestNoteDto> noteResponse = client.postForEntity(
                "/games/request/" + requestId + "/note",
                noteRequest,
                RequestNoteDto.class);
        assertNotNull(noteResponse);
        RequestNoteDto noteResponseBody = noteResponse.getBody();
        assertNotNull(noteResponseBody, "Response body should not be null");
        Integer noteId = noteResponseBody.getId();

        // Act
        ResponseEntity<Void> response = client.exchange(
                "/games/request/" + requestId + "/note/" + noteId,
                HttpMethod.DELETE,
                null,
                Void.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testApproveGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        RequestNoteDto approvalNote = new RequestNoteDto();
        approvalNote.setContent("Approved with note");
        approvalNote.setStaffWriterId(VALID_EMPLOYEE_ID);

        // Act
        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=true",
                HttpMethod.PUT,
                new HttpEntity<>(approvalNote),
                GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDto approvedRequest = response.getBody();
        assertNotNull(approvedRequest);
        assertEquals("APPROVED", approvedRequest.getRequestStatus().toString());
    }

    @Test
    @Order(6)
    public void testRejectGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        RequestNoteDto rejectionNote = new RequestNoteDto();
        rejectionNote.setContent("Rejected with note");
        rejectionNote.setStaffWriterId(VALID_EMPLOYEE_ID);

        // Act
        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=false",
                HttpMethod.PUT,
                new HttpEntity<>(rejectionNote),
                GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDto rejectedRequest = response.getBody();
        assertNotNull(rejectedRequest);
        assertEquals("REFUSED", rejectedRequest.getRequestStatus().toString());
    }

    @Test
    @Order(7)
    public void testDeleteGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto(VALID_NAME, VALID_PRICE, VALID_STOCK,
                VALID_DESCRIPTION, VALID_IMAGE_URL, VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        GameRequestDto createdRequest = createResponse.getBody();
        assertNotNull(createdRequest, "Created request should not be null");
        Integer requestId = createdRequest.getId();

        // Act
        ResponseEntity<Void> response = client.exchange(
                "/games/request/" + requestId,
                HttpMethod.DELETE,
                null,
                Void.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}