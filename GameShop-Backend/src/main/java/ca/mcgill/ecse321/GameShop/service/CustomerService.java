package ca.mcgill.ecse321.GameShop.service;

import java.util.List;

import ca.mcgill.ecse321.GameShop.dto.CustomerRequestDto;
import ca.mcgill.ecse321.GameShop.dto.PaymentDetailsRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;
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
    @Autowired
    private CustomerOrderRepository customerOrderRepository;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private GameRepository gameRepository;

    /**
     * Creates a new customer account.
     * 
     * @param customerRequestDto the details for creating the customer.
     * @return the created CustomerAccount.
     * @throws GameShopException if the customer already exists or if password
     *                           requirements are not met.
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

        // Validate phone number format if provided
        if (customerRequestDto.getPhoneNumber() != null && !customerRequestDto.getPhoneNumber().isEmpty()) {
            if (!PhoneUtils.isValidPhoneNumber(customerRequestDto.getPhoneNumber())) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Phone Number does not meet formatting criteria.");
            }
        }

        // Set up and save the new customer account
        CustomerAccount customer = new CustomerAccount(customerRequestDto.getEmail(), encryptedPassword);

        // Set name if provided
        if (customerRequestDto.getName() != null && !customerRequestDto.getName().isEmpty()) {
            customer.setName(customerRequestDto.getName());
        }

        // Set phone number if provided
        if (customerRequestDto.getPhoneNumber() != null && !customerRequestDto.getPhoneNumber().isEmpty()) {
            customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
        }

        return customerAccountRepository.save(customer);
    }

    /**
     * Updates an existing customer account.
     *
     * @param customerId         the ID of the customer to update
     * @param customerRequestDto the updated customer data
     * @return the updated CustomerAccount
     * @throws GameShopException if customer is not found or if data validation
     *                           fails
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
        if (customerRequestDto.getPassword() != null) {
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
    @Transactional
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
    @Transactional
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
     * @param customerRequestDto The DTO containing the customer's email and
     *                           password for authentication.
     * @return The CustomerAccount object of the authenticated customer.
     * @throws GameShopException if the email is not found or if the password does
     *                           not match.
     */
    @Transactional
    public CustomerAccount login(CustomerRequestDto customerRequestDto) {
        // Retrieve the customer by email
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByEmail(customerRequestDto.getEmail());

        // Check if customer exists and password matches
        if (customer == null || !EncryptionUtils.matches(customerRequestDto.getPassword(), customer.getPassword())) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid email or password.");
        }

        return customer;
    }

    /**
     * Retrieves the order history for a specific customer by their ID.
     *
     * @param customerId The ID of the customer.
     * @return A list of CustomerOrder objects representing the customer's order
     *         history.
     * @throws GameShopException if the customer with customerId is not found.
     */
    @Transactional
    public List<CustomerOrder> getOrderHistoryByCustomerId(Integer customerId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        return customerOrderRepository.findByOrderedBy(customer);
    }

    /**
     * Creates a new payment card for a specific customer by their ID.
     *
     * @param customerId               The ID of the customer.
     * @param paymentDetailsRequestDto The payment details data for creation.
     * @return PaymentDetails representing the created payment card.
     * @throws GameShopException if the customer is not found or already has payment
     *                           details.
     */
    @Transactional
    public PaymentDetails createPaymentCard(Integer customerId, PaymentDetailsRequestDto paymentDetailsRequestDto) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        for (PaymentDetails card : customer.getPaymentCards()) {
            if (card.getCardNumber().equals(paymentDetailsRequestDto.getCardNumber())) {
                throw new GameShopException(HttpStatus.CONFLICT, "Payment details already exist.");
            }
        }

        PaymentDetails paymentDetails = new PaymentDetails(paymentDetailsRequestDto.getCardName(),
                paymentDetailsRequestDto.getPostalCode(),
                paymentDetailsRequestDto.getCardNumber(),
                paymentDetailsRequestDto.getExpMonth(),
                paymentDetailsRequestDto.getExpYear(),
                customer);
        paymentDetails = paymentDetailsRepository.save(paymentDetails);

        customer.addPaymentCard(paymentDetails);
        customerAccountRepository.save(customer);

        return paymentDetails;
    }

    /**
     * Updates an existing payment card for a specific customer by their ID.
     *
     * @param customerId               The ID of the customer.
     * @param paymentDetailsRequestDto The payment details data for update.
     * @return PaymentDetails representing the updated payment card.
     * @throws GameShopException if the customer or payment details are not found.
     */
    @Transactional
    public PaymentDetails updatePaymentCard(Integer customerId, int cardId,
            PaymentDetailsRequestDto paymentDetailsRequestDto) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        PaymentDetails card = paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId);
        if (card == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Card not found.");
        }

        if (!customer.getPaymentCards().contains(card)) {
            throw new GameShopException(HttpStatus.FORBIDDEN, "Card does not belong to the specified customer.");
        }

        card.setCardName(paymentDetailsRequestDto.getCardName());
        card.setPostalCode(paymentDetailsRequestDto.getPostalCode());
        card.setCardNumber(paymentDetailsRequestDto.getCardNumber());
        card.setExpMonth(paymentDetailsRequestDto.getExpMonth());
        card.setExpYear(paymentDetailsRequestDto.getExpYear());

        customerAccountRepository.save(customer);

        return card;
    }

    /**
     * Retrieves a specific payment card for a customer by customer ID and card ID.
     *
     * @param customerId The ID of the customer.
     * @param cardId     The ID of the payment card.
     * @return The specific PaymentDetails object.
     * @throws GameShopException if the customer or the card with customerId and
     *                           cardId respectively are not found
     */
    @Transactional
    public PaymentDetails getPaymentCardById(Integer customerId, Integer cardId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        PaymentDetails card = paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId);
        if (card == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Card not found.");
        }
        return card;
    }

    /**
     * Retrieves all payment cards for a specific customer by their ID.
     *
     * @param customerId The ID of the customer.
     * @return A list of PaymentDetails associated with the customer.
     * @throws GameShopException if the customer with customerId is not found
     */
    @Transactional
    public List<PaymentDetails> getAllPaymentCardsByCustomerId(Integer customerId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }
        return customer.getPaymentCards();
    }

    /**
     * Deletes a payment card for a specific customer and returns the details of the deleted card.
     *
     * @param customerId The ID of the customer who owns the payment card.
     * @param cardId     The ID of the payment card to be deleted.
     * @return PaymentDetails The deleted payment card object.
     * @throws GameShopException if the customer or card is not found, or if the card does not belong to the customer.
     */
    @Transactional
    public PaymentDetails deletePaymentCard(Integer customerId, Integer cardId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        PaymentDetails card = paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(cardId);
        if (card == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Card not found.");
        }

        if (!customer.getPaymentCards().contains(card)) {
            throw new GameShopException(HttpStatus.FORBIDDEN, "Card does not belong to the specified customer.");
        }

        // Remove the card from the customer's collection
        customer.removePaymentCard(card);

        customerAccountRepository.save(customer);

        //Delete the card from the repository
        paymentDetailsRepository.delete(card);

        return card; 
    }

    /**
     * Adds a game to the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @param gameId     The ID of the game to add to the wishlist.
     * @return The added Game object.
     * @throws GameShopException If the customer or game is not found.
     */
    @Transactional
    public Game addToWishlist(Integer customerId, Integer gameId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        Game game = gameRepository.findGameByGameEntityId(gameId);

        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found.");
        }
        customer.addWishListedGame(game);
        customerAccountRepository.save(customer);
        return game;
    }

    /**
     * Removes a game from the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @param gameId     The ID of the game to remove from the wishlist.
     * @throws GameShopException If the customer or game is not found.
     */
    @Transactional
    public Game removeFromWishlist(Integer customerId, Integer gameId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        Game game = gameRepository.findGameByGameEntityId(gameId);

        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game not found.");
        }
        customer.removeWishListedGame(game);
        customerAccountRepository.save(customer);
        return game;
    }

    /**
     * Retrieves the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @return A list of Game objects in the customer's wishlist.
     * @throws GameShopException If the customer is not found.
     */
    @Transactional
    public List<Game> viewWishlist(Integer customerId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);

        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer not found.");
        }

        return customer.getWishListedGames();
    }
}
