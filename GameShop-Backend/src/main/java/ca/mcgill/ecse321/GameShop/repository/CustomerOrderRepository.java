package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Integer> {
    // Find order by order id
    public CustomerOrder findOrderByOrderId(Integer orderId);
}
