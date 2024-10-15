package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;

@SpringBootTest
@Transactional
public class PromotionRepositoryTests {
    @Autowired
    private PromotionRepository repo;

    @Autowired
    private StoreInformationRepository storeRepo;

    @Autowired
    private GameCategoryRepository categoryRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        storeRepo.deleteAll();
        categoryRepo.deleteAll();
    }
    @Test
    public void testCreateAndRetrievePromotion() {
        // Arrange
        StoreInformation storeInfo = new StoreInformation();
        storeRepo.save(storeInfo);

        Double discount = 0.2;
        Promotion promotion = new Promotion(discount, storeInfo);
        promotion.setPromotionType(Promotion.PromotionType.GAME);
        
        // Act
        repo.save(promotion);

        Promotion retrievedPromotion = repo.findPromotionByPromotionId(promotion.getPromotionId());
        
        // Assert
        assertNotNull(retrievedPromotion);
        assertEquals(promotion.getPromotionId(), retrievedPromotion.getPromotionId());
        assertEquals(discount, retrievedPromotion.getDiscountPercentageValue());
        assertEquals(Promotion.PromotionType.GAME, retrievedPromotion.getPromotionType());

        assertNotNull(retrievedPromotion.getInfo());
        assertEquals(storeInfo.getStoreInfoId(), retrievedPromotion.getInfo().getStoreInfoId());
    }
}
