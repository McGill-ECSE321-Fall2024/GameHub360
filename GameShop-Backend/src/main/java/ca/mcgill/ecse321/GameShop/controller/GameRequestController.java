package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.RequestNote;
import ca.mcgill.ecse321.GameShop.dto.GameRequestRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestResponseDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteResponseDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.service.GameRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * REST controller for managing game requests.
 */
@RestController
@RequestMapping("/games/request")
@CrossOrigin(origins = "*")
public class GameRequestController {

    private final GameRequestService gameRequestService;

    @Autowired
    public GameRequestController(GameRequestService gameRequestService) {
        this.gameRequestService = gameRequestService;
    }

    /**
     * Submits a new game request.
     *
     * @param request the game request data transfer object
     * @return the created game request as a response entity
     */
    @PostMapping
    public GameRequestResponseDto submitGameRequest(@RequestBody GameRequestRequestDto request) {
        GameRequest createdRequest = gameRequestService.createGameRequest(request);
        return new GameRequestResponseDto(createdRequest);
    }

    /**
     * Edits an existing game request.
     *
     * @param requestId the ID of the game request to edit
     * @param request   the updated game request data transfer object
     * @return the updated game request as a response entity
     */
    @PostMapping("/{requestId}")
    public GameRequestResponseDto editGameRequest(
            @PathVariable Integer requestId,
            @RequestBody GameRequestRequestDto request) {
        GameRequest updatedRequest = gameRequestService.updateGameRequest(requestId, request);
        return new GameRequestResponseDto(updatedRequest);
    }

    /**
     * Deletes a game request.
     *
     * @param requestId the ID of the game request to delete
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/{requestId}")
    public Void deleteGameRequest(@PathVariable Integer requestId) {
        gameRequestService.deleteGameRequest(requestId);
        return null;
    }

    /**
     * Adds a note to a game request.
     *
     * @param requestId the ID of the game request
     * @param note      the note request DTO
     * @return the added note as a response DTO
     */
    @PostMapping("/{requestId}/note")
    public RequestNoteResponseDto addNote(
            @PathVariable Integer requestId,
            @RequestBody RequestNoteRequestDto note) {
        RequestNote addedNote = gameRequestService.addNote(requestId, note);
        return new RequestNoteResponseDto(addedNote);
    }

    /**
     * Deletes a note from a game request.
     *
     * @param requestId the ID of the game request
     * @param noteId    the ID of the note to delete
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/{requestId}/note/{noteId}")
    public Void deleteNote(
            @PathVariable Integer requestId,
            @PathVariable Integer noteId) {
        gameRequestService.deleteNote(requestId, noteId);
        return null;
    }

    /**
     * Approves or rejects a game request. This operation is restricted to managers.
     *
     * @param requestId the ID of the game request
     * @param managerId the ID of the manager processing the request
     * @param approval  the approval status
     * @param noteDto   the optional note data transfer object
     * @return the processed game request as a response entity
     */
    @PutMapping("/{requestId}/approval")
    public GameRequestResponseDto processRequest(
            @PathVariable Integer requestId,
            @RequestParam Integer managerId,
            @RequestParam boolean approval,
            @RequestBody(required = false) GameRequestApprovalDto noteDto) {
        GameRequest processedRequest = gameRequestService.processRequest(requestId, managerId, approval, noteDto);
        return new GameRequestResponseDto(processedRequest);
    }

    /**
     * Retrieves all game requests.
     *
     * @return a list of all game requests as a response entity
     */
    @GetMapping
    public List<GameRequestResponseDto> getAllRequests() {
        List<GameRequest> requests = gameRequestService.getAllRequests();
        return requests.stream().map(GameRequestResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific game request.
     *
     * @param requestId the ID of the game request to retrieve
     * @return the requested game request as a response entity
     */
    @GetMapping("/{requestId}")
    public GameRequestResponseDto getRequest(@PathVariable Integer requestId) {
        GameRequest request = gameRequestService.getRequest(requestId);
        return new GameRequestResponseDto(request);
    }
}