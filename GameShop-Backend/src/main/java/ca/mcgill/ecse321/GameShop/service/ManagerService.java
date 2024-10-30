package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.exception.ManagerNotFoundException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ManagerService {

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    public ManagerResponseDto login(ManagerRequestDto managerRequestDto) {
        // Check if email is empty
        if (!StringUtils.hasText(managerRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        // Check if password is empty
        if (!StringUtils.hasText(managerRequestDto.getPassword())) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerRequestDto.getEmail());

        // Check if email exists
        if (manager == null) {
            throw new ManagerNotFoundException("Email not found.");
        }

        // Check if the password matches
        if (!manager.getPassword().equals(managerRequestDto.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        // Successful login
        return new ManagerResponseDto(manager);
    }
}
