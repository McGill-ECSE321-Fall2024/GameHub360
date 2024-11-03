package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import ca.mcgill.ecse321.GameShop.model.Game;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Integer> {
    // Find game by game id
    public Game findGameByGameEntityId(Integer gameEntityId);

    // Find game by name
    public Game findGameByName(String name);

    // Find all games
    @NonNull
    public List<Game> findAll();
}
