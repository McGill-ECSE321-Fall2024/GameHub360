package ca.mcgill.ecse321.GameShop.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    @Autowired
    private ActivityLogService activityLogService;

    /**
     * Authenticates an employee by verifying email and password.
     * 
     * @param employeeRequestDto the login request containing email and password.
     * @return the authenticated EmployeeAccount.
     * @throws GameShopException if authentication fails.
     */
    @Transactional
    public EmployeeAccount login(EmployeeRequestDto employeeRequestDto) {
        // Find employee by email
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByEmail(employeeRequestDto.getEmail());

        // Check if employee exists and password matches
        if (employee == null || !EncryptionUtils.matches(employeeRequestDto.getPassword(), employee.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }

        if (!employee.getIsActive()) {
            throw new IllegalStateException("Employee account is deactivated. Please contact the manager.");
        }
        return employee;
    }

    /**
     * Creates a new employee account.
     * 
     * @param employeeRequestDto the details for creating the employee.
     * @return the created EmployeeAccount.
     * @throws GameShopException if an employee already exists or if password
     *                           requirements are not met.
     */
    @Transactional
    public EmployeeAccount createEmployee(EmployeeRequestDto employeeRequestDto) {
        // get new employee details
        String newEmployeeEmail = employeeRequestDto.getEmail();
        String newEmployeePassword = employeeRequestDto.getPassword();

        // Check if employee already exists
        if (employeeAccountRepository.findEmployeeAccountByEmail(newEmployeeEmail) != null) {
            throw new GameShopException(HttpStatus.CONFLICT, "An employee with the same email already exists.");
        }

        // Validate and encrypt password
        if (!PasswordUtils.isValidPassword(newEmployeePassword)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
        }
        String encryptedPassword = EncryptionUtils.encrypt(newEmployeePassword);

        // Set up and save the new employee account
        EmployeeAccount employee = new EmployeeAccount(newEmployeeEmail, encryptedPassword,
                employeeRequestDto.getIsActive());
        employee.setName(employeeRequestDto.getName());
        employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());

        EmployeeAccount createdEmployee = employeeAccountRepository.save(employee);
        // Log activity for account creation
        String contentMessage = "Employee account created for ID: " + createdEmployee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, createdEmployee);

        return createdEmployee;
    }

    /**
     * Updates an existing employee account. Email cannot be changed.
     * 
     * @param employeeId         The ID of the employee to update.
     * @param employeeRequestDto the employee details to update.
     * @return the updated EmployeeAccount.
     * @throws GameShopException if the employee is not found or if password
     *                           requirements are not met.
     */
    @Transactional
    public EmployeeAccount updateEmployee(Integer employeeId, EmployeeRequestDto employeeRequestDto) {
        // Find employee by ID
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        /// Check if employee exists
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        // get new employee password
        String newEmployeePassword = employeeRequestDto.getPassword();

        // Updating allowed fields only
        if (newEmployeePassword != null) {
            if (!PasswordUtils.isValidPassword(newEmployeePassword)) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
            }
            employee.setPassword(EncryptionUtils.encrypt(newEmployeePassword));
        }
        if (employeeRequestDto.getName() != null) {
            employee.setName(employeeRequestDto.getName());
        }
        if (employeeRequestDto.getPhoneNumber() != null) {
            employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());
        }
        if (employeeRequestDto.getIsActive() != null) {
            employee.setIsActive(employeeRequestDto.getIsActive());
        }

        EmployeeAccount updatedEmployee = employeeAccountRepository.save(employee);
        // Log activity for update
        String contentMessage = "Employee account updated for ID: " + updatedEmployee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, updatedEmployee);
        return updatedEmployee;
    }

    /**
     * Deactivates an employee account.
     * 
     * @param employeeId The ID of the employee to deactivate.
     * @return the deactivated EmployeeAccount.
     * @throws GameShopException if the employee is not found.
     */
    @Transactional
    public EmployeeAccount deactivateEmployee(Integer employeeId) {
        // Find employee by ID
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        // Check if employee exists
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        // Deactivate employee
        employee.setIsActive(false);
        EmployeeAccount deactivatedEmployee = employeeAccountRepository.save(employee);

        // Log activity for deactivation
        String contentMessage = "Employee account deactivated for ID: " + employee.getStaffId() + " on "
                + LocalDateTime.now();
        activityLogService.logActivity(contentMessage, employee);
        return deactivatedEmployee;
    }

    /**
     * Retrieves an employee account by ID.
     * 
     * @param employeeId The ID of the employee to retrieve.
     * @return the EmployeeAccount.
     * @throws GameShopException if the employee is not found.
     */
    @Transactional
    public EmployeeAccount getEmployeeById(Integer employeeId) {
        // Find employee by ID
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        // Check if employee exists
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        return employee;
    }

    /**
     * Retrieves all employee accounts.
     *
     * @param None
     * @return A list of EmployeeAccount objects.
     */
    @Transactional
    public List<EmployeeAccount> getAllEmployees() {
            return employeeAccountRepository.findAll();
        }

    /**
     * Retrieves employees by their active/inactive status.
     *
     * @param isActive The status to filter employees by (true for active, false for inactive).
     * @return List of employees matching the given status.
     */
    @Transactional
    public List<EmployeeAccount> getEmployeesByStatus(Boolean isActive) {
        return employeeAccountRepository.findEmployeesByIsActive(isActive);
    }

    @Transactional
    public boolean isEmployeeActive(Integer employeeId) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);
        if (employee == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        return employee.getIsActive();
    }
}
