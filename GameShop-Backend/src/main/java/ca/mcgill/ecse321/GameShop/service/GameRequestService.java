package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
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

    /**
     * Create a new game request
     */
    @Transactional
    public GameRequestDto createGameRequest(GameRequestDto requestDto) {
        // Validate category exists
        GameCategory category = gameCategoryRepository.findGameCategoryByCategoryId(requestDto.getCategoryId());
        if (category == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
        }

        // Create new game request
        GameRequest request = new GameRequest();
        request.setName(requestDto.getName());
        request.setDescription(requestDto.getDescription());
        request.setImageURL(requestDto.getImageUrl());
        request.setRequestStatus(GameRequest.RequestStatus.SUBMITTED);
        request.setRequestDate(Date.valueOf(LocalDate.now()));
        request.addCategory(category);

        // Save and return
        GameRequest savedRequest = gameRequestRepository.save(request);
        return new GameRequestDto(savedRequest);
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

        // Update category if provided
        if (requestDto.getCategoryId() != null) {
            GameCategory newCategory = gameCategoryRepository.findGameCategoryByCategoryId(requestDto.getCategoryId());
            if (newCategory == null) {
                throw new GameException(HttpStatus.NOT_FOUND, "Category not found");
            }
            request.getCategories().clear();
            request.addCategory(newCategory);
        }

        return new GameRequestDto(gameRequestRepository.save(request));
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

        EmployeeAccount writer = employeeAccountRepository.findEmployeeAccountByStaffId(noteDto.getStaffWriterId());
        if (writer == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Staff member not found");
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
     * Process (approve/reject) a game request
     */
    @Transactional
    public GameRequestDto processRequest(Integer requestId, boolean approval, RequestNoteDto noteDto) {
        GameRequest request = gameRequestRepository.findGameRequestByGameEntityId(requestId);
        if (request == null) {
            throw new GameException(HttpStatus.NOT_FOUND, "Game request not found");
        }

        if (request.getRequestStatus() != GameRequest.RequestStatus.SUBMITTED) {
            throw new GameException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        // Add note if provided
        if (noteDto != null) {
            addNote(requestId, noteDto);
        }

        if (approval) {
            // Create new game from request
            Game game = new Game();
            game.setName(request.getName());
            game.setDescription(request.getDescription());
            game.setImageURL(request.getImageURL());
            game.setQuantityInStock(0);
            game.setIsAvailable(true);
            game.setPrice(0.0);
            // Instead of clearing and adding, just add each category
            for (GameCategory category : request.getCategories()) {
                game.addCategory(category);
            }

            gameRepository.save(game);
            request.setRequestStatus(GameRequest.RequestStatus.APPROVED);
        } else {
            request.setRequestStatus(GameRequest.RequestStatus.REFUSED);
        }

        return new GameRequestDto(gameRequestRepository.save(request));
    }

    /**
     * Get all game requests
     */
    @Transactional
    public List<GameRequestDto> getAllRequests() {
        return StreamSupport.stream(gameRequestRepository.findAll().spliterator(), false)
                .map(GameRequestDto::new)
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
        return new GameRequestDto(request);
    }
}