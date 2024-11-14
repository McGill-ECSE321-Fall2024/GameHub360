package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;

public interface StoreInformationRepository extends CrudRepository<StoreInformation, Integer> {
    // Find store information by store information id
    public StoreInformation findStoreInformationByStoreInfoId(Integer storeInfoId);

    // Method to get the first store information (assuming only one entry exists)
    StoreInformation findFirstByOrderByStoreInfoIdAsc();
}
