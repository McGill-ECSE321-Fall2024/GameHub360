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
        Promotion promotion = new Promotion(20.0, storeInfo);
        promotion.setPromotionType(Promotion.PromotionType.GAME);
        repo.save(promotion);

        Promotion retrievedPromotion = repo.findById(promotion.getPromotionId()).orElse(null);
        assertNotNull(retrievedPromotion);
        assertEquals(20.0, retrievedPromotion.getDiscountPercentageValue());
        assertEquals(Promotion.PromotionType.GAME, retrievedPromotion.getPromotionType());
        assertEquals(storeInfo.getStoreInfoId(), retrievedPromotion.getInfo().getStoreInfoId());
    }

    @Test
    public void testUpdatePromotion() {
        Promotion promotion = new Promotion(20.0, storeInfo);
        repo.save(promotion);

        promotion.setDiscountPercentageValue(30.0);
        repo.save(promotion);

        Promotion updatedPromotion = repo.findById(promotion.getPromotionId()).orElse(null);
        assertNotNull(updatedPromotion);
        assertEquals(30.0, updatedPromotion.getDiscountPercentageValue());
    }
    @Test
    public void testDeletePromotion() {
        Promotion promotion = new Promotion(20.0, storeInfo);
        repo.save(promotion);

        repo.delete(promotion);

        Promotion deletedPromotion = repo.findById(promotion.getPromotionId()).orElse(null);
        assertNull(deletedPromotion);
    }

    @Test
    public void testFindAllPromotions() {
        Promotion promotion1 = new Promotion(20.0, storeInfo);
        repo.save(promotion1);

        Promotion promotion2 = new Promotion(30.0, storeInfo);
        repo.save(promotion2);

        List<Promotion> promotions = new ArrayList<>();
        repo.findAll().forEach(promotions::add);

        assertEquals(2, promotions.size());
    }
    @Test
    public void testAddGameCategoryToPromotion() {
        Promotion promotion = new Promotion(20.0, storeInfo);
        GameCategory category = new GameCategory(true, "Action");
        categoryRepo.save(category);
        promotion.addPromotedCategory(category);
        repo.save(promotion);

        Promotion retrievedPromotion = repo.findById(promotion.getPromotionId()).orElse(null);
        assertNotNull(retrievedPromotion);
        assertTrue(retrievedPromotion.getPromotedCategories().contains(category));
    }

}
