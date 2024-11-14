package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.model.RequestNote;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRequestRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.RequestNoteRepository;

@SpringBootTest
public class GameRequestServiceTests {

    @Mock
    private GameRequestRepository gameRequestRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCategoryRepository gameCategoryRepository;

    @Mock
    private EmployeeAccountRepository employeeAccountRepository;

    @Mock
    private RequestNoteRepository requestNoteRepository;

    @Mock
    private ManagerAccountRepository managerAccountRepository;

    @InjectMocks
    private GameRequestService gameRequestService;

    @Test
    public void testCreateGameRequest_Success() {
        // Arrange
        GameRequestRequestDto requestDto = new GameRequestRequestDto(
                "Test Game",
                "Test Description",
                "http://testurl.com/image.jpg",
                Date.valueOf(LocalDate.now()),
                1,
                Arrays.asList(1, 2));

        EmployeeAccount employee = new EmployeeAccount("employee@example.com", "password", true);

        GameCategory category1 = new GameCategory(true, "Category 1");
        GameCategory category2 = new GameCategory(true, "Category 2");

        ReflectionTestUtils.setField(category1, "categoryId", 1);
        ReflectionTestUtils.setField(category2, "categoryId", 2);

        when(employeeAccountRepository.findEmployeeAccountByStaffId(requestDto.getStaffId())).thenReturn(employee);
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(category1);
        when(gameCategoryRepository.findGameCategoryByCategoryId(2)).thenReturn(category2);
        when(gameRequestRepository.save(any(GameRequest.class))).thenAnswer(invocation -> {
            GameRequest savedRequest = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedRequest, "gameEntityId", 1); // Simulate database assigning an ID
            return savedRequest;
        });

        // Act
        GameRequest createdRequest = gameRequestService.createGameRequest(requestDto);

