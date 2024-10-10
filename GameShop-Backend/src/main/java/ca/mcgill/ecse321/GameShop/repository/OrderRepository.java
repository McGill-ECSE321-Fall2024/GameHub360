package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    public Order findOrderByOrderId(Integer orderId);
}
