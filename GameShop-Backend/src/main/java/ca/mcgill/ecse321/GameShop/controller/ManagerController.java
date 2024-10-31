package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * Endpoint for manager login.
     * 
     * @param managerRequestDto The request object containing the manager's email
     *                          and password.
     * @return A response containing the manager's details if login is successful.
     * @throws ManagerException if the email or password is invalid.
     */
    @PostMapping("/login")
    public ManagerResponseDto login(@RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.login(managerRequestDto);
        return new ManagerResponseDto(manager);
    }
}
