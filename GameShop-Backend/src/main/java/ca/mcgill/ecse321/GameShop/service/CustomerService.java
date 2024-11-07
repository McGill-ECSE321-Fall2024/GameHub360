package ca.mcgill.ecse321.GameShop.service;

import java.util.List;
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

    /**
     * Retrieves all customer accounts.
     *
     * @return A list of CustomerAccount objects representing all customer accounts.
     */
    public List<CustomerAccount> getAllCustomers() {
        return (List<CustomerAccount>) customerAccountRepository.findAll();
    }

    /**
     * Retrieves a customer account by its unique ID.
     *
     * @param customerId The unique ID of the customer to retrieve.
     * @return The CustomerAccount object corresponding to the given ID.
     * @throws GameShopException if no customer is found with the specified ID.
     */
    public CustomerAccount getCustomerById(Integer customerId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        return customer;
    }

    /**
     * Authenticates a customer based on their email and password.
     *
     * @param customerRequestDto The DTO containing the customer's email and password for authentication.
     * @return The CustomerAccount object of the authenticated customer.
     * @throws GameShopException if the email is not found or if the password does not match.
     */
    public CustomerAccount login(CustomerRequestDto customerRequestDto) {
        // Retrieve the customer by email
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByEmail(customerRequestDto.getEmail());

        // Check if customer exists and password matches
        if (customer == null || !EncryptionUtils.matches(customerRequestDto.getPassword(), customer.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }

        return customer;
    }
}
