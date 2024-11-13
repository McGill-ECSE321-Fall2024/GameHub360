package ca.mcgill.ecse321.GameShop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.Promotion;

public interface PromotionRepository extends CrudRepository<Promotion, Integer> {
    // Find promotion by promotion id
    public Promotion findPromotionByPromotionId(Integer promotionId);

    // Find promotions by containing a specific game
    public List<Promotion> findByPromotedGamesContaining(Game game);

    // Find promotions by containing a specific category
    public List<Promotion> findByPromotedCategoriesContaining(GameCategory category);
}
