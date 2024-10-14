package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.GameShop.model.Promotion;
import ca.mcgill.ecse321.GameShop.model.StoreInformation;
import ca.mcgill.ecse321.GameShop.model.GameCategory;

@SpringBootTest
@Transactional
public class PromotionRepositoryTests {
    @Autowired
    private PromotionRepository repo;

    @Autowired
    private StoreInformationRepository storeRepo;

    @Autowired
    private GameCategoryRepository categoryRepo;

    private StoreInformation storeInfo;
    @BeforeEach
    public void setUp() {
        storeInfo = new StoreInformation();
        storeRepo.save(storeInfo);
    }
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
        storeRepo.deleteAll();
        categoryRepo.deleteAll();
    }
    @Test
    public void testCreateAndRetrievePromotion() {
        // Arrange
        Promotion promotion = new Promotion(20.0, storeInfo);
        promotion.setPromotionType(Promotion.PromotionType.GAME);
        // Act
        repo.save(promotion);
        // Assert
        Promotion retrievedPromotion = repo.findPromotionByPromotionId(promotion.getPromotionId());
        assertNotNull(retrievedPromotion);
        assertEquals(promotion.getPromotionId(), retrievedPromotion.getPromotionId());
        assertEquals(20.0, retrievedPromotion.getDiscountPercentageValue());
        assertEquals(Promotion.PromotionType.GAME, retrievedPromotion.getPromotionType());
        assertEquals(storeInfo.getStoreInfoId(), retrievedPromotion.getInfo().getStoreInfoId());
    }

    @Test
    public void testUpdatePromotion() {
        // Arrange
        Promotion promotion = new Promotion(20.0, storeInfo);
        promotion.setPromotionType(Promotion.PromotionType.GAME);
        // Act
        repo.save(promotion);
        promotion.setDiscountPercentageValue(30.0);
        promotion.setPromotionType(Promotion.PromotionType.CATEGORY);
        promotion.getInfo().setStorePolicy("New store policy.");
        storeRepo.save(storeInfo);
        repo.save(promotion);
        // Assert
        Promotion updatedPromotion = repo.findPromotionByPromotionId(promotion.getPromotionId());
        assertNotNull(updatedPromotion);
        assertEquals(30.0, updatedPromotion.getDiscountPercentageValue());
        assertEquals(Promotion.PromotionType.CATEGORY, updatedPromotion.getPromotionType());
        assertEquals("New store policy.", updatedPromotion.getInfo().getStorePolicy());
    }
    @Test
    public void testDeletePromotion() {
        // Arrange
        Promotion promotion = new Promotion(20.0, storeInfo);
        // Act
        repo.save(promotion);
        repo.delete(promotion);
        storeRepo.delete(storeInfo);
        // Assert
        Promotion deletedPromotion = repo.findPromotionByPromotionId(promotion.getPromotionId());
        assertNull(deletedPromotion);
    }

}
