package ca.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.service.ManagerService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    /**
     * Endpoint to create a new manager.
     *
     * @param managerRequestDto The request object containing the manager's email,
     *                          password, name (optional), and phone number
     *                          (optional).
     * @return A response containing the newly created manager's details.
     */
    @PostMapping("/")
    public ManagerResponseDto createManager(
            @Validated({ ValidationGroups.Post.class }) @RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.createManager(managerRequestDto);
        return new ManagerResponseDto(manager);
    }

    /**
     * Endpoint to update an existing manager's details.
     *
     * @param managerRequestDto The request object containing the manager's email,
     *                          name (optional), phone number (optional), and
     *                          password (optional).
     * @return A response containing the updated manager's details.
     */
    @PutMapping
    public ManagerResponseDto updateManager(
            @Validated({ ValidationGroups.Update.class }) @RequestBody ManagerRequestDto managerRequestDto) {
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
    public ManagerResponseDto login(
            @Validated({ ValidationGroups.Post.class }) @RequestBody ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerService.login(managerRequestDto);
        return new ManagerResponseDto(manager);
    }

    /**
     * Retrieves the details of the single manager.
     *
     * @return A response containing the manager's details.
     */
    @GetMapping
    public ManagerResponseDto getManagerDetails() {
        ManagerAccount manager = managerService.getManagerDetails();
        return new ManagerResponseDto(manager);
    }
}
