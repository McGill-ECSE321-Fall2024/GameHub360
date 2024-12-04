package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.service.ManagerService;
import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.service.EmployeeService;
import ca.mcgill.ecse321.GameShop.dto.ReviewRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ReviewResponseDto;
import ca.mcgill.ecse321.GameShop.model.Review;
import ca.mcgill.ecse321.GameShop.dto.ReplyRequestDto;
import ca.mcgill.ecse321.GameShop.service.ReviewService;
import ca.mcgill.ecse321.GameShop.dto.PromotionRequestDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionResponseDto;
import ca.mcgill.ecse321.GameShop.dto.PromotionListDto;
import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.service.PromotionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ReviewService reviewService;

    /**
     * Endpoint to create a new manager.
     *
     * @param managerRequestDto The request object containing the manager's email,
     *                          password, name (optional), and phone number
     *                          (optional).
     * @return A response containing the newly created manager's details.
     */
    @PostMapping
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
    @PutMapping("/")
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
     * Endpoint to create a new employee.
     *
     * @param employeeRequestDto The request object containing the employee's email,
     *                           password, name (optional), phone number
     *                           (optional) and isActive (optional).
     * @return A response containing the newly created employee's details.
     */
    @PostMapping("/employees/create")
    public EmployeeResponseDto createEmployee(
            @Validated({ ValidationGroups.Post.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.createEmployee(employeeRequestDto);

        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to retrieve all employees.
     *
     * @param isActive (Optional) Filter by active/inactive employees.
     * @return A list of employee response DTOs.
     */
    @GetMapping("/employees")
    public List<EmployeeResponseDto> getAllEmployees(@RequestParam(value = "isActive", required = false) Boolean isActive) {
        List<EmployeeAccount> employees;
        if (isActive != null) {
            employees = employeeService.getEmployeesByStatus(isActive);
        } else {
            employees = employeeService.getAllEmployees();
        }
        return employees.stream()
                .map(EmployeeResponseDto::new)
                .toList();
    }

    /**
     * Endpoint to fetch details of a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return The employee response DTO with logs.
     */
    @GetMapping("/employees/{employeeId}")
    public EmployeeResponseDto getEmployeeDetails(@PathVariable("employeeId") Integer employeeId) {
        EmployeeAccount employee = employeeService.getEmployeeById(employeeId);
        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to check if an employee is active.
     *
     * @param employeeId The ID of the employee.
     * @return True if the employee is active, otherwise false.
     */
    @GetMapping("/employees/{employeeId}/status")
    public boolean isEmployeeActive(@PathVariable("employeeId") Integer employeeId) {
        return employeeService.isEmployeeActive(employeeId);
    }

    /**
     * Endpoint to get promotions by type.
     *
     * @param type The type of the promotion (e.g., GAME, CATEGORY).
     * @return A list of promotions matching the given type.
     */
    @GetMapping("/promotions/type/{type}")
    public PromotionListDto getPromotionsByType(@PathVariable String type) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<>();
        for (Promotion promotion : promotionService.getPromotionsByType(type)) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to create a new store promotion.
     *
     * @param promotionRequestDto The details of the new promotion.
     * @return The created promotion details.
     */
    @PostMapping("/promotions/create")
    public ResponseEntity<PromotionResponseDto> createPromotion(
            @Valid @RequestBody PromotionRequestDto promotionRequestDto) {
        Promotion promotion = promotionService.createPromotion(promotionRequestDto);
        return ResponseEntity.status(201).body(new PromotionResponseDto(promotion));
    }

    /**
     * Endpoint to view all store promotions.
     *
     * @return A list of all current promotions.
     */
    @GetMapping("/promotions")
    public PromotionListDto getAllPromotions(@RequestParam(value = "type", required = false) String type) {
        List<PromotionResponseDto> promotionDtos = new ArrayList<>();
        List<Promotion> promotions;

        if (type != null) {
            promotions = promotionService.getPromotionsByType(type);
        } else {
            promotions = promotionService.getAllPromotions();
        }

        for (Promotion promotion : promotions) {
            promotionDtos.add(new PromotionResponseDto(promotion));
        }
        return new PromotionListDto(promotionDtos);
    }

    /**
     * Endpoint to fetch details of a specific promotion.
     *
     * @param promotionId The ID of the promotion.
     * @return The promotion response DTO with logs.
     */
    @GetMapping("/promotions/{promotionId}")
    public PromotionResponseDto getPromotionDetails(@PathVariable("promotionId") Integer promotionId) {
        Promotion promotion = promotionService.getPromotionById(promotionId);
        return new PromotionResponseDto(promotion);
    }

    /**
     * Get all reviews organized by game (for manager use).
     *
     * @return List of reviews grouped by games
     */
    @GetMapping("/reviews")
    public List<ReviewResponseDto> getAllReviewsOrganizedByGame() {
        return reviewService.getAllReviewsOrganizedByGame();
    }

    /**
     * Delete or reply to a specific review by ID.
     *
     * @param reviewId The ID of the review.
     * @param replyRequestDto Optional reply to the review.
     */
    @PutMapping("/reviews/{reviewId}")
    public void manageReview(
            @PathVariable("reviewId") int reviewId,
            @RequestBody(required = false) ReplyRequestDto replyRequestDto) {

        if (replyRequestDto != null) {
            reviewService.replyToReview(reviewId, replyRequestDto);
        } else {
            reviewService.deleteReview(reviewId);
        }
    }
}
