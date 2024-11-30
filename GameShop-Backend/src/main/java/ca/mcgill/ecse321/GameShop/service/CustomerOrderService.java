package ca.mcgill.ecse321.GameShop.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;
import jakarta.transaction.Transactional;

@Service
public class CustomerOrderService {

    private static final int RETURN_PERIOD_DAYS = 7;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private GameRepository gameRepository;

    /**
     * Create a new customer order.
     * 
     * @param customerOrderRequestDto the details for creating the order
     * @return CustomerOrder the created order
     * @throws GameShopException if games are not found, are not available, or are
     *                           out of stock
     */
    @Transactional
    public CustomerOrder createCustomerOrder(CustomerOrderRequestDto customerOrderRequestDto) {

        List<Game> games = validateAndFetchGames(customerOrderRequestDto.getOrderedGameIds());
        CustomerAccount customer = validateCustomer(customerOrderRequestDto.getCustomerId());
        PaymentDetails paymentDetails = validatePaymentDetails(customer,
                customerOrderRequestDto.getPaymentInformationId());

        // Create CustomerOrder object
        CustomerOrder customerOrder = new CustomerOrder(java.sql.Date.valueOf(LocalDate.now()), customer,
                paymentDetails);

        // Set status to delivered
        customerOrder.setOrderStatus(OrderStatus.DELIVERED);

        // Create and add OrderGame objects to the CustomerOrder
        for (Game game : games) {
            OrderGame orderGame = new OrderGame(customerOrder, game);
            customerOrder.addOrderedGame(orderGame);

            // Update game quantity in stock
            game.setQuantityInStock(game.getQuantityInStock() - 1);
            gameRepository.save(game);
        }

        return customerOrderRepository.save(customerOrder);
    }

    /**
     * Return a customer order automatically if it was placed within 7 days.
     * 
     * @param orderId the id of the order to return
     * @return CustomerOrder the returned order
     * @throws GameShopException if order is not found, in shipping, or has already
     *                           been returned
     */
    @Transactional
    public CustomerOrder returnCustomerOrder(int orderId) {
        CustomerOrder order = customerOrderRepository.findOrderByOrderId(orderId);

        // Check if order exists
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found");
        }

        // Check if order is in shipping
        if (order.getOrderStatus() == OrderStatus.SHIPPING) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Order with ID " + orderId + " is in shipping");
        }

        // Check if order has already been returned
        if (order.getOrderStatus() == OrderStatus.RETURNED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    "Order with ID " + orderId + " has already been returned");
        }

        // Check if order is delivered
        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    "Order with ID " + orderId + " must be delivered before it can be returned");
        }

        // Check if order was placed within the return period
        LocalDate orderDate = order.getOrderDate().toLocalDate();
        LocalDate currentDate = LocalDate.now();
        long daysSinceOrder = ChronoUnit.DAYS.between(orderDate, currentDate);

        if (daysSinceOrder > RETURN_PERIOD_DAYS) {
            throw new GameShopException(HttpStatus.BAD_REQUEST,
                    "Order with ID " + orderId + " was placed more than " + RETURN_PERIOD_DAYS + " days ago");
        }

        // Automatically set order status to returned
        order.setOrderStatus(OrderStatus.RETURNED);

        return customerOrderRepository.save(order);
    }

    /**
     * Get a customer order by its id.
     * 
     * @param orderId the id of the order to get
     * @return CustomerOrder the order
     * @throws GameShopException if order is not found
     */
    @Transactional
    public CustomerOrder getCustomerOrderById(int orderId) {
        CustomerOrder order = customerOrderRepository.findOrderByOrderId(orderId);

        // Check if order exists
        if (order == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " not found");
        }

        return order;
    }

    /**
     * Get all customer orders.
     * 
     * @return List of all customer orders
     */
    @Transactional
    public List<CustomerOrder> getAllCustomerOrders() {
        return (List<CustomerOrder>) customerOrderRepository.findAll();
    }

    /**
     * Validates and retrieves a list of games by their IDs.
     * 
     * @param gameIds the list of game IDs to validate and fetch
     * @return List of valid Game objects
     * @throws GameShopException if any game is not found, not available, or out of
     *                           stock
     */
    private List<Game> validateAndFetchGames(List<Integer> gameIds) {
        List<Game> games = new ArrayList<>();
        for (int gameId : gameIds) {
            Game game = gameRepository.findGameByGameEntityId(gameId);
            if (game == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND, "Game with ID " + gameId + " not found");
            }
            if (!game.getIsAvailable()) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Game with ID " + gameId + " is not available");
            }
            if (game.getQuantityInStock() == 0) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Game with ID " + gameId + " is out of stock");
            }
            games.add(game);
        }
        return games;
    }

    /**
     * Validates the existence of a customer by their ID.
     * 
     * @param customerId the ID of the customer to validate
     * @return CustomerAccount object if found
     * @throws GameShopException if the customer is not found
     */
    private CustomerAccount validateCustomer(Integer customerId) {
        CustomerAccount customer = customerAccountRepository.findCustomerAccountByCustomerId(customerId);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Customer with ID " + customerId + " not found");
        }
        return customer;
    }

    /**
     * Validates the existence of payment details and checks if it belongs to the
     * given customer.
     * 
     * @param customer             the CustomerAccount object to validate against
     * @param paymentInformationId the ID of the payment details to validate
     * @return PaymentDetails object if valid
     * @throws GameShopException if payment details are not found or do not belong
     *                           to the customer
     */
    private PaymentDetails validatePaymentDetails(CustomerAccount customer, Integer paymentInformationId) {
        PaymentDetails paymentDetails = paymentDetailsRepository
                .findPaymentDetailsByPaymentDetailsId(paymentInformationId);
        if (paymentDetails == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    "Payment information with ID " + paymentInformationId + " not found");
        }
        if (paymentDetails.getCardOwner().getCustomerId() != customer.getCustomerId()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Payment information with ID " + paymentInformationId
                    + " does not belong to customer with ID " + customer.getCustomerId());
        }
        return paymentDetails;
    }
}
