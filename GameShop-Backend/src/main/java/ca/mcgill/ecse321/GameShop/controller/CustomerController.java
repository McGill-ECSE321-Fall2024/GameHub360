package ca.mcgill.ecse321.GameShop.controller;

import ca.mcgill.ecse321.GameShop.dto.*;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
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
     * Endpoint to retrieve order history for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of OrderResponseDto representing the order history of the customer.
     */
    @GetMapping("/{customerId}/orders")
    public List<OrderResponseDto> viewOrderHistory(@PathVariable Integer customerId) {
        return customerService.getOrderHistoryByCustomerId(customerId).stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint to retrieve a specific payment card for a customer.
     *
     * @param customerId The ID of the customer.
     * @param cardId     The ID of the payment card.
     * @return PaymentDetailsResponseDto representing the payment card details.
     */
    @GetMapping("/{customerId}/card/{cardId}")
    public PaymentDetailsResponseDto getPaymentCardById(@PathVariable Integer customerId, @PathVariable Integer cardId) {
        PaymentDetails paymentDetails = customerService.getPaymentCardById(customerId, cardId);
        return new PaymentDetailsResponseDto(paymentDetails);
    }

    /**
     * Endpoint to retrieve all payment cards for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of PaymentDetailsResponseDto representing the customer's payment cards.
     */
    @GetMapping("/{customerId}/cards")
    public List<PaymentDetailsResponseDto> getAllPaymentCards(@PathVariable Integer customerId) {
        List<PaymentDetails> paymentDetailsList = customerService.getAllPaymentCardsByCustomerId(customerId);
        return paymentDetailsList.stream()
                .map(PaymentDetailsResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint to create or update a payment card for a specific customer.
     *
     * @param customerId               The ID of the customer.
     * @param paymentDetailsRequestDto The payment details data for creation or update.
     * @return PaymentDetailsResponseDto representing the created/updated payment details.
     */
    @PostMapping("/{customerId}/payment")
    public PaymentDetailsResponseDto updatePaymentCards(@PathVariable Integer customerId,
                                                        @RequestBody PaymentDetailsRequestDto paymentDetailsRequestDto) {
        PaymentDetails paymentDetails = customerService.createOrUpdatePaymentCard(customerId, paymentDetailsRequestDto);
        return new PaymentDetailsResponseDto(paymentDetails);
    }

    /**
     * Adds a game to the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @param gameId     The ID of the game to add.
     * @return GameResponseDto representing the added game.
     */
    @PostMapping("/{customerId}/wishlist/{gameId}")
    public GameResponseDto addToWishlist(@PathVariable Integer customerId, @PathVariable Integer gameId) {
        Game game = customerService.addToWishlist(customerId, gameId);
        return new GameResponseDto(game);
    }

    /**
     * Removes a game from the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @param gameId     The ID of the game to remove.
     */
    @DeleteMapping("/{customerId}/wishlist/{gameId}")
    public GameResponseDto removeFromWishlist(@PathVariable Integer customerId, @PathVariable Integer gameId) {
        Game game = customerService.removeFromWishlist(customerId, gameId);
        return new GameResponseDto(game);
    }

    /**
     * Retrieves all games in the customer's wishlist.
     *
     * @param customerId The ID of the customer.
     * @return A list of GameResponseDto representing the games in the wishlist.
     */
    @GetMapping("/{customerId}/wishlist")
    public List<GameResponseDto> viewWishlist(@PathVariable Integer customerId) {
        List<Game> wishlist = customerService.viewWishlist(customerId);
        return wishlist.stream()
                .map(GameResponseDto::new)
                .collect(Collectors.toList());
    }
}
