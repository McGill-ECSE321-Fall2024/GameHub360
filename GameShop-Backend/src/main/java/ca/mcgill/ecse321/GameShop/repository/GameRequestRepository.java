package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.GameRequest;

public interface GameRequestRepository extends CrudRepository<GameRequest, Integer> {
    public GameRequest findGameRequestByRequestId(Integer requestId);
}
