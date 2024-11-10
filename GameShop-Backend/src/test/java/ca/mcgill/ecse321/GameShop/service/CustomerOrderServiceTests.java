package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerAccountRepository;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;
import ca.mcgill.ecse321.GameShop.repository.GameRepository;
import ca.mcgill.ecse321.GameShop.repository.OrderGameRepository;
import ca.mcgill.ecse321.GameShop.repository.PaymentDetailsRepository;

@SpringBootTest
public class CustomerOrderServiceTests {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;

    @Mock
    private OrderGameRepository OrderGameRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    @Test
    public void testCreateCustomerOrder() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("exmaple@mcgill.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId());

        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf(LocalDate.now()), orderedGameIds,
                customer.getCustomerId(), paymentDetails.getPaymentDetailsId());

        // Mock repository behavior
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(any(Integer.class)))
                .thenReturn(paymentDetails);
        when(gameRepository.findGameByGameEntityId(any(Integer.class))).thenReturn(game1);
        when(OrderGameRepository.save(any(OrderGame.class))).thenReturn(orderGame1);
        when(OrderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(orderGame1);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(customer);
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        CustomerOrder response = customerOrderService.createCustomerOrder(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(customer.getCustomerId(), response.getOrderedBy().getCustomerId());
        assertEquals(paymentDetails.getPaymentDetailsId(), response.getPaymentInformation().getPaymentDetailsId());
        assertEquals(Date.valueOf(LocalDate.now()), response.getOrderDate());
        verify(customerOrderRepository, times(1)).save(any(CustomerOrder.class));

    }

    @Test
    public void testCreateCustomerOrderWithInvalidCustomer() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("Not John Doe", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId());

        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf(LocalDate.now()), orderedGameIds,
                customer.getCustomerId(), paymentDetails.getPaymentDetailsId());

        // Mock repository behavior
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(any(Integer.class)))
                .thenReturn(paymentDetails);
        when(gameRepository.findGameByGameEntityId(any(Integer.class))).thenReturn(game1);
        when(OrderGameRepository.save(any(OrderGame.class))).thenReturn(orderGame1);
        when(OrderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(orderGame1);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(null);
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.createCustomerOrder(requestDto));

        // Assert
        assertEquals("Customer not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testCreateCustomerOrderWithInvalidPaymentDetails() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("John Doe", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");

        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);

        List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId());

        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf(LocalDate.now()), orderedGameIds,
                customer.getCustomerId(), paymentDetails.getPaymentDetailsId());

        // Mock repository behavior
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(any(Integer.class))).thenReturn(null);
        when(gameRepository.findGameByGameEntityId(any(Integer.class))).thenReturn(game1);
        when(OrderGameRepository.save(any(OrderGame.class))).thenReturn(orderGame1);
        when(OrderGameRepository.findOrderGameById(any(Integer.class))).thenReturn(orderGame1);
        when(customerAccountRepository.findCustomerAccountByCustomerId(any(Integer.class))).thenReturn(customer);
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.createCustomerOrder(requestDto));

        // Assert
        assertEquals("Payment information not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());

    }

    @Test
    public void testReturnCustomerOrder() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("johndoe@gmail.com", "password123");

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        customerOrder.setOrderStatus(OrderStatus.DELIVERED);

        // Mock repository behavior
        when(customerOrderRepository.findById(any(Integer.class))).thenReturn(Optional.of(customerOrder));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(customerOrder);

        // Act
        CustomerOrder response = customerOrderService.returnCustomerOrder(customerOrder.getOrderId());

        // Assert
        assertNotNull(response);
        assertEquals(OrderStatus.RETURNED, response.getOrderStatus());
        verify(customerOrderRepository, times(1)).findById(customerOrder.getOrderId());
        verify(customerOrderRepository, times(1)).save(customerOrder);
    }

    @Test
    public void testReturnCustomerOrderWithInvalidOrder() {
        // Arrange
        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf(LocalDate.now()), List.of(1), 1,
                1);

        // Mock repository behavior
        when(customerOrderRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.returnCustomerOrder(requestDto.getOrderedById()));

        // Assert
        assertEquals("Order not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());

    }

    @Test
    public void testReturnCustomerOrderWithAlreadyReturnedOrder() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("exmaple@mcgill.com", "password123");

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        customerOrder.setOrderStatus(OrderStatus.RETURNED);

        // Mock repository behavior
        when(customerOrderRepository.findById(any(Integer.class))).thenReturn(Optional.of(customerOrder));

        // Act
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.returnCustomerOrder(customerOrder.getOrderId()));

        // Assert
        assertEquals("Order has already been returned", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testMonitorOrderStatuses() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("example@gmail.com", "password123");

        // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        customerOrder.setOrderStatus(OrderStatus.DELIVERED);

        // Mock repository behavior
        when(customerOrderRepository.findAll()).thenReturn(List.of(customerOrder));

        // Act
        CustomerOrder response = customerOrderService.monitorOrderStatuses(customerOrder.getOrderId());

        // Assert
        assertNotNull(response);
        assertEquals(OrderStatus.DELIVERED, response.getOrderStatus());
        verify(customerOrderRepository, times(1)).findAll();
    }
}
