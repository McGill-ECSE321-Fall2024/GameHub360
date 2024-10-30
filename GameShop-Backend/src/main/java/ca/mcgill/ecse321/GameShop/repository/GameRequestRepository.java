package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.GameRequest;

public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {
    // Find game request by game entity id
    public GameRequest findGameRequestByGameEntityId(Integer gameEntityId);
}
