package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ManagerRequestDto managerRequestDto) {
        try {
            // Check if email or password is empty
            if (!StringUtils.hasText(managerRequestDto.getEmail()) ||
                    !StringUtils.hasText(managerRequestDto.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email and password cannot be empty.");
            }

            // Attempt login 
            ManagerResponseDto managerResponseDto = managerService.login(managerRequestDto);

            if (managerResponseDto != null) {
                return ResponseEntity.ok(managerResponseDto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Incorrect email or password.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
