package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.SalesMetricsDto;
import ca.mcgill.ecse321.GameShop.dto.StoreInformationRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.StoreInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StoreInformationService {

    @Autowired
    private StoreInformationRepository storeInformationRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private OrderGameRepository orderGameRepository;

    /**
     * Creates a new store policy based on the given details.
     * Ensures that only one store policy can exist at a time.
     * 
     * @param storeInfoRequestDto The details of the new store policy.
     * @return The created store policy.
     * @throws GameShopException if a store policy already exists.
     */
    @Transactional
    public StoreInformation createStorePolicy(StoreInformationRequestDto storeInfoRequestDto) {
        // Ensure only one store policy exists
        if (storeInformationRepository.count() > 0) {
            throw new GameShopException(HttpStatus.CONFLICT,
                    "A store policy already exists. Only one store policy is allowed.");
        }

        // Create and save a new store policy
        StoreInformation storeInformation = new StoreInformation();
        storeInformation.setStorePolicy(storeInfoRequestDto.getStorePolicy());

        return storeInformationRepository.save(storeInformation);
    }

    /**
     * Retrieves the current store policy.
     * 
     * @return The current store policy.
     * @throws GameShopException if no store policy is found.
     */
    public StoreInformation getStorePolicy() {
        // Fetch the store policy or throw an exception if not found
        StoreInformation storeInformation = storeInformationRepository.findFirstByOrderByStoreInfoIdAsc();
        if (storeInformation == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Store policy not found.");
        }
        return storeInformation;
    }

    /**
     * Updates the existing store policy with the provided details.
     * 
     * @param storeInfoRequestDto The updated policy details.
     * @return The updated store information.
     * @throws GameShopException if no existing store policy is found.
     */
    @Transactional
    public StoreInformation updatePolicy(StoreInformationRequestDto storeInfoRequestDto) {
        // Ensure there is an existing store policy to update
        StoreInformation existingPolicy = storeInformationRepository.findFirstByOrderByStoreInfoIdAsc();
        if (existingPolicy == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Store policy not found.");
        }

        // Update the policy details
        existingPolicy.setStorePolicy(storeInfoRequestDto.getStorePolicy());
        return storeInformationRepository.save(existingPolicy);
    }

    /**
     * Retrieves the store's current sales metrics.
     * 
     * @return The sales metrics data.
     */
    public SalesMetricsDto getSalesMetrics() {
        double totalSales = 0.0;
        int totalOrders = (int) customerOrderRepository.count(); // Total number of orders
        int totalGamesSold = (int) orderGameRepository.count(); // Total number of games sold
        int totalCustomers = (int) customerAccountRepository.count(); // Total number of unique customers

        // Calculate the total sales by summing the price of each sold game
        for (var orderGame : orderGameRepository.findAll()) {
            if (orderGame.getGame() != null) {
                totalSales += orderGame.getGame().getPrice();
            }
        }

        return new SalesMetricsDto(totalSales, totalOrders, totalGamesSold, totalCustomers);
    }
}
