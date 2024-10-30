package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.OrderGame;

public interface OrderGameRepository extends CrudRepository<OrderGame, Integer> {
    // Find order game by order game id
    public OrderGame findOrderGameById(Integer id);
}
