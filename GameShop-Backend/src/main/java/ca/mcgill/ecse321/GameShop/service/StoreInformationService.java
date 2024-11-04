package ca.mcgill.ecse321.GameShop.service;

import ca.mcgill.ecse321.GameShop.dto.SalesMetricsDto;
import ca.mcgill.ecse321.GameShop.dto.StoreInformationRequestDto;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import org.springframework.stereotype.Service;

@Service
public class StoreInformationService {

    /**
     * Creates a new store policy based on the given details.
     * 
     * @param storeInfoRequestDto The details of the new store policy.
     * @return The created store policy.
     */
    public StoreInformation createStorePolicy(StoreInformationRequestDto storeInfoRequestDto) {
        return null;
    }

    /**
     * Retrieves all store policies.
     * 
     * @return A list of all store policies.
     */
    public StoreInformation getStorePolicy() {
        return null;
    }

    /**
     * Updates the store policy based on the given details.
     * 
     * @param storeInfoRequestDto The updated policy details.
     * @return The updated store information.
     */
    public StoreInformation updatePolicy(StoreInformationRequestDto storeInfoRequestDto) {
        return null;
    }

    /**
     * Retrieves the store's current sales metrics.
     * 
     * @return The sales metrics data.
     */
    public SalesMetricsDto getSalesMetrics() {
        return null;
    }
}
