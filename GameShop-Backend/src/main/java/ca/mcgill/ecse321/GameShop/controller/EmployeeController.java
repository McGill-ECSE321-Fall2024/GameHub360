package ca.mcgill.ecse321.GameShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.GameShop.dto.ActivityLogListDto;
import ca.mcgill.ecse321.GameShop.dto.ActivityLogResponseDto;
import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.ActivityLog;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.service.ActivityLogService;
import ca.mcgill.ecse321.GameShop.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ActivityLogService activityLogService;

    /**
     * Endpoint to create a new employee.
     *
     * @param employeeRequestDto The request object containing the employee's email,
     *                           password, name (optional), phone number
     *                           (optional) and isActive (optional).
     * @return A response containing the newly created employee's details.
     */
    @PostMapping
    public EmployeeResponseDto createEmployee(
            @Validated({ ValidationGroups.Post.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.createEmployee(employeeRequestDto);

        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to update an existing employee's details by employee ID.
     *
     * @param employeeId         The ID of the employee to update.
     * @param employeeRequestDto The request object containing the employee's email,
     *                           password, name (optional), phone number
     *                           (optional) and isActive (optional).
     * @return A response containing the updated employee's details.
     */
    @PutMapping("/{employeeId}")
    public EmployeeResponseDto updateEmployee(
            @PathVariable("employeeId") Integer employeeId,
            @Validated({ ValidationGroups.Update.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.updateEmployee(employeeId, employeeRequestDto);

        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint for employee login.
     * 
     * @param employeeRequestDto The request object containing the employee's email
     *                           and password.
     * @return A response containing the employee's details if login is successful.
     */
    @PostMapping("/login")
    public EmployeeResponseDto login(
            @Validated({ ValidationGroups.Post.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.login(employeeRequestDto);
        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to deactivate an existing employee's details by employee ID.
     *
     * @param employeeId The ID of the employee to deactivate.
     * @return A response containing the deactivated employee's details.
     */
    @PutMapping("/{employeeId}/deactivate")
    public EmployeeResponseDto deactivateEmployee(@PathVariable("employeeId") Integer employeeId) {
        EmployeeAccount employee = employeeService.deactivateEmployee(employeeId);

        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to retrieve all activity logs for monitoring purposes for all
     * employees.
     *
     * @return A list of all activity logs.
     */
    @GetMapping("/activities")
    public ActivityLogListDto getAllEmployeesActivityLogs() {
        // Create a list of activity log response dtos
        List<ActivityLogResponseDto> activityLogsDto = new ArrayList<ActivityLogResponseDto>();

        // for each activity log, create a response dto and add to list of dtos
        for (ActivityLog log : activityLogService.getAllEmployeesActivityLogs()) {
            activityLogsDto.add(new ActivityLogResponseDto(log));
        }
        return new ActivityLogListDto(activityLogsDto);
    }

    /**
     * Endpoint to retrieve activity logs for monitoring purposes for an employee by
     * employee ID.
     *
     * @param employeeId The ID of the employee to retrieve activity logs for.
     * @return A list of all activity logs.
     */
    @GetMapping("/{employeeId}/activities")
    public ActivityLogListDto getEmployeeActivityLogs(@PathVariable("employeeId") Integer employeeId) {
        // Create a list of activity log response dtos
        List<ActivityLogResponseDto> activityLogsDto = new ArrayList<ActivityLogResponseDto>();
        // Get employee by ID
        EmployeeAccount employee = employeeService.getEmployeeById(employeeId);

        // for each activity log, create a response dto and add to list of dtos
        for (ActivityLog log : activityLogService.getEmployeeActivityLogs(employee)) {
            activityLogsDto.add(new ActivityLogResponseDto(log));
        }
        return new ActivityLogListDto(activityLogsDto);
    }
}
