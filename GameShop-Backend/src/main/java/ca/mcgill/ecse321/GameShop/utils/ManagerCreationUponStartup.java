package ca.mcgill.ecse321.GameShop.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ca.mcgill.ecse321.GameShop.dto.ManagerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.ManagerAccount;
import ca.mcgill.ecse321.GameShop.repository.ManagerAccountRepository;
import ca.mcgill.ecse321.GameShop.service.ManagerService;

@Component
public class ManagerCreationUponStartup implements CommandLineRunner {

    @Autowired
    private ManagerAccountRepository managerAccountRepository;

    @Autowired
    private ManagerService managerService;

    @Override
    public void run(String... args) throws Exception {
        // Check if a manager already exists
        if (managerAccountRepository.count() == 0) {
            // Create a default manager account, can change the details later
            ManagerRequestDto defaultManager = new ManagerRequestDto();
            defaultManager.setEmail("manager@gameshop.com");
            defaultManager.setPassword("Manager@123");
            defaultManager.setName("Manager X");
            defaultManager.setPhoneNumber("123-456-7890");

            try {
                ManagerAccount manager = managerService.createManager(defaultManager);
                System.out.println("Default manager account created: " + manager.getEmail());
            } catch (GameShopException e) {
                System.err.println("Failed to create default manager: " + e.getMessage());
            }
        } else {
            System.out.println("Manager account already exists. Skipping creation.");
        }
    }
}
