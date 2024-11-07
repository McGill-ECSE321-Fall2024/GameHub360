package ca.mcgill.ecse321.GameShop.service;

import java.util.Optional;

import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.utils.EncryptionUtils;
import ca.mcgill.ecse321.GameShop.utils.PasswordUtils;
import ca.mcgill.ecse321.GameShop.utils.PhoneUtils;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    
    /**
     * Creates a new customer account.
     * 
     * @param customerRequestDto the details for creating the customer.
     * @return the created CustomerAccount.
     * @throws GameShopException if the customer already exists or if password
     *                          requirements are not met.
     */
    @Transactional
    public CustomerAccount createCustomer(CustomerRequestDto customerRequestDto) {

        // Check if account already exists by email
        if (customerAccountRepository.findCustomerAccountByEmail(customerRequestDto.getEmail()) != null) {
            throw new GameShopException(HttpStatus.CONFLICT, "An account with this email already exists.");
        }

        // Validate and encrypt password
        if (!PasswordUtils.isValidPassword(customerRequestDto.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
        }
        String encryptedPassword = EncryptionUtils.encrypt(customerRequestDto.getPassword());

        // Validate phone number format
        if (!PhoneUtils.isValidPhoneNumber(customerRequestDto.getPhoneNumber())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Phone Number does not meet formatting criteria.");
        }

        // Set up and save the new customer account
        CustomerAccount customer = new CustomerAccount(customerRequestDto.getEmail(), encryptedPassword);
        customer.setName(customerRequestDto.getName());
        customer.setPhoneNumber(customerRequestDto.getPhoneNumber());

        return customerAccountRepository.save(customer);
    }

    /**
     * Updates an existing customer account.
     *
     * @param customerId the ID of the customer to update
     * @param customerRequestDto the updated customer data
     * @return the updated CustomerAccount
     * @throws GameShopException if customer is not found or if data validation fails
     */
    @Transactional
    public CustomerAccount updateCustomer(Integer customerId, CustomerRequestDto customerRequestDto) {
        // Retrieve the existing customer account by ID
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);

        // If customer not found, throw an exception
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        // Validate and potentially update password
        if (customerRequestDto.getPassword() != null && !customerRequestDto.getPassword().isEmpty()) {
            if (!PasswordUtils.isValidPassword(customerRequestDto.getPassword())) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Password does not meet security requirements.");
            }
            customer.setPassword(EncryptionUtils.encrypt(customerRequestDto.getPassword()));
        }

        // Update optional fields: name and phone number
        if (customerRequestDto.getName() != null) {
            customer.setName(customerRequestDto.getName());
        }

        if (customerRequestDto.getPhoneNumber() != null) {
            if (!PhoneUtils.isValidPhoneNumber(customerRequestDto.getPhoneNumber())) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Phone Number does not meet formatting criteria.");
            }
            customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
        }

        // Save and return the updated customer account
        return customerAccountRepository.save(customer);
    }
}
