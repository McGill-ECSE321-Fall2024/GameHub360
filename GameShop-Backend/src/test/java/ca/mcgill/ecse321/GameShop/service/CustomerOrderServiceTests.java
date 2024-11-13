package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.exception.GameShopException;
import ca.mcgill.ecse321.GameShop.model.*;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder.OrderStatus;
import ca.mcgill.ecse321.GameShop.repository.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
public class CustomerOrderServiceTests {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    @Test
    public void testCreateCustomerOrderSuccess() {
        // Arrange
        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Arrays.asList(1, 2), 1001, 2001);
        CustomerAccount customer = new CustomerAccount();

        Game game1 = new Game();
        game1.setQuantityInStock(10);
        game1.setIsAvailable(true);

        Game game2 = new Game();
        game2.setQuantityInStock(5);
        game2.setIsAvailable(true);

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardOwner(customer);

        when(customerAccountRepository.findCustomerAccountByCustomerId(2001)).thenReturn(customer);
        when(paymentDetailsRepository.findPaymentDetailsByPaymentDetailsId(1001)).thenReturn(paymentDetails);
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(game1);
        when(gameRepository.findGameByGameEntityId(2)).thenReturn(game2);
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CustomerOrder response = customerOrderService.createCustomerOrder(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(customer, response.getOrderedBy());
        assertEquals(paymentDetails, response.getPaymentInformation());
        assertEquals(2, response.getOrderedGames().size());
        verify(gameRepository, times(2)).save(any(Game.class));
        verify(customerOrderRepository, times(1)).save(any(CustomerOrder.class));
    }

    @Test
    public void testCreateCustomerOrderGameNotFound() {
        // Arrange
        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Collections.singletonList(1), 1001, 2001);
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(null);

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.createCustomerOrder(requestDto));
        assertEquals("Game with ID 1 not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testCreateCustomerOrderCustomerNotFound() {
        // Arrange
        CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Collections.singletonList(1), 1001, 2001);
        Game game = new Game();
        game.setQuantityInStock(5);
        game.setIsAvailable(true);
        when(gameRepository.findGameByGameEntityId(1)).thenReturn(game);
        when(customerAccountRepository.findCustomerAccountByCustomerId(2001)).thenReturn(null);

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class,
                () -> customerOrderService.createCustomerOrder(requestDto));
        assertEquals("Customer with ID 2001 not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testReturnCustomerOrderSuccess() {
        // Arrange
        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(Date.valueOf(LocalDate.now().minusDays(5)));
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(customerOrderRepository.findOrderByOrderId(1)).thenReturn(order);
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CustomerOrder response = customerOrderService.returnCustomerOrder(1);

        // Assert
        assertNotNull(response);
        assertEquals(OrderStatus.RETURNED, response.getOrderStatus());
        verify(customerOrderRepository, times(1)).save(order);
    }

    @Test
    public void testReturnCustomerOrderNotFound() {
        // Arrange
        when(customerOrderRepository.findOrderByOrderId(1)).thenReturn(null);

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerOrderService.returnCustomerOrder(1));
        assertEquals("Order with ID 1 not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }

    @Test
    public void testReturnCustomerOrderOutsideReturnPeriod() {
        // Arrange
        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(Date.valueOf(LocalDate.now().minusDays(10)));
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(customerOrderRepository.findOrderByOrderId(1)).thenReturn(order);

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerOrderService.returnCustomerOrder(1));
        assertEquals("Order with ID 1 was placed more than 7 days ago", e.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }

    @Test
    public void testGetCustomerOrderByIdSuccess() {
        // Arrange
        CustomerOrder order = new CustomerOrder();
        when(customerOrderRepository.findOrderByOrderId(1)).thenReturn(order);

        // Act
        CustomerOrder response = customerOrderService.getCustomerOrderById(1);

        // Assert
        assertNotNull(response);
        verify(customerOrderRepository, times(1)).findOrderByOrderId(1);
    }

    @Test
    public void testGetCustomerOrderByIdNotFound() {
        // Arrange
        when(customerOrderRepository.findOrderByOrderId(1)).thenReturn(null);

        // Act & Assert
        GameShopException e = assertThrows(GameShopException.class, () -> customerOrderService.getCustomerOrderById(1));
        assertEquals("Order with ID 1 not found", e.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
}
