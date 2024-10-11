package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
    public Game findGameByGameEntityId(Integer gameEntityId);
}
