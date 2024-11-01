package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.service.ManagerService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * Endpoint to create a new manager.
     *
     * @param managerRequestDto The request object containing the manager's email,
     *                          password, name (optionally), and phone number
     *                          (optionally).
     * @return A response containing the newly created manager's details.
     */
    @PostMapping("/")
    public ManagerResponseDto createManager(@Valid @RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.createManager(managerRequestDto);
        return new ManagerResponseDto(manager);
    }

    /**
     * Endpoint to update an existing manager's details.
     *
     * @param managerRequestDto The request object containing the manager's email,
     *                          password, name (optionally), and phone number
     *                          (optionally).
     * @return A response containing the updated manager's details.
     */
    @PutMapping("/")
    public ManagerResponseDto updateManager(@Valid @RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.updateManager(managerRequestDto);
        return new ManagerResponseDto(manager);
    }

    /**
     * Endpoint for manager login.
     * 
     * @param managerRequestDto The request object containing the manager's email
     *                          and password.
     * @return A response containing the manager's details if login is successful.
     */
    @PostMapping("/login")
    public ManagerResponseDto login(@Valid @RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.login(managerRequestDto);
        return new ManagerResponseDto(manager);
    }
}
