package ca.mcgill.ecse321.GameShop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.ActivityLog;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.service.ActivityLogService;
import ca.mcgill.ecse321.GameShop.service.EmployeeService;

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
    @PostMapping("/")
    public EmployeeResponseDto createEmployee(
            @Validated({ ValidationGroups.Post.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.createEmployee(employeeRequestDto);
        // Log activity for account creation
        String contentMessage = "Employee account created for ID: " + employee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, employee);
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
            @PathVariable Integer employeeId,
            @Validated({ ValidationGroups.Update.class }) @RequestBody EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeService.updateEmployee(employeeId, employeeRequestDto);
        // Log activity for update
        String contentMessage = "Employee account updated for ID: " + employee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, employee);
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
    public EmployeeResponseDto deactivateEmployee(@PathVariable Integer employeeId) {
        EmployeeAccount employee = employeeService.deactivateEmployee(employeeId);
        // Log activity for deactivation
        String contentMessage = "Employee account deactivated for ID: " + employee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, employee);

        return new EmployeeResponseDto(employee);
    }

    /**
     * Endpoint to retrieve all activity logs for monitoring purposes for all
     * employees.
     *
     * @return A list of all activity logs.
     */
    @GetMapping("/activities")
    public List<ActivityLog> getAllEmployeesActivitiActivityLogs() {
        return activityLogService.getAllEmployeesActivityLogs();
    }

    /**
     * Endpoint to retrieve activity logs for monitoring purposes for an employee by
     * employee ID.
     *
     * @param employeeId The ID of the employee to retrieve activity logs for.
     * @return A list of all activity logs.
     */
    @GetMapping("/{employeeId}/activities")
    public List<ActivityLog> getEmployeeActivities(@PathVariable Integer employeeId) {
        return employeeService.retrieveEmployee(employeeId).getLogs();
    }
}
