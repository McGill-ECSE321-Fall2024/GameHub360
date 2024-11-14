package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;
import ca.mcgill.ecse321.GameShop.utils.PhoneUtils;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    /**
     * Authenticates a manager by verifying email and password.
     * 
     * @param managerRequestDto the login request containing email and password.
     * @return the authenticated ManagerAccount.
     * @throws GameShopException if authentication fails.
     */
    @Transactional
    public ManagerAccount login(ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerRequestDto.getEmail());

        if (manager == null || !EncryptionUtils.matches(managerRequestDto.getPassword(), manager.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }
        return manager;
    }

    /**
     * Creates a new manager account. Only one manager can exist at a time.
     * 
     * @param managerRequestDto the details for creating the manager.
     * @return the created ManagerAccount.
     * @throws GameShopException if a manager already exists or if password
     *                          requirements are not met.
     */
    @Transactional
    public ManagerAccount createManager(ManagerRequestDto managerRequestDto) {
        if (managerAccountRepository.count() > 0) {
            throw new GameShopException(HttpStatus.CONFLICT, "A manager already exists. Only one manager is allowed.");
        }

        // Validate and encrypt password
        if (!PasswordUtils.isValidPassword(managerRequestDto.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
        }
        String encryptedPassword = EncryptionUtils.encrypt(managerRequestDto.getPassword());

        // Validate phone number format
        if (!PhoneUtils.isValidPhoneNumber(managerRequestDto.getPhoneNumber())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Phone Number does not meet formatting criteria.");
        }

        // Set up and save the new manager account
        ManagerAccount manager = new ManagerAccount(managerRequestDto.getEmail(), encryptedPassword);
        manager.setName(managerRequestDto.getName());
        manager.setPhoneNumber(managerRequestDto.getPhoneNumber());

        return managerAccountRepository.save(manager);
    }

    /**
     * Updates an existing manager account. Email cannot be changed.
     * 
     * @param managerRequestDto the manager details to update.
     * @return the updated ManagerAccount.
     * @throws GameShopException if the manager is not found or if password
     *                          requirements are not met.
     */
    @Transactional
    public ManagerAccount updateManager(ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerRequestDto.getEmail());

        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Manager not found.");
        }

        // Update only allowed fields
        if (managerRequestDto.getPassword() != null) {
            if (!PasswordUtils.isValidPassword(managerRequestDto.getPassword())) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
            }
            manager.setPassword(EncryptionUtils.encrypt(managerRequestDto.getPassword()));
        }
        if (managerRequestDto.getName() != null) {
            manager.setName(managerRequestDto.getName());
        }
        if (managerRequestDto.getPhoneNumber() != null) {
            manager.setPhoneNumber(managerRequestDto.getPhoneNumber());
        }

        return managerAccountRepository.save(manager);
    }
}
