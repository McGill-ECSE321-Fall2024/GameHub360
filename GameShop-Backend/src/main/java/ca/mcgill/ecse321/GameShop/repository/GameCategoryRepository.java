package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.GameCategory;

public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    // Find game category by category id
    public GameCategory findGameCategoryByCategoryId(Integer categoryId);

    // Find game category by category id
    public GameCategory findGameCategoryByName(String name);

    // Find game categories by containing a specific promotion
    public List<GameCategory> findByPromotionsContaining(Promotion promotion);

    // Find game categories by containing a specific game
    public List<GameCategory> findByGamesContaining(Game game);

    // Find all game categories
    public List<GameCategory> findAllGameCategories();
}
