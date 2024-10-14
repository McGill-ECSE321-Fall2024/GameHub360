package ca.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.GameShop.model.StoreInformation;

@SpringBootTest
public class StoreInformationRepositoryTests {
    @Autowired
    private StoreInformationRepository repo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    @Transactional
    void testCreateAndReadStoreInformation(){

        // ---- Arrange
        StoreInformation storeInfo = new StoreInformation();
        storeInfo.setStorePolicy("This is a store policy");
        storeInfo.addCurrentPromotion(0.45);
        storeInfo = repo.save(storeInfo);

        // ---- Act
        StoreInformation retrievedStoreInfo = repo.findStoreInformationByStoreInfoId(storeInfo.getStoreInfoId());

        // ---- Assert
        // Asserting the Attributes
        assertNotNull(retrievedStoreInfo);
        assertEquals(retrievedStoreInfo.getStoreInfoId(), storeInfo.getStoreInfoId());
        assertEquals(retrievedStoreInfo.getStorePolicy(), storeInfo.getStorePolicy());

        // Asserting the Associations
        assertEquals(retrievedStoreInfo.getCurrentPromotion(0).getPromotionId(), storeInfo.getCurrentPromotion(0).getPromotionId());
    }

}
