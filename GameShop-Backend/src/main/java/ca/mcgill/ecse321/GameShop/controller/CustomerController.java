package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.CustomerResponseDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;

    /**
     * Endpoint to create a new customer
     * 
     * @param customerRequestDto The request object containing the customer's email,
     *                          password, name (optional), and phone number
     *                          (optional).
     * @return A response containing the newly created customer's details.
     */
    @PostMapping("/")
    public CustomerResponseDto createCustomer(
            @Validated({ ValidationGroups.Post.class }) @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerAccount customer = customerService.createCustomer(customerRequestDto);
        return new CustomerResponseDto(customer);
    }

    /**
     * Endpoint to update a customer
     *
     * @param customerRequestDto The request object containing the customer's updated email,
     *                          password, name (optional), or phone number
     *                          (optional).
     * @param customerId The customer Id of the customer account that is being updated
     * @return A response containing the newly updated customer's details.
     */
    @PutMapping("/{customerId}")
    public CustomerResponseDto updateCustomer(
            @PathVariable Integer customerId,
            @Validated({ ValidationGroups.Update.class }) @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerAccount updatedCustomer = customerService.updateCustomer(customerId, customerRequestDto);
        return new CustomerResponseDto(updatedCustomer);
    }

    /**
     * Endpoint to retrieve all customer accounts.
     *
     * @return A list of CustomerResponseDto representing all customer accounts.
     */
    @GetMapping
    public List<CustomerResponseDto> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(CustomerResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint to retrieve a specific customer account by its ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return A CustomerResponseDto containing the customer details.
     */
    @GetMapping("/{customerId}")
    public CustomerResponseDto getCustomerById(@PathVariable Integer customerId) {
        CustomerAccount customer = customerService.getCustomerById(customerId);
        return new CustomerResponseDto(customer);
    }

    /**
     * Endpoint to log in a customer.
     *
     * @param customerRequestDto The DTO containing the customer's email and password for authentication.
     * @return A CustomerResponseDto containing the customer's details if login is successful.
     */
    @PostMapping("/login")
    public CustomerResponseDto login(@RequestBody CustomerRequestDto customerRequestDto) {
        CustomerAccount customer = customerService.login(customerRequestDto);
        return new CustomerResponseDto(customer);
    }

    /**
     * Endpoint to retrieve the order history of a specific customer.
     *
     * @param customerId The ID of the customer whose order history is being requested.
     * @return A list of CustomerOrderDto objects representing the customer's order history.
     */
//    @GetMapping("/{customerId}/orders")
//    public List<CustomerOrderDto> getOrderHistory(@PathVariable Integer customerId) {
//        return customerService.getOrderHistory(customerId).stream()
//                .map(CustomerOrderDto::new)
//                .collect(Collectors.toList());
//    }
}
