package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.OrderGame;

import java.sql.Date;

@SpringBootTest
public class CustomerOrderRepositoryTests {
    @Autowired
    private CustomerOrderRepository customerOrderRepo;
    @Autowired
    private CustomerAccountRepository customerAccountRepo;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private OrderGameRepository orderGameRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderGameRepo.deleteAll();
        customerOrderRepo.deleteAll();
        paymentDetailsRepo.deleteAll();
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerAccountRepo.deleteAll();
    }

    @Test
    @Transactional
    void testCreateAndReadCustomerOrder() {
        // ---- Arrange
        Date orderDate = new Date(System.currentTimeMillis()); // Current date for order

        CustomerAccount customer = new CustomerAccount("mohamed-amine@email.com", "MyPasswordTest");
        customer = customerAccountRepo.save(customer);

        PaymentDetails paymentInfo = new PaymentDetails("MAB", "XXX XXX", "123456789", 10, 2024, customer);
        paymentInfo = paymentDetailsRepo.save(paymentInfo);

        GameCategory category1 = new GameCategory(true, "War Games");
        category1 = gameCategoryRepo.save(category1);

        Game game1 = new Game("COD", "Call Of Duty", "https://www.url.ca", 10, true, 20.99, category1);
        game1 = gameRepo.save(game1);

        CustomerOrder order = new CustomerOrder(orderDate, customer, paymentInfo);
        order = customerOrderRepo.save(order);

        OrderGame orderGame1 = new OrderGame(order, game1);
        orderGame1 = orderGameRepo.save(orderGame1);

        order.addOrderedGame(orderGame1);

        order = customerOrderRepo.save(order);

        // ---- Act
        CustomerOrder savedOrder = customerOrderRepo.findOrderByOrderId(order.getOrderId());

        // ---- Assert
        // Asserting the attributes
        assertNotNull(savedOrder);
        assertEquals(savedOrder.getOrderId(), order.getOrderId());
        assertEquals(savedOrder.getOrderStatus(), order.getOrderStatus());
        assertEquals(savedOrder.getOrderDate().toString(), orderDate.toString());

        // Asserting the associations
        assertEquals(savedOrder.getOrderedGame(0).getOrderGameId(), orderGame1.getOrderGameId());
        assertEquals(savedOrder.getOrderedBy().getCustomerId(), customer.getCustomerId());
        assertEquals(savedOrder.getPaymentInformation().getPaymentDetailsId(), paymentInfo.getPaymentDetailsId());
    }
}