package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ManagerResponseDto;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    public ManagerResponseDto login(ManagerRequestDto managerRequestDto) {
        ManagerAccount manager = managerAccountRepository.findManagerAccountByEmail(managerRequestDto.getEmail());

        // Check if email exists and if the password matches
        if (manager != null && manager.getPassword().equals(managerRequestDto.getPassword())) {
            return new ManagerResponseDto(manager);
        }

        return null; // Incorrect email or password
    }
}
