package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.exception.GameException;
import ca.mcgill.ecse321.GameShop.model.*;
import ca.mcgill.ecse321.GameShop.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GameRequestService {

    @Autowired
    private GameRequestRepository gameRequestRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCategoryRepository gameCategoryRepository;

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    private RequestNoteRepository requestNoteRepository;

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    /**
     * Create a new game request
     */
    @Transactional
    public GameRequestDto createGameRequest(GameRequestDto requestDto) {
        // Validate employee exists
        EmployeeAccount employee = employeeAccountRepository
                .findEmployeeAccountByStaffId(requestDto.getStaffId());
        if (employee == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        // Validate category exists
        GameCategory category = gameCategoryRepository
                .findGameCategoryByCategoryId(requestDto.getCategoryId());
        if (category == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
        }

        // Create new request
        GameRequest request = new GameRequest();
        request.setName(requestDto.getName());
        request.setDescription(requestDto.getDescription());
        request.setImageURL(requestDto.getImageUrl());
        request.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);
        request.setRequestDate(Date.valueOf(LocalDate.now()));
        request.setRequestPlacer(employee);
        request.setCategories(category);

        return convertToDto(gameRequestRepository.save(request));
    }

    /**
     * Update an existing game request
     */
    @Transactional
    public GameRequestDto updateGameRequest(Integer requestId, GameRequestDto requestDto) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Cannot update processed request");
        }

        // Update fields
        if (requestDto.getName() != null)
            request.setName(requestDto.getName());
        if (requestDto.getDescription() != null)
            request.setDescription(requestDto.getDescription());
        if (requestDto.getImageUrl() != null)
            request.setImageURL(requestDto.getImageUrl());
        if (requestDto.getCategoryId() != null) {
            GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(requestDto.getCategoryId());
            if (category == null) {
                throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
            }
            request.setCategories(category);
        }

        return convertToDto(gameRequestRepository.save(request));
    }

    /**
     * Delete a game request
     */
    @Transactional
    public void deleteGameRequest(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Cannot delete processed request");
        }

        gameRequestRepository.delete(request);
    }

    /**
     * Add a note to a game request
     */
    @Transactional
    public RequestNoteDto addNote(Integer requestId, RequestNoteDto noteDto) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        // Staff writer can be either EmployeeAccount or ManagerAccount
        StaffAccount writer = employeeAccountRepository.findEmployeeAccountByStaffId(noteDto.getStaffWriterId());
        if (writer == null) {
            writer = managerAccountRepository.findManagerAccountByStaffId(noteDto.getStaffWriterId());
            if (writer == null) {
                throw new GameException(HttpStatus.NOT_FOUND, "Staff member not found");
            }
        }

        RequestNote note = new RequestNote();
        note.setContent(noteDto.getContent());
        note.setNoteDate(Date.valueOf(LocalDate.now()));
        note.setNotesWriter(writer);
        note.setGameRequest(request);

        return new RequestNoteDto(requestNoteRepository.save(note));
    }

    /**
     * Delete a note from a game request
     */
    @Transactional
    public void deleteNote(Integer requestId, Integer noteId) {
        RequestNote note = requestNoteRepository.findRequestNoteByNoteId(noteId);
        if (note == null || note.getGameRequest() == null
                || note.getGameRequest().getGameEntityId() != requestId) {
            throw new GameException(HttpStatus.NOT_FOUND, "Note not found");
        }

        requestNoteRepository.delete(note);
    }

    /**
     * Process (approve/reject) a game request - only managers can do this
     */
    @Transactional
    public GameRequestDto processRequest(Integer requestId, Integer managerId, boolean approval,
            GameRequestApprovalDto approvalDto) {
        // Verify the manager exists
        ManagerAccount manager = managerAccountRepository.findManagerAccountByStaffId(managerId);
        if (manager == null) {
            throw new GameException(HttpStatus.FORBIDDEN, "Only managers can process game requests");
        }

        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        // Add note if provided
        if (approvalDto != null && approvalDto.getNote() != null) {
            approvalDto.getNote().setStaffWriterId(managerId);
            addNote(requestId, approvalDto.getNote());
        }

        if (approval) {
            // Validate price and quantity for approval
            if (approvalDto == null || approvalDto.getPrice() == null || approvalDto.getPrice() <= 0) {
                throw new GameException(HttpStatus.BAD_REQUEST, "Valid price must be provided for approval");
            }
            if (approvalDto.getQuantityInStock() == null || approvalDto.getQuantityInStock() < 0) {
                throw new GameException(HttpStatus.BAD_REQUEST, "Valid quantity must be provided for approval");
            }

            // Create new game from request with manager-specified price and quantity
            Game game = new Game();
            game.setName(request.getName());
            game.setDescription(request.getDescription());
            game.setImageURL(request.getImageURL());
            game.setQuantityInStock(approvalDto.getQuantityInStock());
            game.setPrice(approvalDto.getPrice());
            game.setIsAvailable(true);
            game.setCategories(request.getCategories().toArray(new GameCategory[0]));

            gameRepository.save(game);
            request.setRequestStatus(GameRequest.RequestStatus.APPROVED);
        } else {
            request.setRequestStatus(GameRequest.RequestStatus.REFUSED);
        }

        gameRequestRepository.save(request);
        return convertToDto(request);
    }

    /**
     * Get all game requests
     */
    @Transactional
    public List<GameRequestDto> getAllRequests() {
        return StreamSupport.stream(gameRequestRepository.findAll().spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific game request
     */
    @Transactional
    public GameRequestDto getRequest(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }
        return convertToDto(request);
    }

    /**
     * Helper method to convert GameRequest to GameRequestDto
     */
    private GameRequestDto convertToDto(GameRequest request) {
        GameRequestDto dto = new GameRequestDto();
        dto.setId(request.getGameEntityId());
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setImageUrl(request.getImageURL());
        dto.setStaffId(request.getRequestPlacer().getStaffId());
        dto.setRequestStatus(request.getRequestStatus());
        dto.setRequestDate(request.getRequestDate().toString());
        dto.setCategoryId(request.getCategories().get(0).getCategoryId());
        dto.setNotes(request.getAssociatedNotes().stream().map(RequestNoteDto::new).collect(Collectors.toList()));
        return dto;
    }
}
