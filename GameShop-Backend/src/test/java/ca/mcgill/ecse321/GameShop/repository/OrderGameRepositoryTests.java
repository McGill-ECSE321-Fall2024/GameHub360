package ca.mcgill.ecse321.GameShop.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.GameShop.model.CustomerAccount;
import ca.mcgill.ecse321.GameShop.model.CustomerOrder;
import ca.mcgill.ecse321.GameShop.model.Game;
import ca.mcgill.ecse321.GameShop.model.GameCategory;
import ca.mcgill.ecse321.GameShop.model.OrderGame;
import ca.mcgill.ecse321.GameShop.model.PaymentDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

@SpringBootTest
public class OrderGameRepositoryTests {
    @Autowired
    private OrderGameRepository repo;
    @Autowired
    private CustomerAccountRepository customerAccRepo;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepo;
    @Autowired
    private CustomerOrderRepository customerOrderRepo;
    @Autowired
    private GameCategoryRepository gameCategoryRepo;
    @Autowired 
    private GameRepository gameRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        repo.deleteAll();
    }

    @Test
    @Transactional
    void testCreateAndReadOrderGame(){

        // Arrange
        Date orderDate = new Date(System.currentTimeMillis());

        CustomerAccount orderedBy = new CustomerAccount("myemail@mail.com", "pwd");
        orderedBy = customerAccRepo.save(orderedBy);
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails = paymentDetailsRepo.save(paymentDetails);
        CustomerOrder customerOrder = new CustomerOrder(orderDate, orderedBy, paymentDetails);
        customerOrder = customerOrderRepo.save(customerOrder);

        GameCategory gameCategory = new GameCategory(true, "MyCat");
        gameCategory = gameCategoryRepo.save(gameCategory);
        Game game = new Game("myGame", "MyDescription", "https:\\myurl.com", 3, true, 10.99, gameCategory);
        game = gameRepo.save(game);

        OrderGame orderGame = new OrderGame(customerOrder, game);
        orderGame = repo.save(orderGame);

        // Act
        OrderGame retrievedOrderGame = repo.findOrderGameById(orderGame.getOrderGameId());

        // Assert
        assertNotNull(retrievedOrderGame);
        assertNotNull(retrievedOrderGame.getOrderGameId());
        assertEquals(customerOrder.getOrderId(), retrievedOrderGame.getCustomerOrder().getOrderId());
        assertEquals(game.getGameEntityId(), retrievedOrderGame.getGame().getGameEntityId());
        assertEquals(customerOrder.getOrderDate().toString(), retrievedOrderGame.getCustomerOrder().getOrderDate().toString());
        assertEquals(game.getName(), retrievedOrderGame.getGame().getName());
    }
    
}