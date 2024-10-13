package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        paymentDetailsRepo.deleteAll();
        orderGameRepo.deleteAll();
        gameRepo.deleteAll();
        gameCategoryRepo.deleteAll();
        customerOrderRepo.deleteAll();
        customerAccountRepo.deleteAll();
    }

    @Test
    @Transactional
    void testCreateAndReadCustomerOrder() {
        // 1. Create and save related entities
        // ---- Attributes
        Date orderDate = new Date(System.currentTimeMillis()); 

        // ---- Associations
        CustomerAccount customer = new CustomerAccount("mohamed-amine@email.com", "MyPasswordTest");
        customer = customerAccountRepo.save(customer);

        PaymentDetails paymentInfo = new PaymentDetails("MAB", "XXX XXX", 123456789, 10, 2024, customer);
        paymentInfo = paymentDetailsRepo.save(paymentInfo);

        GameCategory category1 = new GameCategory(true, "War Games");
        category1 = gameCategoryRepo.save(category1);

        GameCategory category2 = new GameCategory(true, "Arcade");
        category2 = gameCategoryRepo.save(category2);

        Game game1 = new Game("COD", "Call Of Duty", "https://www.url.ca", 10, true, 20.99, category1);
        game1 = gameRepo.save(game1);

        Game game2 = new Game("Mario", "It's me Mario!", "https://www.mario.ca", 10, true, 25.99, category2);
        game2 = gameRepo.save(game2);

        // ---- Initialize and save CustomerOrder
        CustomerOrder order = new CustomerOrder(orderDate, customer, paymentInfo);
        order = customerOrderRepo.save(order); 

        // ---- Add Game Orders to the Customer Order
        OrderGame orderGame1 = new OrderGame(order, game1);
        orderGame1.setCustomerOrder(order);
        orderGame1 = orderGameRepo.save(orderGame1);

        OrderGame orderGame2 = new OrderGame(order, game2);
        orderGame2.setCustomerOrder(order); 
        orderGame2 = orderGameRepo.save(orderGame2);

        order.addOrderedGame(orderGame1); 
        order.addOrderedGame(orderGame2);

        // ---- Save Customer Order with Game Orders
        order = customerOrderRepo.save(order);

        // 3. Read the object from the database using the repository
        CustomerOrder savedOrder = customerOrderRepo.findOrderByOrderId(order.getOrderId());

        // 4. Assert that the object from the database has the correct attributes
        assertNotNull(savedOrder);
        assertEquals(orderDate.toString(), savedOrder.getOrderDate().toString());

        assertNotNull(savedOrder.getOrderedBy());
        assertEquals(customer.getEmail(), savedOrder.getOrderedBy().getEmail());
        assertEquals(customer.getPassword(), savedOrder.getOrderedBy().getPassword());

        assertNotNull(savedOrder.getPaymentInformation());
        assertEquals(paymentInfo.getCardName(), savedOrder.getPaymentInformation().getCardName());
        assertEquals(paymentInfo.getCardNumber(), savedOrder.getPaymentInformation().getCardNumber());

        assertEquals(2, savedOrder.getOrderedGames().size());
        assertTrue(savedOrder.getOrderedGames().contains(orderGame1));
        assertTrue(savedOrder.getOrderedGames().contains(orderGame2));
    }
}