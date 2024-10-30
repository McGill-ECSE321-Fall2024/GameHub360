package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerNotFoundException;
import ca.mcgill.ecse321.GameShop.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ManagerRequestDto managerRequestDto) {
        try {
            ManagerResponseDto managerResponseDto = managerService.login(managerRequestDto);
            return ResponseEntity.ok(managerResponseDto);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for input validation errors or incorrect password
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (ManagerNotFoundException e) {
            // Return 404 Not Found for non-existent email
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
