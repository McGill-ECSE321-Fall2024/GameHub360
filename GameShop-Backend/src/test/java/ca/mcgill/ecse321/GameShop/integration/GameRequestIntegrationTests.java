package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.repository.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;

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

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    private static Integer VALID_CATEGORY_ID;
    private static Integer VALID_EMPLOYEE_ID;
    private static Integer VALID_MANAGER_ID;
    private static final String VALID_NAME = "Test Game Request";
    private static final String VALID_DESCRIPTION = "A test game request description";
    private static final String VALID_IMAGE_URL = "http://example.com/game.jpg";
    private static final Double VALID_PRICE = 19.99;
    private static final Integer VALID_QUANTITY_IN_STOCK = 10;

    @BeforeAll
    public void setUp() {
        gameRequestRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        employeeAccountRepository.deleteAll();
        managerAccountRepository.deleteAll();

        GameCategory category = new GameCategory(true, "Action Games");
        category.setCategoryType(GameCategory.CategoryType.GENRE);
        VALID_CATEGORY_ID = gameCategoryRepository.save(category).getCategoryId();

        EmployeeAccount employee = new EmployeeAccount("testEmployee", "password123", false);
        VALID_EMPLOYEE_ID = employeeAccountRepository.save(employee).getStaffId();

        ManagerAccount manager = new ManagerAccount("testManager", "password123");
        VALID_MANAGER_ID = managerAccountRepository.save(manager).getStaffId();
    }

    @AfterAll
    public void cleanUp() {
        gameRequestRepository.deleteAll();
        gameCategoryRepository.deleteAll();
        employeeAccountRepository.deleteAll();
        managerAccountRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSubmitGameRequest() {
        // Arrange
        GameRequestDto request = new GameRequestDto();
        request.setName(VALID_NAME);
        request.setDescription(VALID_DESCRIPTION);
        request.setImageUrl(VALID_IMAGE_URL);
        request.setPrice(VALID_PRICE);
        request.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        request.setStaffId(VALID_EMPLOYEE_ID);
        request.setCategoryId(VALID_CATEGORY_ID);

        // Act
        ResponseEntity<GameRequestDto> response = client.postForEntity(
                "/games/request", request, GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameRequestDto responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertEquals(VALID_NAME, responseBody.getName());
        assertEquals(VALID_EMPLOYEE_ID, responseBody.getStaffId());
        assertEquals(GameRequest.RequestStatus.SUBMITTED, responseBody.getRequestStatus());
    }

    @Test
    @Order(2)
    public void testApproveGameRequestAsManager() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game request creation failed");

        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setPrice(VALID_PRICE);
        approvalDto.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

        RequestNoteDto approvalNote = new RequestNoteDto();
        approvalNote.setContent("Approved with note");
        approvalNote.setStaffWriterId(VALID_MANAGER_ID);
        approvalDto.setNote(approvalNote);

        // Act
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GameRequestApprovalDto> entity = new HttpEntity<>(approvalDto, headers);

        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=true&managerId=" + VALID_MANAGER_ID,
                HttpMethod.PUT,
                entity,
                GameRequestDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Game request approval failed");
        GameRequestDto approvedRequest = response.getBody();
        assertNotNull(approvedRequest);
        assertEquals("APPROVED", approvedRequest.getRequestStatus().toString());
    }

    @Test
    @Order(3)
    public void testApproveGameRequestAsEmployeeShouldFail() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName("Another Test Game Request");
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game request creation failed");

        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setPrice(VALID_PRICE);
        approvalDto.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

        RequestNoteDto approvalNote = new RequestNoteDto();
        approvalNote.setContent("Attempted approval by employee");
        approvalNote.setStaffWriterId(VALID_EMPLOYEE_ID);
        approvalDto.setNote(approvalNote);

        // Act & Assert
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GameRequestApprovalDto> entity = new HttpEntity<>(approvalDto, headers);

        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=true&employeeId=" + VALID_EMPLOYEE_ID,
                HttpMethod.PUT,
                entity,
                GameRequestDto.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Expected BAD_REQUEST status");
    }

    @Test
    @Order(4)
    public void testEditGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Game Request");

        // Act
        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId,
                HttpMethod.POST,
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
    @Order(5)
    public void testAddNoteToGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setPrice(VALID_PRICE);
        approvalDto.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

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
        RequestNoteDto noteResponseBody = response.getBody();
        assertNotNull(noteResponseBody);
        assertEquals("Test note content", noteResponseBody.getContent());
        assertEquals(VALID_EMPLOYEE_ID, noteResponseBody.getStaffWriterId());
    }

    @Test
    @Order(6)
    public void testDeleteNoteFromGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

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
    @Order(7)
    public void testRejectGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);
        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

        RequestNoteDto rejectionNote = new RequestNoteDto();
        rejectionNote.setContent("Rejected with note");
        rejectionNote.setStaffWriterId(VALID_MANAGER_ID);

        // Act
        ResponseEntity<GameRequestDto> response = client.exchange(
                "/games/request/" + requestId + "/approval?approval=false&managerId=" + VALID_MANAGER_ID,
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
    @Order(8)
    public void testDeleteGameRequest() {
        // Arrange
        GameRequestDto createRequest = new GameRequestDto();
        createRequest.setName(VALID_NAME);
        createRequest.setDescription(VALID_DESCRIPTION);
        createRequest.setImageUrl(VALID_IMAGE_URL);
        createRequest.setPrice(VALID_PRICE);
        createRequest.setQuantityInStock(VALID_QUANTITY_IN_STOCK);
        createRequest.setStaffId(VALID_EMPLOYEE_ID);
        createRequest.setCategoryId(VALID_CATEGORY_ID);

        ResponseEntity<GameRequestDto> createResponse = client.postForEntity(
                "/games/request", createRequest, GameRequestDto.class);
        assertNotNull(createResponse, "Response should not be null");
        GameRequestDto responseBody = createResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        Integer requestId = responseBody.getId();

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