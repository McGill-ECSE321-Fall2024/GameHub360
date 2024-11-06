package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.CustomerResponseDto;
import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.ValidationGroups;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


}
