package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
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
     * Create a new game request.
     * 
     * @param requestDto the game request data transfer object containing request details
     * @return the created game request as a GameRequestDto
     */
    @Transactional
    public GameRequestDto createGameRequest(GameRequestDto requestDto) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(requestDto.getStaffId());
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(requestDto.getCategoryId());
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Category not found");
        }

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
     * Update an existing game request.
     * 
     * @param requestId the ID of the game request to update
     * @param requestDto the game request data transfer object containing updated request details
     * @return the updated game request as a GameRequestDto
     */
    @Transactional
    public GameRequestDto updateGameRequest(Integer requestId, GameRequestDto requestDto) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot update processed request");
        }

        if (requestDto.getName() != null) {
            request.setName(requestDto.getName());
        }
        if (requestDto.getDescription() != null) {
            request.setDescription(requestDto.getDescription());
        }
        if (requestDto.getImageUrl() != null) {
            request.setImageURL(requestDto.getImageUrl());
        }
        if (requestDto.getCategoryId() != null) {
            GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(requestDto.getCategoryId());
            if (category == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND, "Category not found");
            }
            request.setCategories(category);
        }

        return convertToDto(gameRequestRepository.save(request));
    }

    /**
     * Delete a game request.
     * 
     * @param requestId the ID of the game request to delete
     */
    @Transactional
    public void deleteGameRequest(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot delete processed request");
        }

        gameRequestRepository.delete(request);
    }

    /**
     * Add a note to a game request.
     * 
     * @param requestId the ID of the game request to add a note to
     * @param noteDto the request note data transfer object containing note details
     * @return the created request note as a RequestNoteDto
     */
    @Transactional
    public RequestNoteDto addNote(Integer requestId, RequestNoteDto noteDto) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        StaffAccount writer = employeeAccountRepository.findEmployeeAccountByStaffId(noteDto.getStaffWriterId());
        if (writer == null) {
            writer = managerAccountRepository.findManagerAccountByStaffId(noteDto.getStaffWriterId());
            if (writer == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND, "Staff member not found");
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
     * Delete a note from a game request.
     * 
     * @param requestId the ID of the game request to delete a note from
     * @param noteId the ID of the note to delete
     */
    @Transactional
    public void deleteNote(Integer requestId, Integer noteId) {
        RequestNote note = requestNoteRepository.findRequestNoteByNoteId(noteId);
        if (note == null || note.getGameRequest() == null
                || note.getGameRequest().getGameEntityId() != requestId) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Note not found");
        }

        requestNoteRepository.delete(note);
    }

    /**
     * Process (approve/reject) a game request - only managers can do this.
     * 
     * @param requestId the ID of the game request to process
     * @param managerId the ID of the manager processing the request
     * @param approval true if the request is approved, false if rejected
     * @param approvalDto the game request approval data transfer object containing approval details
     * @return the processed game request as a GameRequestDto
     */
    @Transactional
    public GameRequestDto processRequest(Integer requestId, Integer managerId, boolean approval, GameRequestApprovalDto approvalDto) {
        ManagerAccount manager = managerAccountRepository.findManagerAccountByStaffId(managerId);
        if (manager == null) {
            throw new GameShopException(HttpStatus.FORBIDDEN, "Only managers can process game requests");
        }

        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        if (approvalDto != null && approvalDto.getNote() != null) {
            approvalDto.getNote().setStaffWriterId(managerId);
            addNote(requestId, approvalDto.getNote());
        }

        if (approval) {
            if (approvalDto == null || approvalDto.getPrice() == null || approvalDto.getPrice() <= 0) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Valid price must be provided for approval");
            }
            if (approvalDto.getQuantityInStock() == null || approvalDto.getQuantityInStock() < 0) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Valid quantity must be provided for approval");
            }

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
     * Get all game requests.
     * 
     * @return a list of all game requests as GameRequestDto objects
     */
    @Transactional
    public List<GameRequestDto> getAllRequests() {
        return StreamSupport.stream(gameRequestRepository.findAll().spliterator(), false)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific game request.
     * 
     * @param requestId the ID of the game request to retrieve
     * @return the retrieved game request as a GameRequestDto
     */
    @Transactional
    public GameRequestDto getRequest(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }
        return convertToDto(request);
    }

    /**
     * Helper method to convert GameRequest to GameRequestDto.
     * 
     * @param request the game request to convert
     * @return the converted game request as a GameRequestDto
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
