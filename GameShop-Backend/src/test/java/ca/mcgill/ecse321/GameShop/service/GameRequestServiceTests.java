package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.*;
import ca.mcgill.ecse321.GameShop.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
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

    private GameRequest testRequest;
    private GameCategory testCategory;
    private EmployeeAccount testEmployee;
    private RequestNote testNote;
    private GameRequestDto validRequestDto;

    @BeforeEach
    public void setUp() {
        testCategory = new GameCategory(true, "Test Category");
        testCategory.setCategoryType(GameCategory.CategoryType.GENRE);

        testEmployee = new EmployeeAccount("testEmployee", "password", true);

        testRequest = new GameRequest();
        testRequest.setRequestPlacer(testEmployee);
        testRequest.setName("Test Request");
        testRequest.setDescription("Test Description");
        testRequest.setImageURL("http://test.com/image.jpg");
        testRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);
        testRequest.setRequestDate(Date.valueOf(LocalDate.now()));
        testRequest.setCategories(testCategory);
        testRequest.setRequestPlacer(testEmployee);

        testNote = new RequestNote();
        testNote.setContent("Test Note");
        testNote.setNoteDate(Date.valueOf(LocalDate.now()));
        testNote.setNotesWriter(testEmployee);
        testNote.setGameRequest(testRequest);

        validRequestDto = new GameRequestDto();
        validRequestDto.setName("Test Request");
        validRequestDto.setDescription("Test Description");
        validRequestDto.setImageUrl("http://test.com/image.jpg");
        validRequestDto.setCategoryId(1);
    }

    @Test
    public void testCreateGameRequestSuccessfully() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(testCategory);
        when(gameRequestRepository.save(any(GameRequest.class))).thenReturn(testRequest);
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any())).thenReturn(testEmployee);

        // Act
        GameRequestDto response = gameRequestService.createGameRequest(validRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals("Test Request", response.getName());
        assertEquals("SUBMITTED", response.getRequestStatus().toString());
        verify(gameRequestRepository).save(any(GameRequest.class));
    }

    @Test
    public void testCreateGameRequestWithInvalidCategory() {
        // Arrange
        when(gameCategoryRepository.findGameCategoryByCategoryId(1)).thenReturn(null);
        when(employeeAccountRepository.findEmployeeAccountByStaffId(any())).thenReturn(testEmployee);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> gameRequestService.createGameRequest(validRequestDto));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    public void testUpdateGameRequestSuccessfully() {
        // Arrange
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);
        when(gameRequestRepository.save(any(GameRequest.class))).thenReturn(testRequest);

        GameRequestDto updateRequest = new GameRequestDto();
        updateRequest.setName("Updated Request");

        // Act
        GameRequestDto response = gameRequestService.updateGameRequest(1, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Request", response.getName());
        verify(gameRequestRepository).save(any(GameRequest.class));
    }

    @Test
    public void testUpdateProcessedGameRequest() {
        // Arrange
        testRequest.setRequestStatus(GameRequest.RequestStatus.APPROVED);
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> gameRequestService.updateGameRequest(1, validRequestDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Cannot update processed request", exception.getMessage());
    }

    @Test
    public void testAddNoteToGameRequest() {
        // Arrange
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);
        when(employeeAccountRepository.findEmployeeAccountByStaffId(1)).thenReturn(testEmployee);
        when(requestNoteRepository.save(any(RequestNote.class))).thenReturn(testNote);

        RequestNoteDto noteDto = new RequestNoteDto();
        noteDto.setContent("Test Note");
        noteDto.setStaffWriterId(1);

        // Act
        RequestNoteDto response = gameRequestService.addNote(1, noteDto);

        // Assert
        assertNotNull(response);
        assertEquals("Test Note", response.getContent());
        verify(requestNoteRepository).save(any(RequestNote.class));
    }

    @Test
    public void testDeleteNoteFromGameRequest() {
        // Arrange
        RequestNote mockNote = mock(RequestNote.class);
        GameRequest mockRequest = mock(GameRequest.class);

        when(mockNote.getGameRequest()).thenReturn(mockRequest);
        when(mockRequest.getGameEntityId()).thenReturn(1);
        when(requestNoteRepository.findRequestNoteByNoteId(1)).thenReturn(mockNote);

        // Act
        gameRequestService.deleteNote(1, 1);

        // Assert
        verify(requestNoteRepository).delete(mockNote);
        verify(mockNote, times(2)).getGameRequest();
        verify(mockRequest).getGameEntityId();
    }

    @Test
    public void testApproveGameRequest() {
        // Arrange
        testRequest.setCategories(testCategory);
        testRequest = spy(testRequest);
        when(testRequest.getAssociatedNotes()).thenReturn(new ArrayList<RequestNote>());
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);
        when(gameRepository.save(any(Game.class))).thenReturn(new Game());
        when(gameRequestRepository.save(any(GameRequest.class))).thenReturn(testRequest);
        when(managerAccountRepository.findManagerAccountByStaffId(1)).thenReturn(new ManagerAccount());

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setPrice(29.99);
        approvalDto.setQuantityInStock(10);

        // Act
        GameRequestDto response = gameRequestService.processRequest(1, 1, true, approvalDto);

        // Assert
        assertNotNull(response);
        assertEquals(GameRequest.RequestStatus.APPROVED.toString(), response.getRequestStatus().toString());
    }

    @Test
    public void testRejectGameRequest() {
        // Arrange
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);
        when(managerAccountRepository.findManagerAccountByStaffId(1)).thenReturn(new ManagerAccount());
        when(gameRequestRepository.save(any(GameRequest.class))).thenReturn(testRequest);

        GameRequestApprovalDto approvalDto = new GameRequestApprovalDto();
        approvalDto.setRejectionReason("Rejection reason");

        // Act
        GameRequestDto response = gameRequestService.processRequest(1, 1, false, approvalDto);

        // Assert
        assertNotNull(response);
        assertEquals(GameRequest.RequestStatus.REFUSED.toString(), response.getRequestStatus().toString());
        verify(gameRequestRepository).save(any(GameRequest.class));
    }

    @Test
    public void testProcessAlreadyProcessedRequest() {
        // Arrange
        testRequest.setRequestStatus(GameRequest.RequestStatus.APPROVED);
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);
        when(managerAccountRepository.findManagerAccountByStaffId(1)).thenReturn(new ManagerAccount());

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> gameRequestService.processRequest(1, 1, true, null));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Request already processed", exception.getMessage());
    }

    @Test
    public void testGetAllRequests() {
        // Arrange
        when(gameRequestRepository.findAll()).thenReturn(Arrays.asList(testRequest));

        // Act
        List<GameRequestDto> requests = gameRequestService.getAllRequests();

        // Assert
        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals("Test Request", requests.get(0).getName());
    }

    @Test
    public void testGetSpecificRequest() {
        // Arrange
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(testRequest);

        // Act
        GameRequestDto response = gameRequestService.getRequest(1);

        // Assert
        assertNotNull(response);
        assertEquals("Test Request", response.getName());
        assertEquals(GameRequest.RequestStatus.SUBMITTED.toString(), response.getRequestStatus().toString());
    }

    @Test
    public void testGetNonExistentRequest() {
        // Arrange
        when(gameRequestRepository.findGameRequestByGameEntityId(1)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class,
                () -> gameRequestService.getRequest(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game request not found", exception.getMessage());
    }
}