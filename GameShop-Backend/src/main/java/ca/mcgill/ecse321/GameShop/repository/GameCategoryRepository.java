package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.GameCategory;

public interface GameCategoryRepository extends CrudRepository<GameCategory, Integer> {
    // Find game category by category id
    public GameCategory findGameCategoryByCategoryId(Integer categoryId);
}
