package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    public Promotion findPromotionByPromotionId(Integer promotionId);
}
