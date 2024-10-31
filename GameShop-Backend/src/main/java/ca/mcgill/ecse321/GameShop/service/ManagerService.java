package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ManagerService {

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    public ManagerAccount login(ManagerRequestDto managerRequestDto) {
        // Check if email is empty
        if (!StringUtils.hasText(managerRequestDto.getEmail())) {
            throw new ManagerException(HttpStatus.BAD_REQUEST, "Email cannot be empty.");
        }

        // Check if password is empty
        if (!StringUtils.hasText(managerRequestDto.getPassword())) {
            throw new ManagerException(HttpStatus.BAD_REQUEST, "Password cannot be empty.");
        }

        // Fetch manager by email
        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerRequestDto.getEmail());

        // Check if the email exists and if the password matches
        if (manager == null || !manager.getPassword().equals(managerRequestDto.getPassword())) {
            throw new ManagerException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }

        // Successful login
        return manager;
    }
}
