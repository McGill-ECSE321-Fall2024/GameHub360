package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.StoreInformation;

public interface StoreInformationRepository extends CrudRepository<StoreInformation, Integer> {
    public StoreInformation findStoreInformationByStoreInfoId(Integer storeInfoId);
}
