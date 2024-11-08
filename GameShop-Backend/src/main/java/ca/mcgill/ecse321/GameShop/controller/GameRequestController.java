package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.GameRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.service.GameRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games/request")
@CrossOrigin(origins = "*")
public class GameRequestController {

    @Autowired
    private GameRequestService gameRequestService;

    /**
     * Submit a new game request
     */
    @PostMapping
    public ResponseEntity<GameRequestDto> submitGameRequest(
            @RequestBody GameRequestDto request) {
        return ResponseEntity.ok(gameRequestService.createGameRequest(request));
    }

    /**
     * Edit an existing game request
     */
    @PostMapping("/{requestId}")
    public ResponseEntity<GameRequestDto> editGameRequest(
            @PathVariable Integer requestId,
            @RequestBody GameRequestDto request) {
        return ResponseEntity.ok(gameRequestService.updateGameRequest(requestId, request));
    }

    /**
     * Delete a game request
     */
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteGameRequest(@PathVariable Integer requestId) {
        gameRequestService.deleteGameRequest(requestId);
        return ResponseEntity.ok().build();
    }

    /**
     * Add a note to a game request
     */
    @PostMapping("/{requestId}/note")
    public ResponseEntity<RequestNoteDto> addNote(
            @PathVariable Integer requestId,
            @RequestBody RequestNoteDto note) {
        return ResponseEntity.ok(gameRequestService.addNote(requestId, note));
    }

    /**
     * Delete a note from a game request
     */
    @DeleteMapping("/{requestId}/note/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Integer requestId,
            @PathVariable Integer noteId) {
        gameRequestService.deleteNote(requestId, noteId);
        return ResponseEntity.ok().build();
    }

    /**
     * Approve or reject a game request - manager only
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
     * Get all game requests
     */
    @GetMapping
    public ResponseEntity<List<GameRequestDto>> getAllRequests() {
        return ResponseEntity.ok(gameRequestService.getAllRequests());
    }

    /**
     * Get a specific game request
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<GameRequestDto> getRequest(@PathVariable Integer requestId) {
        return ResponseEntity.ok(gameRequestService.getRequest(requestId));
    }
}