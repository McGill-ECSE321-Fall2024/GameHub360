package ca.mcgill.ecse321.GameShop.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import ca.mcgill.ecse321.GameShop.model.StaffAccount;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.GameCategoryRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRequestRepository;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.RequestNoteRepository;

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
     * @param requestDto the game request data transfer object containing request
     *                   details
     * @return the created game request as a GameRequest
     * @throws GameShopException if employee not found or category not found
     */
    @Transactional
    public GameRequest createGameRequest(GameRequestRequestDto gameRequestRequestDto) {
        EmployeeAccount employee = employeeAccountRepository
                .findEmployeeAccountByStaffId(gameRequestRequestDto.getStaffId());
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        for (Integer categoryId : gameRequestRequestDto.getCategoryIds()) {
            GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
            if (category == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND, "Category not found");
            }
        }

        GameRequest gameRequest = new GameRequest();
        gameRequest.setName(gameRequestRequestDto.getName());
        gameRequest.setDescription(gameRequestRequestDto.getDescription());
        gameRequest.setImageURL(gameRequestRequestDto.getImageUrl());
        gameRequest.setRequestDate(Date.valueOf(LocalDate.now()));
        gameRequest.setRequestPlacer(employee);
        for (Integer categoryId : gameRequestRequestDto.getCategoryIds()) {
            GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
            gameRequest.addCategory(category);
        }
        gameRequest.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);

        return gameRequestRepository.save(gameRequest);
    }

    /**
     * Update an existing game request.
     * 
     * @param requestId  the ID of the game request to update
     * @param requestDto the game request data transfer object containing updated
     *                   request details
     * @return the updated game request as a GameRequest
     * @throws GameShopException if game request not found, request already
     *                           processed, or category not found
     */
    @Transactional
    public GameRequest updateGameRequest(Integer requestId, GameRequestRequestDto requestDto) {
        GameRequest gameRequest = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (gameRequest == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (gameRequest.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot update processed request");
        }

        if (requestDto.getName() != null) {
            gameRequest.setName(requestDto.getName());
        }
        if (requestDto.getDescription() != null) {
            gameRequest.setDescription(requestDto.getDescription());
        }
        if (requestDto.getImageUrl() != null) {
            gameRequest.setImageURL(requestDto.getImageUrl());
        }
        if (requestDto.getCategoryIds() != null) {
            for (Integer categoryId : requestDto.getCategoryIds()) {
                GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(categoryId);
                if (category == null) {
                    throw new GameShopException(HttpStatus.NOT_FOUND, "Category not found");
                }
                if (!gameRequest.getCategories().contains(category)) {
                    gameRequest.addCategory(category);
                }
            }
        }

        return gameRequestRepository.save(gameRequest);
    }

    /**
     * Delete a game request.
     * 
     * @param requestId the ID of the game request to delete
     * @throws GameShopException if game request not found or request already
     *                           processed
     */
    @Transactional
    public void deleteGameRequest(Integer requestId) {
        GameRequest gameRequest = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (gameRequest == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (gameRequest.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Cannot delete processed request");
        }

        gameRequestRepository.delete(gameRequest);
    }

    /**
     * Add a note to a game request.
     * 
     * @param requestId the ID of the game request to add a note to
     * @param noteDto   the request note data transfer object containing note
     *                  details
     * @return the created request note as a RequestNote
     * @throws GameShopException if game request not found or staff member not found
     */
    @Transactional
    public RequestNote addNote(Integer requestId, RequestNoteRequestDto noteDto) {
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

        return requestNoteRepository.save(note);
    }

    /**
     * Delete a note from a game request.
     * 
     * @param requestId the ID of the game request to delete a note from
     * @param noteId    the ID of the note to delete
     * @throws GameShopException if note not found
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
     * @param requestId   the ID of the game request to process
     * @param managerId   the ID of the manager processing the request
     * @param approval    true if the request is approved, false if rejected
     * @param approvalDto the game request approval data transfer object containing
     *                    approval details
     * @return the processed game request
     * @throws GameShopException if manager not found, game request not found, or
     *                           invalid approval details
     */
    @Transactional
    public GameRequest processRequest(Integer requestId, Integer managerId, boolean approval,
            GameRequestApprovalDto approvalDto) {
        ManagerAccount manager = managerAccountRepository.findManagerAccountByStaffId(managerId);
        if (manager == null) {
            throw new GameShopException(HttpStatus.FORBIDDEN, "Only managers can process game requests");
        }

        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
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

        return gameRequestRepository.save(request);
    }

    /**
     * Get all game requests.
     * 
     * @return a list of all game requests as GameRequest objects
     */
    @Transactional
    public List<GameRequest> getAllRequests() {
        return StreamSupport.stream(gameRequestRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific game request.
     * 
     * @param requestId the ID of the game request to retrieve
     * @return the retrieved game request as a GameRequest
     * @throws GameShopException if game request not found
     */
    @Transactional
    public GameRequest getRequest(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }
        return request;
    }

    /**
     * Get all notes for a specific game request.
     * 
     * @param requestId the ID of the game request to retrieve notes for
     * @return a list of all notes for the game request as RequestNote objects
     * @throws GameShopException if game request not found
     */
    @Transactional
    public List<RequestNote> getNotes(Integer requestId) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game request not found");
        }
        return request.getAssociatedNotes();
    }
}
