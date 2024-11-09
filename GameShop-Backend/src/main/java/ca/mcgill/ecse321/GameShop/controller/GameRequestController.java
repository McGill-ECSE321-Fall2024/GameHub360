package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.service.GameRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<GameRequestDto> submitGameRequest(@RequestBody GameRequestDto request) {
        GameRequestDto createdRequest = gameRequestService.createGameRequest(request);
        return ResponseEntity.ok(createdRequest);
    }

    /**
     * Edits an existing game request.
     *
     * @param requestId the ID of the game request to edit
     * @param request   the updated game request data transfer object
     * @return the updated game request as a response entity
     */
    @PostMapping("/{requestId}")
    public ResponseEntity<GameRequestDto> editGameRequest(
            @PathVariable Integer requestId,
            @RequestBody GameRequestDto request) {
        GameRequestDto updatedRequest = gameRequestService.updateGameRequest(requestId, request);
        return ResponseEntity.ok(updatedRequest);
    }

    /**
     * Deletes a game request.
     *
     * @param requestId the ID of the game request to delete
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteGameRequest(@PathVariable Integer requestId) {
        gameRequestService.deleteGameRequest(requestId);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a note to a game request.
     *
     * @param requestId the ID of the game request
     * @param note      the note data transfer object
     * @return the added note as a response entity
     */
    @PostMapping("/{requestId}/note")
    public ResponseEntity<RequestNoteDto> addNote(
            @PathVariable Integer requestId,
            @RequestBody RequestNoteDto note) {
        RequestNoteDto addedNote = gameRequestService.addNote(requestId, note);
        return ResponseEntity.ok(addedNote);
    }

    /**
     * Deletes a note from a game request.
     *
     * @param requestId the ID of the game request
     * @param noteId    the ID of the note to delete
     * @return a response entity indicating the result of the operation
     */
    @DeleteMapping("/{requestId}/note/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Integer requestId,
            @PathVariable Integer noteId) {
        gameRequestService.deleteNote(requestId, noteId);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<GameRequestDto> processRequest(
            @PathVariable Integer requestId,
            @RequestParam Integer managerId,
            @RequestParam boolean approval,
            @RequestBody(required = false) GameRequestApprovalDto noteDto) {
        GameRequestDto response = gameRequestService.processRequest(requestId, managerId, approval, noteDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all game requests.
     *
     * @return a list of all game requests as a response entity
     */
    @GetMapping
    public ResponseEntity<List<GameRequestDto>> getAllRequests() {
        List<GameRequestDto> requests = gameRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * Retrieves a specific game request.
     *
     * @param requestId the ID of the game request to retrieve
     * @return the requested game request as a response entity
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<GameRequestDto> getRequest(@PathVariable Integer requestId) {
        GameRequestDto request = gameRequestService.getRequest(requestId);
        return ResponseEntity.ok(request);
    }
}