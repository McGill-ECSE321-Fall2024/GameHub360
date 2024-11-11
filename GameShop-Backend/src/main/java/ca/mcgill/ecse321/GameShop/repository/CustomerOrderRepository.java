package ca.mcgill.ecse321.GameShop.repository;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.CustomerOrder;

import java.util.List;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Integer> {
    // Find order by order id
    public CustomerOrder findOrderByOrderId(Integer orderId);

    // Find all orders for a specific customer by customer ID
    public List<CustomerOrder> findByOrderedBy(CustomerAccount customer);
}