        // Assert
        assertNotNull(createdRequest);
        assertEquals("Test Game", createdRequest.getName());
        assertEquals("Test Description", createdRequest.getDescription());
        assertEquals("http://testurl.com/image.jpg", createdRequest.getImageURL());
        assertEquals(employee, createdRequest.getRequestPlacer());
        assertEquals(2, createdRequest.getCategories().size());
        verify(gameRequestRepository, times(1)).save(any(GameRequest.class));
    }

    @Test
    public void testCreateGameRequest_EmployeeNotFound() {
        // Arrange
        GameRequestRequestDto requestDto = new GameRequestRequestDto(
                "Test Game",
                "Test Description",
                "http://testurl.com/image.jpg",
                Date.valueOf(LocalDate.now()),
                1,
                Arrays.asList(1, 2));

        when(employeeAccountRepository.findEmployeeAccountByStaffId(requestDto.getStaffId())).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.createGameRequest(requestDto);
        });

        // Assert
        assertEquals("Employee not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testCreateGameRequest_CategoryNotFound() {
        // Arrange
        GameRequestRequestDto requestDto = new GameRequestRequestDto(
                "Test Game",
                "Test Description",
                "http://testurl.com/image.jpg",
                Date.valueOf(LocalDate.now()),
                1,
                Arrays.asList(1, 2));

        EmployeeAccount employee = new EmployeeAccount("employee@example.com", "password", true);

        GameCategory category1 = new GameCategory(true, "Category 1");
        GameCategory category2 = new GameCategory(true, "Category 2");

        ReflectionTestUtils.setField(category1, "categoryId", 1);
        ReflectionTestUtils.setField(category2, "categoryId", 2);

        when(employeeAccountRepository.findEmployeeAccountByStaffId(requestDto.getStaffId())).thenReturn(employee);
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(category1);
        when(gameCategoryRepository.findGameCategoryByCategoryId(2)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.createGameRequest(requestDto);
        });

        // Assert
        assertEquals("Category not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateGameRequest_Success() {
        // Arrange
        Integer requestId = 1;
        GameRequestRequestDto requestDto = new GameRequestRequestDto(
                "Updated Game",
                "Updated Description",
                "http://newimage.com/image.jpg",
                Date.valueOf(LocalDate.now()),
                1,
                Arrays.asList(1));

        GameRequest gameRequest = new GameRequest();
        gameRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);
        gameRequest.setCategories(new GameCategory[] {});

        GameCategory category = new GameCategory(true, "Category 1");
        ReflectionTestUtils.setField(category, "categoryId", 1);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(category);
        when(gameRequestRepository.save(any(GameRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GameRequest updatedRequest = gameRequestService.updateGameRequest(requestId, requestDto);

        // Assert
        assertNotNull(updatedRequest);
        assertEquals("Updated Game", updatedRequest.getName());
        assertEquals("Updated Description", updatedRequest.getDescription());
        assertEquals("http://newimage.com/image.jpg", updatedRequest.getImageURL());
        assertEquals(1, updatedRequest.getCategories().size());
        verify(gameRequestRepository, times(1)).save(any(GameRequest.class));
    }

    @Test
    public void testUpdateGameRequest_RequestNotFound() {
        // Arrange
        Integer requestId = 1;
        GameRequestRequestDto requestDto = new GameRequestRequestDto();

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.updateGameRequest(requestId, requestDto);
        });

        // Assert
        assertEquals("Game request not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testUpdateGameRequest_CannotUpdateProcessedRequest() {
        // Arrange
        Integer requestId = 1;
        GameRequestRequestDto requestDto = new GameRequestRequestDto(
                "Updated Game",
                "Updated Description",
                "http://newimage.com/image.jpg",
                Date.valueOf(LocalDate.now()),
                1,
                Arrays.asList(1));

        GameRequest gameRequest = new GameRequest();
        ReflectionTestUtils.setField(gameRequest, "gameEntityId", requestId);
        gameRequest.setRequestStatus(GameRequest.RequestStatus.APPROVED); // Set status to APPROVED

        GameCategory category = new GameCategory(true, "Category 1");
        ReflectionTestUtils.setField(category, "categoryId", 1);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(category);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.updateGameRequest(requestId, requestDto);
        });

        assertEquals("Cannot update processed request", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void testDeleteGameRequest_Success() {
        // Arrange
        Integer requestId = 1;
        GameRequest gameRequest = new GameRequest();
        gameRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);

        // Act
        gameRequestService.deleteGameRequest(requestId);

        // Assert
        verify(gameRequestRepository, times(1)).delete(gameRequest);
    }

    @Test
    public void testDeleteGameRequest_RequestNotFound() {
        // Arrange
        Integer requestId = 1;

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.deleteGameRequest(requestId);
        });

        // Assert
        assertEquals("Game request not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteGameRequest_CannotDeleteProcessedRequest() {
        // Arrange
        Integer requestId = 1;
        GameRequest gameRequest = new GameRequest();
        gameRequest.setRequestStatus(GameRequest.RequestStatus.APPROVED);
        ReflectionTestUtils.setField(gameRequest, "gameEntityId", requestId);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.deleteGameRequest(requestId);
        });

        assertEquals("Cannot delete processed request", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void testAddNote_Success() {
        // Arrange
        Integer requestId = 1;
        RequestNoteRequestDto noteDto = new RequestNoteRequestDto();
        noteDto.setContent("This is a test note.");
        noteDto.setNoteDate(Date.valueOf(LocalDate.now()));
        noteDto.setStaffWriterId(1);
        noteDto.setGameRequestId(requestId);

        GameRequest gameRequest = new GameRequest();

        EmployeeAccount staffAccount = new EmployeeAccount("employee@example.com", "password", true);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(employeeAccountRepository.findEmployeeAccountByStaffId(noteDto.getStaffWriterId()))
                .thenReturn(staffAccount);
        when(requestNoteRepository.save(any(RequestNote.class))).thenAnswer(invocation -> {
            RequestNote savedNote = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedNote, "noteId", 1); // Simulate database assigning an ID
            return savedNote;
        });

        // Act
        RequestNote addedNote = gameRequestService.addNote(requestId, noteDto);

        // Assert
        assertNotNull(addedNote);
        assertEquals("This is a test note.", addedNote.getContent());
        assertEquals(staffAccount, addedNote.getNotesWriter());
        assertEquals(gameRequest, addedNote.getGameRequest());
        verify(requestNoteRepository, times(1)).save(any(RequestNote.class));
    }

    @Test
    public void testAddNote_GameRequestNotFound() {
        // Arrange
        Integer requestId = 1;
        RequestNoteRequestDto noteDto = new RequestNoteRequestDto();
        noteDto.setContent("This is a test note.");
        noteDto.setNoteDate(Date.valueOf(LocalDate.now()));
        noteDto.setStaffWriterId(1);
        noteDto.setGameRequestId(requestId);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.addNote(requestId, noteDto);
        });

        // Assert
        assertEquals("Game request not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testAddNote_StaffNotFound() {
        // Arrange
        Integer requestId = 1;
        RequestNoteRequestDto noteDto = new RequestNoteRequestDto();
        noteDto.setContent("This is a test note.");
        noteDto.setNoteDate(Date.valueOf(LocalDate.now()));
        noteDto.setStaffWriterId(1);
        noteDto.setGameRequestId(requestId);

        GameRequest gameRequest = new GameRequest();

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(employeeAccountRepository.findEmployeeAccountByStaffId(noteDto.getStaffWriterId())).thenReturn(null);
        when(managerAccountRepository.findManagerAccountByStaffId(noteDto.getStaffWriterId())).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.addNote(requestId, noteDto);
        });

        // Assert
        assertEquals("Staff member not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testDeleteNote_Success() {
        // Arrange
        Integer requestId = 1;
        Integer noteId = 2;

        GameRequest gameRequest = new GameRequest();
        ReflectionTestUtils.setField(gameRequest, "gameEntityId", requestId);

        RequestNote note = new RequestNote();
        ReflectionTestUtils.setField(note, "noteId", noteId);
        note.setGameRequest(gameRequest);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(requestNoteRepository.findRequestNoteByNoteId(noteId)).thenReturn(note);

        // Act
        gameRequestService.deleteNote(requestId, noteId);

        // Assert
        verify(requestNoteRepository, times(1)).delete(note);
    }

    @Test
    public void testDeleteNote_NoteNotFound() {
        // Arrange
        Integer requestId = 1;
        Integer noteId = 2;

        when(requestNoteRepository.findRequestNoteByNoteId(noteId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.deleteNote(requestId, noteId);
        });

        // Assert
        assertEquals("Note not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void testProcessRequest_Approve_Success() {
        // Arrange
        Integer requestId = 1;
        Integer managerId = 100;
        boolean approval = true;

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setPrice(59.99);
        approvalDto.setQuantityInStock(10);
        RequestNoteRequestDto noteDto = new RequestNoteRequestDto();
        noteDto.setContent("Approved with conditions.");
        noteDto.setNoteDate(Date.valueOf(LocalDate.now()));
        noteDto.setStaffWriterId(managerId);
        approvalDto.setNote(noteDto);

        ManagerAccount manager = new ManagerAccount();

        GameRequest gameRequest = new GameRequest();
        gameRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);
        gameRequest.setName("Game Name");
        gameRequest.setDescription("Game Description");
        gameRequest.setImageURL("http://gameimage.com");
        gameRequest.setCategories(new GameCategory[] {});

        when(managerAccountRepository.findManagerAccountByStaffId(managerId)).thenReturn(manager);
        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameRequestRepository.save(any(GameRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GameRequest processedRequest = gameRequestService.processRequest(requestId, managerId, approval, approvalDto);

        // Assert
        assertNotNull(processedRequest);
        assertEquals(GameRequest.RequestStatus.APPROVED, processedRequest.getRequestStatus());
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(gameRequestRepository, times(1)).save(any(GameRequest.class));
    }

    @Test
    public void testProcessRequest_Reject_Success() {
        // Arrange
        Integer requestId = 1;
        Integer managerId = 100;
        boolean approval = false;

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        RequestNoteRequestDto noteDto = new RequestNoteRequestDto();
        noteDto.setContent("Request rejected due to budget constraints.");
        noteDto.setNoteDate(Date.valueOf(LocalDate.now()));
        noteDto.setStaffWriterId(managerId);
        approvalDto.setNote(noteDto);

        ManagerAccount manager = new ManagerAccount();

        GameRequest gameRequest = new GameRequest();
        gameRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);

        when(managerAccountRepository.findManagerAccountByStaffId(managerId)).thenReturn(manager);
        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);
        when(gameRequestRepository.save(any(GameRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        GameRequest processedRequest = gameRequestService.processRequest(requestId, managerId, approval, approvalDto);

        // Assert
        assertNotNull(processedRequest);
        assertEquals(GameRequest.RequestStatus.REFUSED, processedRequest.getRequestStatus());
        verify(gameRequestRepository, times(1)).save(any(GameRequest.class));
    }

    @Test
    public void testProcessRequest_ManagerNotFound() {
        // Arrange
        Integer requestId = 1;
        Integer managerId = 100;
        boolean approval = true;
        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();

        when(managerAccountRepository.findManagerAccountByStaffId(managerId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.processRequest(requestId, managerId, approval, approvalDto);
        });

        // Assert
        assertEquals("Only managers can process game requests", exception.getMessage());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void testGetAllRequests_Success() {
        // Arrange
        GameRequest gameRequest1 = new GameRequest();
        GameRequest gameRequest2 = new GameRequest();

        when(gameRequestRepository.findAll()).thenReturn(Arrays.asList(gameRequest1, gameRequest2));

        // Act
        List<GameRequest> requests = gameRequestService.getAllRequests();

        // Assert
        assertNotNull(requests);
        assertEquals(2, requests.size());
        verify(gameRequestRepository, times(1)).findAll();
    }

    @Test
    public void testGetRequest_Success() {
        // Arrange
        Integer requestId = 1;
        GameRequest gameRequest = new GameRequest();
        ReflectionTestUtils.setField(gameRequest, "gameEntityId", requestId);

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(gameRequest);

        // Act
        GameRequest request = gameRequestService.getRequest(requestId);

        // Assert
        assertNotNull(request);
        assertEquals(requestId, request.getGameEntityId());
        verify(gameRequestRepository, times(1)).findGameRequestByGameEntityId(requestId);
    }

    @Test
    public void testGetRequest_NotFound() {
        // Arrange
        Integer requestId = 1;

        when(gameRequestRepository.findGameRequestByGameEntityId(requestId)).thenReturn(null);

        // Act
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameRequestService.getRequest(requestId);
        });

        // Assert
        assertEquals("Game request not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}