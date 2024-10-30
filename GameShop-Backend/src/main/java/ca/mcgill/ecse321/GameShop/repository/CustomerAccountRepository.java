package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;

public interface CustomerAccountRepository extends CrudRepository<CustomerAccount, Integer> {
    // Find customer account by customer id
    public CustomerAccount findCustomerAccountByCustomerId(Integer customerId);
}
