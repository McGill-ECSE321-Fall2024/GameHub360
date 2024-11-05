package ca.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.GameShop.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.GameShop.exception.EmployeeException;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.repository.EmployeeAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import jakarta.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeAccountRepository employeeAccountRepository;

    /**
     * Authenticates an employee by verifying email and password.
     * 
     * @param employeeRequestDto the login request containing email and password.
     * @return the authenticated EmployeeAccount.
     * @throws EmployeeException if authentication fails.
     */
    @Transactional
    public EmployeeAccount login(EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByEmail(employeeRequestDto.getEmail());

        if (employee == null || !EncryptionUtils.matches(employeeRequestDto.getPassword(), employee.getPassword())) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }
        return employee;
    }

    /**
     * Creates a new employee account.
     * 
     * @param employeeRequestDto the details for creating the employee.
     * @return the created EmployeeAccount.
     * @throws EmployeeException if an employee already exists or if password
     *                           requirements are not met.
     */
    @Transactional
    public EmployeeAccount createEmployee(EmployeeRequestDto employeeRequestDto) {
        String newEmployeeEmail = employeeRequestDto.getEmail();
        String newEmployeePassword = employeeRequestDto.getPassword();

        if (employeeAccountRepository.findEmployeeAccountByEmail(newEmployeeEmail) != null) {
            throw new EmployeeException(HttpStatus.CONFLICT, "An employee with same email already exists.");
        }

        // Validate and encrypt password
        if (!PasswordUtils.isValidPassword(newEmployeePassword)) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
        }
        String encryptedPassword = EncryptionUtils.encrypt(newEmployeePassword);

        // Set up and save the new employee account
        EmployeeAccount employee = new EmployeeAccount(newEmployeeEmail, encryptedPassword,
                employeeRequestDto.getIsActive());
        employee.setName(employeeRequestDto.getName());
        employee.setPhoneNumber(employeeRequestDto.getPhoneNumber());

        return employeeAccountRepository.save(employee);
    }

    /**
     * Updates an existing employee account. Email cannot be changed.
     * 
     * @param employeeId         The ID of the employee to update.
     * @param employeeRequestDto the employee details to update.
     * @return the updated EmployeeAccount.
     * @throws EmployeeException if the employee is not found or if password
     *                           requirements are not met.
     */
    @Transactional
    public EmployeeAccount updateEmployee(Integer employeeId, EmployeeRequestDto employeeRequestDto) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        if (employee == null) {
            throw new EmployeeException(HttpStatus.NOT_FOUND, "Employee not found.");
        }

        String newEmployeePassword = employeeRequestDto.getPassword();

        // Updating allowed fields only
        if (newEmployeePassword != null) {
            if (!PasswordUtils.isValidPassword(newEmployeePassword)) {
                throw new EmployeeException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
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

        return employeeAccountRepository.save(employee);
    }

    /**
     * Deactivates an employee account.
     * 
     * @param employeeId The ID of the employee to deactivate.
     * @return the deactivated EmployeeAccount.
     * @throws EmployeeException if the employee is not found.
     */
    @Transactional
    public EmployeeAccount deactivateEmployee(Integer employeeId) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        if (employee == null) {
            throw new EmployeeException(HttpStatus.NOT_FOUND, "Employee not found.");
        }

        employee.setIsActive(false);
        return employeeAccountRepository.save(employee);
    }

    /**
     * Retrieves an employee account by ID. Used for admin purposes like retrieving
     * activity logs.
     * 
     * @param employeeId The ID of the employee to retrieve.
     * @return the retrieved EmployeeAccount.
     * @throws EmployeeException if the employee is not found.
     */
    @Transactional
    public EmployeeAccount retrieveEmployee(Integer employeeId) {
        EmployeeAccount employee = employeeAccountRepository.findEmployeeAccountByStaffId(employeeId);

        if (employee == null) {
            throw new EmployeeException(HttpStatus.NOT_FOUND, "Employee not found.");
        }

        return employee;
    }
}
