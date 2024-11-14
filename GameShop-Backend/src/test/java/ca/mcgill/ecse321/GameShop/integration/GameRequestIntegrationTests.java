package ca.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestResponseDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteResponseDto;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRequestRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;

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

                // Create and save a valid game category
                GameCategory category = new GameCategory(true, "Action Games");

                VALID_CATEGORY_ID = gameCategoryRepository.save(category).getCategoryId();

                // Create and save a valid employee account
                EmployeeAccount employee = new EmployeeAccount("testEmployee", "password123", false);
                VALID_EMPLOYEE_ID = employeeAccountRepository.save(employee).getStaffId();

                // Create and save a valid manager account
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

        /**
         * Test submitting a valid game request.
         */
        @Test
        @Order(1)
        public void testSubmitGameRequest() {
                // Arrange
                GameRequestRequestDto request = new GameRequestRequestDto();
                request.setName(VALID_NAME);
                request.setDescription(VALID_DESCRIPTION);
                request.setImageUrl(VALID_IMAGE_URL);
                request.setRequestDate(Date.valueOf(LocalDate.now()));
                request.setStaffId(VALID_EMPLOYEE_ID);
                request.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                // Act
                ResponseEntity<GameRequestResponseDto> response = client.postForEntity(
                                "/games/request", request, GameRequestResponseDto.class);

                // Assert
                assertNotNull(response, "Response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected HTTP status 200 OK");

                GameRequestResponseDto responseBody = response.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                assertEquals(VALID_NAME, responseBody.getName(), "Game request name mismatch");
                assertEquals(VALID_DESCRIPTION, responseBody.getDescription(), "Game request description mismatch");
                assertEquals(VALID_IMAGE_URL, responseBody.getImageUrl(), "Game request image URL mismatch");
                assertEquals(VALID_EMPLOYEE_ID, responseBody.getStaffId(), "Game request staff ID mismatch");
                assertNotNull(responseBody.getId(), "Game request ID should not be null");
                assertTrue(responseBody.getCategoryIds().contains(VALID_CATEGORY_ID),
                                "Game request should contain the valid category ID");
        }

        /**
         * Test approving a game request as a manager.
         */
        @Test
        @Order(2)
        public void testApproveGameRequestAsManager() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Approve");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game request creation failed");

                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare approval DTO
                GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
                approvalDto.setPrice(VALID_PRICE);
                approvalDto.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

                RequestNoteRequestDto approvalNote = new RequestNoteRequestDto();
                approvalNote.setContent("Approved with note");
                approvalNote.setNoteDate(Date.valueOf(LocalDate.now()));
                approvalNote.setStaffWriterId(VALID_MANAGER_ID);
                approvalNote.setGameRequestId(requestId); // Set the game request ID

                approvalDto.setNote(approvalNote);

                // Act
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<GameRequestApprovalDto> entity = new HttpEntity<>(approvalDto, headers);

                ResponseEntity<GameRequestResponseDto> response = client.exchange(
                                "/games/request/" + requestId + "/approval?approval=true&managerId=" + VALID_MANAGER_ID,
                                HttpMethod.PUT,
                                entity,
                                GameRequestResponseDto.class);

                // Assert
                assertNotNull(response, "Response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Game request approval failed");

                GameRequestResponseDto approvedRequest = response.getBody();
                assertNotNull(approvedRequest, "Approved request body should not be null");
        }

        /**
         * Test approving a game request as an employee should fail.
         */
        @Test
        @Order(3)
        public void testApproveGameRequestAsEmployeeShouldFail() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName("Another Test Game Request");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game request creation failed");

                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare approval DTO
                GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
                approvalDto.setPrice(VALID_PRICE);
                approvalDto.setQuantityInStock(VALID_QUANTITY_IN_STOCK);

                RequestNoteRequestDto approvalNote = new RequestNoteRequestDto();
                approvalNote.setContent("Attempted approval by employee");
                approvalNote.setNoteDate(Date.valueOf(LocalDate.now()));
                approvalNote.setStaffWriterId(VALID_EMPLOYEE_ID);
                approvalNote.setGameRequestId(requestId); // Set the game request ID

                approvalDto.setNote(approvalNote);

                // Act & Assert
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<GameRequestApprovalDto> entity = new HttpEntity<>(approvalDto, headers);

                ResponseEntity<GameRequestResponseDto> response = client.exchange(
                                "/games/request/" + requestId + "/approval?approval=true&managerId="
                                                + VALID_EMPLOYEE_ID,
                                HttpMethod.PUT,
                                entity,
                                GameRequestResponseDto.class);

                assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),
                                "Expected FORBIDDEN status when approving as employee");
        }

        /**
         * Test editing an existing game request.
         */
        @Test
        @Order(4)
        public void testEditGameRequest() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Edit");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertNotNull(createResponse, "Response should not be null");
                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare update request DTO
                GameRequestRequestDto updateRequest = new GameRequestRequestDto();
                updateRequest.setName("Updated Game Request Edit");
                updateRequest.setDescription("Updated Description");
                updateRequest.setImageUrl("http://updatedimage.com/image.jpg");
                updateRequest.setRequestDate(Date.valueOf(LocalDate.now())); // Required field
                updateRequest.setStaffId(VALID_EMPLOYEE_ID); // Required field
                updateRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID)); // Required field

                // Act
                ResponseEntity<GameRequestResponseDto> response = client.postForEntity(
                                "/games/request/" + requestId,
                                updateRequest,
                                GameRequestResponseDto.class);

                // Assert
                assertNotNull(response, "Response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Game request update failed");

                GameRequestResponseDto updatedRequest = response.getBody();
                assertNotNull(updatedRequest, "Updated request body should not be null");
                assertEquals("Updated Game Request Edit", updatedRequest.getName(),
                                "Game request name was not updated correctly");
                assertEquals("Updated Description", updatedRequest.getDescription(),
                                "Game request description was not updated correctly");
                assertEquals("http://updatedimage.com/image.jpg", updatedRequest.getImageUrl(),
                                "Game request image URL was not updated correctly");
        }

        /**
         * Test adding a note to a game request.
         */
        @Test
        @Order(5)
        public void testAddNoteToGameRequest() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Note");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertNotNull(createResponse, "Response should not be null");
                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare note request DTO
                RequestNoteRequestDto noteRequest = new RequestNoteRequestDto();
                noteRequest.setContent("Test note content");
                noteRequest.setNoteDate(Date.valueOf(LocalDate.now()));
                noteRequest.setStaffWriterId(VALID_EMPLOYEE_ID);
                noteRequest.setGameRequestId(requestId); // Set the game request ID

                // Act
                ResponseEntity<RequestNoteResponseDto> response = client.postForEntity(
                                "/games/request/" + requestId + "/note",
                                noteRequest,
                                RequestNoteResponseDto.class);

                // Assert
                assertNotNull(response, "Response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Adding note failed");

                RequestNoteResponseDto noteResponseBody = response.getBody();
                assertNotNull(noteResponseBody, "Note response body should not be null");
                assertEquals("Test note content", noteResponseBody.getContent(), "Note content mismatch");
                assertEquals(VALID_EMPLOYEE_ID, noteResponseBody.getStaffWriterId(), "Note staff writer ID mismatch");
                assertEquals(requestId, noteResponseBody.getGameRequestId(), "Note game request ID mismatch");
        }

        /**
         * Test deleting a note from a game request.
         */
        @Test
        @Order(6)
        public void testDeleteNoteFromGameRequest() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Delete Note");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertNotNull(createResponse, "Response should not be null");
                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare note request DTO
                RequestNoteRequestDto noteRequest = new RequestNoteRequestDto();
                noteRequest.setContent("Test note to delete");
                noteRequest.setNoteDate(Date.valueOf(LocalDate.now()));
                noteRequest.setStaffWriterId(VALID_EMPLOYEE_ID);
                noteRequest.setGameRequestId(requestId); // Set the game request ID

                // Add a note
                ResponseEntity<RequestNoteResponseDto> noteResponse = client.postForEntity(
                                "/games/request/" + requestId + "/note",
                                noteRequest,
                                RequestNoteResponseDto.class);
                assertNotNull(noteResponse, "Note addition response should not be null");
                RequestNoteResponseDto noteResponseBody = noteResponse.getBody();
                assertNotNull(noteResponseBody, "Note response body should not be null");
                Integer noteId = noteResponseBody.getNoteId();

                // Act
                ResponseEntity<Void> response = client.exchange(
                                "/games/request/" + requestId + "/note/" + noteId,
                                HttpMethod.DELETE,
                                null,
                                Void.class);

                // Assert
                assertNotNull(response, "Delete note response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Deleting note failed");
        }

        /**
         * Test rejecting a game request as a manager.
         */
        @Test
        @Order(7)
        public void testRejectGameRequest() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Reject");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertNotNull(createResponse, "Response should not be null");
                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Prepare rejection DTO
                GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
                approvalDto.setRejectionReason("Rejected due to budget constraints.");

                RequestNoteRequestDto rejectionNote = new RequestNoteRequestDto();
                rejectionNote.setContent("Rejected with note");
                rejectionNote.setNoteDate(Date.valueOf(LocalDate.now()));
                rejectionNote.setStaffWriterId(VALID_MANAGER_ID);
                rejectionNote.setGameRequestId(requestId); // Set the game request ID

                approvalDto.setNote(rejectionNote);

                // Act
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<GameRequestApprovalDto> entity = new HttpEntity<>(approvalDto, headers);

                ResponseEntity<GameRequestResponseDto> response = client.exchange(
                                "/games/request/" + requestId + "/approval?approval=false&managerId="
                                                + VALID_MANAGER_ID,
                                HttpMethod.PUT,
                                entity,
                                GameRequestResponseDto.class);

                // Assert
                assertNotNull(response, "Response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Game request rejection failed");

                GameRequestResponseDto rejectedRequest = response.getBody();
                assertNotNull(rejectedRequest, "Rejected request body should not be null");
        }

        /**
         * Test deleting a game request.
         */
        @Test
        @Order(8)
        public void testDeleteGameRequest() {
                // Arrange
                // Create a game request
                GameRequestRequestDto createRequest = new GameRequestRequestDto();
                createRequest.setName(VALID_NAME + " Delete");
                createRequest.setDescription(VALID_DESCRIPTION);
                createRequest.setImageUrl(VALID_IMAGE_URL);
                createRequest.setRequestDate(Date.valueOf(LocalDate.now()));
                createRequest.setStaffId(VALID_EMPLOYEE_ID);
                createRequest.setCategoryIds(Arrays.asList(VALID_CATEGORY_ID));

                ResponseEntity<GameRequestResponseDto> createResponse = client.postForEntity(
                                "/games/request", createRequest, GameRequestResponseDto.class);
                assertNotNull(createResponse, "Response should not be null");
                GameRequestResponseDto responseBody = createResponse.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                Integer requestId = responseBody.getId();

                // Act
                ResponseEntity<Void> response = client.exchange(
                                "/games/request/" + requestId,
                                HttpMethod.DELETE,
                                null,
                                Void.class);

                // Assert
                assertNotNull(response, "Delete game request response should not be null");
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Deleting game request failed");
        }
}