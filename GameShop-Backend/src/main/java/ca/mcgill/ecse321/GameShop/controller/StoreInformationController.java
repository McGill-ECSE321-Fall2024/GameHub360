package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.StoreInformationRequestDto;
import ca.mcgill.ecse321.GameShop.dto.StoreInformationResponseDto;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.dto.SalesMetricsDto;
import ca.mcgill.ecse321.GameShop.service.StoreInformationService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/store")
public class StoreInformationController {

    @Autowired
    private StoreInformationService storeInformationService;

    /**
     * Creates a new store policy.
     *
     * @param storeInfoRequestDto The request object containing details for the new
     *                            store policy.
     * @return A response containing the created store policy details.
     */
    @PostMapping("/policy")
    public StoreInformationResponseDto createPolicy(
            @Valid @RequestBody StoreInformationRequestDto storeInfoRequestDto) {
        StoreInformation storePolicy = storeInformationService.createStorePolicy(storeInfoRequestDto);
        return new StoreInformationResponseDto(storePolicy);
    }

    /**
     * Retrieves the current store policy.
     *
     * @return A response containing the details of the store policy.
     */
    @GetMapping("/policy")
    public StoreInformationResponseDto viewPolicy() {
        StoreInformation storePolicy = storeInformationService.getStorePolicy();
        return new StoreInformationResponseDto(storePolicy);
    }

    /**
     * Updates the store policy with new details.
     *
     * @param storeInfoRequestDto The request object containing the updated store
     *                            policy details.
     * @return A response containing the updated store policy details.
     */
    @PutMapping("/policy")
    public StoreInformationResponseDto managePolicy(
            @Valid @RequestBody StoreInformationRequestDto storeInfoRequestDto) {
        StoreInformation updatedPolicy = storeInformationService.updatePolicy(storeInfoRequestDto);
        return new StoreInformationResponseDto(updatedPolicy);
    }

    /**
     * Retrieves the current store sales metrics.
     *
     * @return A response containing the store’s sales metrics.
     */
    @GetMapping("/sales/metrics")
    public SalesMetricsDto viewSalesMetrics() {
        return storeInformationService.getSalesMetrics();
    }
}
