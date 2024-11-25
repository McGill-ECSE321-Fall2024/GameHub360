package ca.mcgill.ecse321.GameShop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.GameShop.dto.GameRequestApprovalDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestRequestDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestResponseDto;
import ca.mcgill.ecse321.GameShop.dto.GameRequestsListDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteRequestDto;
import ca.mcgill.ecse321.GameShop.dto.RequestNoteResponseDto;
import ca.mcgill.ecse321.GameShop.model.GameRequest;
import ca.mcgill.ecse321.GameShop.model.RequestNote;
import ca.mcgill.ecse321.GameShop.service.GameRequestService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/games/request")
public class GameRequestController {

    @Autowired
    private GameRequestService gameRequestService;

    /**
     * Submits a new game request.
     *
     * @param request the game request data transfer object
     * @return the created game request as a response entity
     */
    @PostMapping
    public GameRequestResponseDto submitGameRequest(@RequestBody @Valid GameRequestRequestDto request) {
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
            @PathVariable("requestId") int requestId,
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
    public Void deleteGameRequest(@PathVariable("requestId") int requestId) {
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
            @PathVariable("requestId") int requestId,
            @RequestBody @Valid RequestNoteRequestDto note) {
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
            @PathVariable("requestId") int requestId,
            @PathVariable("noteId") int noteId) {
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
            @PathVariable("requestId") int requestId,
            @RequestParam("managerId") int managerId,
            @RequestParam("approval") boolean approval,
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
    public GameRequestsListDto getAllRequests() {
        List<GameRequest> requests = gameRequestService.getAllRequests();
        List<GameRequestResponseDto> responseDtos = requests.stream()
                .map(GameRequestResponseDto::new)
                .collect(Collectors.toList());
        return new GameRequestsListDto(responseDtos);
    }

    /**
     * Retrieves a specific game request.
     *
     * @param requestId the ID of the game request to retrieve
     * @return the requested game request as a response entity
     */
    @GetMapping("/{requestId}")
    public GameRequestResponseDto getRequest(@PathVariable("requestId") int requestId) {
        GameRequest request = gameRequestService.getRequest(requestId);
        return new GameRequestResponseDto(request);
    }
}