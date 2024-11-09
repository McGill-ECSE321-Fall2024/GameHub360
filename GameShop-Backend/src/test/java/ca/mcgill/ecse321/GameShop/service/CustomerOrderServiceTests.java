package ca.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.dto.CustomerOrderRequestDto;
import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.repository.CustomerOrderRepository;

@SpringBootTest
public class CustomerOrderServiceTests {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    @Test
    public void testCreateCustomerOrder() {
        // Arrange
        CustomerAccount customer = new CustomerAccount("exmaple@mcgill.com", "password123");
        GameCategory gameCategory = new GameCategory(true, "Action");
        GameCategory gameCategory2 = new GameCategory(true, "love");
        //Game(String aName, String aDescription, String aImageURL, int aQuantityInStock, boolean aIsAvailable, double aPrice, GameCategory... allCategories)
        Game game1 = new Game("Game1", "desc", "imageurl", 10, true, 10.0, gameCategory);
        Game game2 = new Game("Game2", "desc2", "image2url", 20, true, 20.0, gameCategory);
        Game game3 = new Game("Game3", "desc3", "image3url", 30, true, 30.0, gameCategory2);

            // Create the CustomerOrder object
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "H3H 1A7", 123456789, 12, 2023, customer);
        CustomerOrder customerOrder = new CustomerOrder(Date.valueOf(LocalDate.now()), customer, paymentDetails);
        OrderGame orderGame1 = new OrderGame(customerOrder, game1);
        OrderGame orderGame2 = new OrderGame(customerOrder, game2);
        OrderGame orderGame3 = new OrderGame(customerOrder, game3);

        List<Integer> orderedGameIds = List.of(orderGame1.getOrderGameId(), orderGame2.getOrderGameId(), orderGame3.getOrderGameId());

       CustomerOrderRequestDto requestDto = new CustomerOrderRequestDto(Date.valueOf(LocalDate.now()), orderedGameIds, customer.getCustomerId() ,paymentDetails.getPaymentDetailsId());

       // Mock repository behavior
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
}

